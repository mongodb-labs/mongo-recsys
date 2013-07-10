package servlets;

import static classes.Constants.databaseName;
import static classes.Constants.itemCollection;

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
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	/*
	 * Class Variables
	 */
	private String failedLogin = "Login failed.";
       
    /*
     * Constructor: Login
     * ------------------
     * Calls the parent's constructor.
     */
    public Login() {
        super();
    }

	/*
	 * HTTP Method: doGet
	 * ------------------
	 * Solely serves to create and format the movie string for the user's homepage.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// Get user information from the request.
//		 String unique_id = request.getParameter("unique_id");
		
		String unique_id = "dkhavari";
		
		// Quick check to see if the user is logged in.
//		if (unique_id == null) {
//			String message = "Please login.";
//			request.setAttribute("message", message);
//			request.getRequestDispatcher("login.jsp")
//					.forward(request, response);
//			return;
//		}
		
		// Grab the ServletContext.
		ServletContext context = request.getSession().getServletContext();

		// Grab the MongoClient and create a database or get it, if it exists.
		MongoClient m = (MongoClient) context.getAttribute("mongo");
		DB db = m.getDB(databaseName);
		
		// Retrieve the appropriate user object from the collection.
		DBCollection coll = db.getCollection("users");
		QueryBuilder mainUserQuery = new QueryBuilder();
		mainUserQuery.put("unique_id").is(unique_id);
		DBObject mainUser = coll.findOne(mainUserQuery.get());

		// Get the itemCollection from the db.
		DBCollection items = db.getCollection(itemCollection);

		// Get the user's list of favorite items - used in aggregation.
		BasicBSONList mainFavorites = (BasicBSONList) mainUser.get("favorites");

		System.out.println("Main Favorites: " + mainFavorites);

		// Create the matching command.
		BasicDBObject match = new BasicDBObject("$match", new BasicDBObject(
				"movie_id", new BasicDBObject("$in", mainFavorites)));

		// Now group these items together by genre.
		BasicDBObject groupFields = new BasicDBObject("_id", "$genre");
		groupFields.put("movie_id", new BasicDBObject("$push", "$movie_id"));
		BasicDBObject group = new BasicDBObject("$group", groupFields);

		// Run the aggregation.
		AggregationOutput output = items.aggregate(match, group);
		
		// Declare ArrayLists to store information on mainUser's movies.
		ArrayList<String> titlesByGenre = new ArrayList<String>();
		ArrayList<Integer> idNumbers = new ArrayList<Integer>();
		
		for(DBObject obj : output.results()) {
			String genre = obj.get("_id").toString();
			System.out.println(genre);
			/*
			 * Add the genre, but also add a -1 sentinel.
			 * This sentinel can be used in the .jsp to ensure
			 * that we don't allow users to treat genres as movies.
			 */
			titlesByGenre.add(genre);
			idNumbers.add(-1);
			
			BasicBSONList movieids = (BasicBSONList) obj.get("movie_id");
			// Put the data into their respective ArrayLists.
			for(int i = 0; i < movieids.size(); i++) {
				int id = Integer.parseInt(movieids.get(i).toString());
				idNumbers.add(id);
				DBObject current = items.findOne(new BasicDBObject("movie_id", id));
				titlesByGenre.add(current.get("title").toString());
			}
		}
		
		// Testing.
		System.out.println("Here is the title list: " + titlesByGenre);
		System.out.println(idNumbers);

		// Finish up by dispatching the request.
		request.setAttribute("unique_id", unique_id);
		request.setAttribute("titles", titlesByGenre);
		request.setAttribute("ids", idNumbers);
		request.getRequestDispatcher("home.jsp").forward(request, response);
		
	}

	/*
	 * HTTP Method: doPost
	 * -------------------
	 * Checks that the user has a valid unique_id and responds accordingly.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// Grab the ServletContext.
		ServletContext context = request.getSession().getServletContext();

		// Grab the MongoClient and create a database or get it if it already exists.
		MongoClient m = (MongoClient) context.getAttribute("mongo");
		DB db = m.getDB(databaseName);
		DBCollection coll = db.getCollection("users");
		
		// Get uniqueID from the request.
		String uniqueID = request.getParameter("userid");
		
		// There's very little security in this program - it just checks the user's uniqueID.
		QueryBuilder mainUserQuery = new QueryBuilder();
		mainUserQuery.put("unique_id").is(uniqueID);
		DBObject check = coll.findOne(mainUserQuery.get());
		
		// Ensure that the user's unique_id corresponds to a user in the db.
		if(check == null) {
			request.setAttribute("message", failedLogin);
			request.getRequestDispatcher("login.jsp").forward(request, response);
			return;
		}
		
		// Successfully login, give the user back their unique_id so they stay logged in.
		request.setAttribute("unique_id", uniqueID);
		request.getRequestDispatcher("home.jsp").forward(request, response);
		
	}

}
