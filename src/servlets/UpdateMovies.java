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

/**
 * Servlet implementation class UpdateMovies
 */
@WebServlet("/UpdateMovies")
public class UpdateMovies extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	/*
	 * Constructor: UpdateMovies
	 * -------------------------
	 * Invokes the parent class' constructor.
	 */
	public UpdateMovies() {
		super();
	}

	/*
	 * Response: doGet
	 * ---------------
	 * Implements the search functionality and returns a list of candidate items.
	 * Takes in user specifications with regard to genre and/or title.
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		// Get unique user ID info from the request.
		String unique_id = request.getParameter("unique_id");
				
		// Check if the uniqueID is there, if not, redirect.
		if(unique_id == null) {
			System.out.println("No id!");
			String message = "Please login.";
			request.setAttribute("message", message);
			request.getRequestDispatcher("login.jsp").forward(request, response);
			return;
		}
		
		// Retrieve the ServletContext.
		ServletContext context = request.getSession().getServletContext();

		// Grab the MongoClient and create a database or get it if it already exists.
		MongoClient m = (MongoClient) context.getAttribute("mongo");
		DB db = m.getDB(databaseName);
		DBCollection coll = db.getCollection(itemCollection);
		
		// Do filtering based on user preferences.
		String genre = request.getParameter("genre");
		String name = request.getParameter("title");
		BasicDBObject filterByGenre = new BasicDBObject();
		BasicDBObject filterByName = new BasicDBObject();
		AggregationOutput output = null;
		
		// Search according to user preference.
		if(genre.equals("Any") && name.equals("")) { // Any genre, no title.
			// System.out.println("Any genre and no title.");
			filterByGenre.append("$match", new BasicDBObject("genre", new BasicDBObject("$exists", true)));
			output = coll.aggregate(filterByGenre);
			
		} else if(genre.equals("Any") && !name.equals("")) { // Any genre, specific title.
			// System.out.println("Any genre and specific title.");
			// System.out.println(name);
			filterByName.append("$match", new BasicDBObject("title", java.util.regex.Pattern.compile("(?i)" + name)));
			output = coll.aggregate(filterByName);
			
		} else if(!genre.equals("Any") && name.equals("")) { // Specific genre, no title.
			// System.out.println("Specific genre and no title.");
			filterByGenre.append("$match", new BasicDBObject("genre", genre));
			output = coll.aggregate(filterByGenre);
		} else if(!genre.equals("Any") && !name.equals("")) { // Specific genre, specific title.
			// System.out.println("Specific genre and specific title.");
			// System.out.println(genre);
			filterByGenre.append("$match", new BasicDBObject("genre", genre));
			filterByName.append("$match", new BasicDBObject("title", java.util.regex.Pattern.compile("(?i)" + name)));
			output = coll.aggregate(filterByGenre, filterByName);
			
		}
		
		// Set up all of our results in the checkbox form for being submitted to the .jsp
		ArrayList<String> searchResults = new ArrayList<String>();
		ArrayList<Integer> idNumbers = new ArrayList<Integer>();
		int counter = 0;
		
		// Read all of the AggregationOutput into the searchResults ArrayList.
		for (DBObject obj : output.results()) {
			if(obj == null) break;
			searchResults.add(obj.get("title").toString());
			idNumbers.add(Integer.parseInt(obj.get("movie_id").toString()));
			counter++;
		}
		
		// Quickly re-populate the list of genres.
		ArrayList<String> genres = getGenres(request);
		
		// Forward to the searching page.
		request.setAttribute("genres", genres);
		request.setAttribute("results", searchResults);
		request.setAttribute("ids", idNumbers);
		request.setAttribute("arraysize", counter);
		request.setAttribute("unique_id", unique_id);
		request.getRequestDispatcher("search.jsp").forward(request, response);
	}

	/*
	 * Response: doPost
	 * ----------------
	 * Takes the movies that the user has checked and appends them to that user's
	 * favorites array in the database.
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		// Get unique user ID info from the request.
		String unique_id = request.getParameter("unique_id");
		
		// Check for unique_id - copy/paste.
		if(unique_id == null) {
			System.out.println("No id!");
			String message = "Please login.";
			request.setAttribute("message", message);
			request.getRequestDispatcher("login.jsp").forward(request, response);
			return;
		}
		
		// Get the database and collection.
		MongoClient m = (MongoClient) request.getServletContext().getAttribute("mongo");
		DB db = m.getDB(databaseName);
		DBCollection users = db.getCollection("users");
		
		// Create a query to find the user by unique_id.
		DBObject query = new BasicDBObject("unique_id", unique_id);
		DBObject mainUser = users.findOne(query);
		
		// Get the list of the mainUser's favorite items to avoid double-adding.
		BasicBSONList favorites = (BasicBSONList) mainUser.get("favorites");
		
		// Quickly re-populate the list of genres.
		ArrayList<String> genres = getGenres(request);
		
		// Try=catch to ensure the user has not submitted an empty form.
		try {
			
			String[] addedMovies = request.getParameterValues("movie");
			
			// Add to the user's favorites array.
			for(int i = 0; i < addedMovies.length; i++) {
				BasicDBObject idObj = new BasicDBObject("favorites", Integer.parseInt(addedMovies[i]));
				if(!(favorites.contains(Integer.parseInt(addedMovies[i])))) {
					DBObject updateQuery = new BasicDBObject("$push", idObj);
					DBObject searchQuery = new BasicDBObject("unique_id", unique_id);
					users.update(searchQuery, updateQuery);
				}
			}
					
			// Forward to the searching page.
			request.setAttribute("genres", genres);
			request.setAttribute("unique_id", unique_id);
			request.setAttribute("message", "New movies successfully added to database.");
			request.getRequestDispatcher("search.jsp").forward(request, response);
			
		} catch(Exception e) {
			
			// Forward to the searching page.
			request.setAttribute("genres", genres);
			request.setAttribute("unique_id", unique_id);
			request.setAttribute("message", "");
			request.getRequestDispatcher("search.jsp").forward(request, response);
			
		}
		
		
		
	}
	
	/*
	 * Helper Function: getGenres
	 * --------------------------
	 * Re-populates the genres drop-down in search.
	 */
	private ArrayList<String> getGenres(HttpServletRequest request) {
		
		// Instantiate result String.
		ArrayList<String> genres = new ArrayList<String>();
		
		// Grab the MongoClient from the ServletContext.
				ServletContext context = request.getSession().getServletContext();

				// Grab the MongoClient, the database, and the collection.
				MongoClient m = (MongoClient) context.getAttribute("mongo");
				DB db = m.getDB(databaseName);
				DBCollection coll = db.getCollection(itemCollection);

				// Get the unique genres.
				BasicBSONList arr = (BasicBSONList) coll.distinct("genre");

				// Iterate through the collection and put the names of every user in an ArrayList.
				for (int i = 0; i < arr.size(); i++) {
					genres.add(arr.get(i).toString());
				}
		
		return genres;
	}

}
