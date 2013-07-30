package servlets;

import static classes.Constants.databaseName;
import static classes.Constants.itemCollection;
import static classes.Constants.itemIDField;
import static classes.Constants.itemName;
import static classes.Constants.numberOfRecommendations;
import static classes.Constants.userCollection;
import static classes.Constants.userIDField;
import static classes.Constants.userPrefs;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.types.BasicBSONList;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.QueryBuilder;

/**
 * Servlet implementation class GetRecommendations
 */
@WebServlet("/GetRecommendations")
public class GetRecommendations extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetRecommendations() {
		super();
	}

	/*
	 * HTTP Method: doGet
	 * -------------------
	 * Upon POST, run the recommendation
	 * algorithm and return the user's favorite items as well as a list of
	 * numberOfRecommendations more that they might enjoy.
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		// Grab the ServletContext.
		ServletContext context = request.getSession().getServletContext();

		// Grab the MongoClient and create a database or get it if it already
		// exists.
		MongoClient m = (MongoClient) context.getAttribute("mongo");
		DB db = m.getDB(databaseName);

		// Get user information from the request.
		String unique_id = request.getParameter(userIDField);

		// Quick check to see if the user is logged in.
		if (unique_id == null) {
			String message = "Please login.";
			request.setAttribute("message", message);
			request.getRequestDispatcher("login.jsp")
					.forward(request, response);
			return;
		}

		// Set up a user object to represent the user in the query.
		BasicDBObject mainUser = new BasicDBObject();
		mainUser.put(userIDField, unique_id);

		// Retrieve the appropriate user object from the collection.
		DBCollection coll = db.getCollection(userCollection);
		QueryBuilder mainUserQuery = new QueryBuilder();
		mainUserQuery.put(userIDField).is(unique_id);
		DBObject mainUserObject = coll.findOne(mainUserQuery.get());

		// New instantiations for images and plot.
		ArrayList<String> images = new ArrayList<String>();
		ArrayList<String> plots = new ArrayList<String>();
		
		// Dispatch the movies that the user likes.
		ArrayList<String> films = identifyTopMovies(db, mainUserObject,
				unique_id, images, plots);

		// Set the value of the message and dispatch to the JSP.
		request.setAttribute("films", films);
		request.setAttribute("images", images);
		request.setAttribute("plots", plots);
		request.setAttribute(userIDField, unique_id);
		request.getRequestDispatcher("recs.jsp").forward(request, response);
		
	}

	/*
	 * Helper String: identifyTopMovies
	 * --------------------------------
	 * Finds all of the top movies for the given user using aggregation.
	 */
	private ArrayList<String> identifyTopMovies(DB db, DBObject mainUser,
			String unique_id, ArrayList<String> images, ArrayList<String> plots) {

		// Declare the return ArrayList.
		ArrayList<String> results = new ArrayList<String>();

		// Get the user collection.
		DBCollection users = db.getCollection(userCollection);

		// Get the movie collection.
		DBCollection items = db.getCollection(itemCollection);

		// Get the user's list of favorite items - used in aggregation.
		BasicBSONList mainFavorites = (BasicBSONList) mainUser.get(userPrefs);

		// Create the matching command.
		BasicDBObject match = new BasicDBObject("$match", new BasicDBObject(
				userPrefs, new BasicDBObject("$in", mainFavorites)));

		// Project over the favorites array.
		BasicDBObject project = new BasicDBObject("$project",
				new BasicDBObject(userPrefs, "$favorites"));

		// Unwind so every favorite movie is separate.
		BasicDBObject unwind = new BasicDBObject("$unwind", "$favorites");

		// Count the number of occurrences of each movie and group them as such.
		BasicDBObject groupFields = new BasicDBObject("_id", "$favorites");
		groupFields.put("score", new BasicDBObject("$sum", 1));
		BasicDBObject group = new BasicDBObject("$group", groupFields);

		// Remove any items that are already among the main user's favorites.
		BasicDBObject filter = new BasicDBObject("$match", new BasicDBObject(
				"_id", new BasicDBObject("$nin", mainFavorites)));

		// Sort the items in descending order (highest scored items first).
		BasicDBObject sort = new BasicDBObject("$sort", new BasicDBObject(
				"score", -1));

		// Limit the number of returned items to numberOfRecommendations.
		BasicDBObject limit = new BasicDBObject("$limit",
				numberOfRecommendations);

		AggregationOutput output = users.aggregate(match, project, unwind,
				group, filter, sort, limit);

		// Iterate through the output, look up the names of the movies, add them
		// to the result string.
		for (DBObject obj : output.results()) {
			int movieNum = Integer.parseInt(obj.get("_id").toString());
			BasicDBObject query = new BasicDBObject(itemIDField, movieNum);
			DBObject recommendation = items.findOne(query);
			String title = recommendation.get(itemName).toString();
			images.add(recommendation.get("img").toString());
			plots.add(recommendation.get("plot").toString());
			results.add(title);
		}

		return results;
	}
	
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {}

}
