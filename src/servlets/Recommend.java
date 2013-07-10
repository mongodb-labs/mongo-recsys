package servlets;

import static classes.Constants.*;

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
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.QueryBuilder;


/**
 * Servlet implementation class Recommend
 */
@WebServlet("/Recommend")
public class Recommend extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/*
	 * Constructor: Recommend
	 * ----------------------
	 * Invokes the parent class' constructor.
	 */
	public Recommend() {
		super();
	}

	/*
	 * HTTP Method: doGet
	 * ------------------
	 * Used by home.jsp to redirect the user to search.jsp, passing the unique_id as well.
	 * This method also gets the 
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		// Grab the MongoClient from the ServletContext.
		ServletContext context = request.getSession().getServletContext();

		// Grab the MongoClient, the database, and the collection.
		MongoClient m = (MongoClient) context.getAttribute("mongo");
		DB db = m.getDB(databaseName);
		DBCollection coll = db.getCollection(itemCollection);

		// Get the unique genres.
		BasicBSONList arr = (BasicBSONList) coll.distinct("genre");

		// Iterate through the collection and put the names of every genre in an ArrayList.
		ArrayList<String> genres = new ArrayList<String>();
		for (int i = 0; i < arr.size(); i++) {
			genres.add(arr.get(i).toString());
		}
		
		String unique_id = request.getParameter("unique_id");
		request.setAttribute("genres", genres);
		request.setAttribute("unique_id", unique_id);
		request.getRequestDispatcher("search.jsp").forward(request, response);

	}

	/*
	 * HTTP Method: doPost
	 * -------------------
	 * Upon POST, run the recommendation algorithm and return the user's
	 * favorite items as well as a list of numberOfRecommendations more
	 * that they might enjoy.
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// Grab the ServletContext.
		ServletContext context = request.getSession().getServletContext();

		// Grab the MongoClient and create a database or get it if it already exists.
		MongoClient m = (MongoClient) context.getAttribute("mongo");
		DB db = m.getDB(databaseName);

		// Get user information from the request.
		String unique_id = request.getParameter("unique_id");
		
		// Quick check to see if the user is logged in.
		if(unique_id == null) {
			String message = "Please login.";
			request.setAttribute("message", message);
			request.getRequestDispatcher("login.jsp").forward(request, response);
			return;
		}
		
		// Set up a user object to represent the user in the query.
		BasicDBObject mainUser = new BasicDBObject();
		mainUser.put("unique_id", unique_id);

		// Retrieve the appropriate user object from the collection.
		DBCollection coll = db.getCollection("users");
		QueryBuilder mainUserQuery = new QueryBuilder();
		mainUserQuery.put("unique_id").is(unique_id);
		DBObject mainUserObject = coll.findOne(mainUserQuery.get());

		// Grab the user's array from the user object.
		BasicBSONList arr = (BasicBSONList) mainUserObject.get("favorites");

		// Dispatch the movies that the user likes.
		String message = createRecommendation(db, mainUserObject, arr, unique_id);

		// Set the value of the message and dispatch to the JSP.
		request.setAttribute("unique_id", unique_id);
		request.setAttribute("message", message);
		request.getRequestDispatcher("home.jsp").forward(request, response);
	}

	/*
	 * String: createRecommendation
	 * ---------------------
	 * Creates the message that is dispatched to search.jsp in the doPost function.
	 * Calls buildSimilarCollection as well as identifyTopMovies, which get a list
	 * of users with similar preferences then construct a list of most recommended
	 * movies respectively.
	 */
	private String createRecommendation(DB db, DBObject mainUserObject,
			BasicBSONList arr, String unique_id) {

		// Basic setup operations for the message and collections.
		DBCollection dataColl = db.getCollection(itemCollection);
		DBCollection userColl = db.getCollection("users");
		String mainUserFavorites = "Favorites list for "
				+ mainUserObject.get("user") + ":<br>"
				+ getMainUserFavorites(mainUserObject, dataColl, userColl, arr);
		String message = mainUserFavorites;

		// Find the top numberOfRecommendations candidate movies for the mainUser.
		String recommended = identifyTopMovies(db, mainUserObject, unique_id);
		message += "<br>Recommended Films:<br>" + recommended;

		// Drop the similar collection. This will be deleted soon.
		DBCollection similar = db.getCollection("similar");
		similar.drop();

		return message;
	}

	/*
	 * Helper String: identifyTopMovies
	 * --------------------------------
	 * Finds all of the top movies for the given user using aggregation.
	 */
	private String identifyTopMovies(DB db, DBObject mainUser, String unique_id) {

		// Declare the return string.
		String result = "";
		
		// Get the user collection.
		DBCollection users = db.getCollection("users");
		
		// Get the movie collection.
		DBCollection items = db.getCollection(itemCollection);

		// Get the user's list of favorite items - used in aggregation.
		BasicBSONList mainFavorites = (BasicBSONList) mainUser.get("favorites");
		
		// Create the matching command.
		BasicDBObject match = new BasicDBObject("$match",
				new BasicDBObject("favorites", new BasicDBObject("$in", mainFavorites)));
		
		// Project over the favorites array.
		BasicDBObject project = new BasicDBObject("$project", new BasicDBObject("favorites", "$favorites"));
		
		// Unwind so every favorite movie is separate.
		BasicDBObject unwind = new BasicDBObject("$unwind", "$favorites");
		
		// Count the number of occurrences of each movie and group them as such.
		BasicDBObject groupFields = new BasicDBObject("_id", "$favorites");
		groupFields.put("score", new BasicDBObject("$sum", 1));
		BasicDBObject group = new BasicDBObject("$group", groupFields);
		
		// Remove any items that are already among the main user's favorites.
		BasicDBObject filter = new BasicDBObject("$match", new BasicDBObject("_id",
				new BasicDBObject("$nin", mainFavorites)));
				
		// Sort the items in descending order (highest scored items first).
		BasicDBObject sort = new BasicDBObject("$sort", new BasicDBObject("score", -1));
		
		// Limit the number of returned items to numberOfRecommendations.
		BasicDBObject limit = new BasicDBObject("$limit", numberOfRecommendations);
		
		AggregationOutput output = users.aggregate(match, project, unwind, group, filter, sort, limit);
				
		// Iterate through the output, look up the names of the movies, add them to the result string.
		for(DBObject obj : output.results()) {
			int movieNum = Integer.parseInt(obj.get("_id").toString());
			BasicDBObject query = new BasicDBObject("movie_id", movieNum);
			DBObject recommendation = items.findOne(query);
			String title = recommendation.get("title").toString();
			result += title + "<br>";
		}

		return result;
	}

	/*
	 * Helper String: getMainUserFavorites
	 * -----------------------------------
	 * Gets the main user's favorite items and formats them.
	 * All this function does is to pull the favorites array
	 * from the user object and make it readable as a String.
	 */
	private String getMainUserFavorites(DBObject mainUserObject,
			DBCollection data, DBCollection users, BasicBSONList arr) {
		String message = "";
		
		// This puts all of the mainUser's favorite items into the message.
		for (int i = 0; i < arr.size(); i++) {
			QueryBuilder q = new QueryBuilder();
			q.put("movie_id").is(arr.get(i));
			DBCursor cur = data.find(q.get());
			DBObject movieEntry = cur.next();
			message += movieEntry.get("title") + "\n<br>";
		}

		return message;
	}

}