package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.types.BasicBSONList;
import org.bson.types.ObjectId;

import classes.ValueComparator;

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
	 * Class variables. These can be changed to alter the recommendation algorithm.
	 */
	private static String databaseName = "daviddb";
	private static String itemCollection = "movies";
	private static int numberOfRecommendations = 5;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Recommend() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// Grab the MongoClient from the ServletContext.
		ServletContext context = request.getSession().getServletContext();

		// Grab the MongoClient and create a database.
		MongoClient m = (MongoClient) context.getAttribute("mongo");
		DB db = m.getDB(databaseName);

		// Get information from the request.
		String user = request.getParameter("user");

		// Set up a user BasicDBObject to be user in the query.
		BasicDBObject mainUser = new BasicDBObject();
		ObjectId mainId = new ObjectId(user);
		mainUser.put("_id", mainId);

		// Using the _id we build a query and retrieve the appropriate user object.
		DBCollection coll = db.getCollection("users");
		QueryBuilder mainUserQuery = new QueryBuilder();
		mainUserQuery.put("_id").is(mainUser.get("_id"));
		DBCursor cur = coll.find(mainUserQuery.get());
		DBObject mainUserObject = cur.next();

		// Grab the user's array from the user object.
		BasicBSONList arr = (BasicBSONList) mainUserObject.get("favorites");

		// Dispatch the movies that the user likes.
		String message = createRecommendation(db, mainUserObject, arr);

		// Set the value of the message and dispatch to the JSP.
		request.setAttribute("message", message);
		request.getRequestDispatcher("search.jsp").forward(request, response);
	}

	/*
	 * String: createRecommendation
	 * ---------------------
	 * Creates the message that is dispatched to search.jsp in the doPost function.
	 * Includes much of the algorithmic work.
	 */
	private String createRecommendation(DB db, DBObject mainUserObject, BasicBSONList arr) {

		// Basic set-up operations for the message and collections.
		DBCollection dataColl = db.getCollection(itemCollection);
		DBCollection userColl = db.getCollection("users");
		String mainUserFavorites = "Favorites list for " + mainUserObject.get("user") + ":<br>" +
		getMainUserFavorites(mainUserObject, dataColl, userColl, arr);
		String message = mainUserFavorites;

		// Get all of the users similar to our main user. This will be changed.
		buildSimilarCollection(db, userColl, mainUserObject);

		// Find the top five candidate movies for the mainUser.
		String recommended = identifyTopMovies(db, mainUserObject);
		message += "<br>Recommended Films:<br>" + recommended;

		// Drop the similar collection.
		DBCollection similar = db.getCollection("similar");
		similar.drop();

		return message;
	}

	/*
	 * Helper String: identifyTopMovies
	 * --------------------------------
	 * Finds all of the top movies for the given user.
	 */
	private String identifyTopMovies(DB db, DBObject mainUser) {

		// Declare the return string.
		String result = "";

		// Get the basic information we need for aggregation.
		DBCollection similar = db.getCollection("similar");
		DBCollection items = db.getCollection(itemCollection);
		BasicBSONList mainFavorites = (BasicBSONList) mainUser.get("favorites");

		// Create the aggregation pipeline and execute it.
		BasicDBObject unwind = new BasicDBObject();
		unwind.append("$unwind", "$favorites");
		BasicDBObject filter = new BasicDBObject();
		filter.append("$match", new BasicDBObject("favorites", new BasicDBObject("$nin", mainFavorites)));
		BasicDBObject sort = new BasicDBObject();
		sort.append("$sort", new BasicDBObject("score", 1).append("movie_id", 1));

		AggregationOutput output = similar.aggregate(unwind, filter, sort);

		// Declare the HashMap and TreeMap we need.
		HashMap<String, Integer> similarUsers = new HashMap<String, Integer>();
		ValueComparator bvc = new ValueComparator(similarUsers);
        TreeMap<String, Integer> sortedSimilarUsers = new TreeMap<String, Integer>(bvc);

		// Use the Iterator to traverse the collection.
		for (DBObject obj : output.results()) {
			int movieNum = Integer.parseInt(obj.get("favorites").toString());	
			int score = Integer.parseInt(obj.get("score").toString());
			BasicDBObject query = new BasicDBObject("movie_id", movieNum);
			DBObject recommendation = items.findOne(query);

			// Put the recommendation into a HashMap.
			String title = recommendation.get("title").toString();

			if(similarUsers.containsKey(title)) {
				int currentScore = similarUsers.get(title);
				similarUsers.put(title, currentScore + score);
			} else {
				similarUsers.put(title, score);
			}
		}

		sortedSimilarUsers.putAll(similarUsers);

		// Iterate through the HashMap and add the best options.
		@SuppressWarnings("rawtypes")
		Iterator it = sortedSimilarUsers.entrySet().iterator();
		int counter = 0;
		while(it.hasNext()) {
			if(counter >= numberOfRecommendations) break;
			@SuppressWarnings("unchecked")
			Map.Entry<String,Integer> pairs = (Map.Entry<String,Integer>)it.next();
			result += pairs.getKey() + "<br>";
			counter++;
		}

		return result;
	}

	/*
	 * Helper Function: buildSimilarUsersCollection
	 * --------------------------------------------
	 * This function puts the similar users into their
	 * own collection, which will make it easier to use
	 * MongoDB's aggregation functions.
	 */
	private void buildSimilarCollection(DB db, DBCollection userColl, DBObject mainUserObject) {

		// Get the new collection and retrieve the array of favorites.
		DBCollection similarUsers = db.getCollection("similar");
		BasicBSONList arr = (BasicBSONList) mainUserObject.get("favorites");

		// This algorithm checks which users also list one of mainUser's movies among their favorites.
		for(int i = 0; i < arr.size(); i++) {

			// Find users who share common movies with the mainUser.
			List<Integer> list = new ArrayList<Integer>();
			list.add(Integer.parseInt(arr.get(i).toString()));
			BasicDBObject query = new BasicDBObject("favorites", new BasicDBObject("$in", list));
			DBCursor cur = userColl.find(query);

			// Add these similar users into the database.
			while(cur.hasNext()) {

				// This is the item we have here.
				DBObject currentObject = cur.next();

				// Make sure we don't add the main user to the collection.
				if(!(currentObject.get("_id").toString().equals(mainUserObject.get("_id").toString()))) {

					// Upsert with an increment to the score field.
					BasicDBObject increment = new BasicDBObject();
					increment.append("$inc", new BasicDBObject().append("score", 1));

					// I've set it so upsert = true and multi = false.
					similarUsers.update(currentObject, increment, true, false);

				}
			}
		}
	}

	/*
	 * Helper String: getMainUserFavorites
	 * -----------------------------------
	 * Gets the main user's favorite items and formats them.
	 */
	private String getMainUserFavorites(DBObject mainUserObject, DBCollection data,
										DBCollection users, BasicBSONList arr) {
		String message = "";
		// This puts all of the mainUser's favorite items into the message.
		for(int i = 0; i < arr.size(); i++) {
			QueryBuilder q = new QueryBuilder();
			q.put("movie_id").is(arr.get(i));
			DBCursor cur = data.find(q.get());
			DBObject movieEntry = cur.next();
			message += movieEntry.get("title") + "\n<br>";
		}

		return message;
	}

}