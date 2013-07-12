package servlets;

import static classes.Constants.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.bson.types.BasicBSONList;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

/**
 * Servlet implementation class UpdateMovies
 */
@WebServlet("/UpdateMovies")
public class AddItems extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	/*
	 * Constructor: UpdateMovies
	 * -------------------------
	 * Invokes the parent class' constructor.
	 */
	public AddItems() {
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
		String unique_id = request.getParameter(userIDField);
				
		// Check if the uniqueID is there, if not, redirect.
		if(unique_id == null) {
			String message = "Please login.";
			request.setAttribute("message", message);
			request.getRequestDispatcher("login.jsp").forward(request, response);
			return;
		}
		
		// Retrieve the ServletContext.
		ServletContext context = request.getSession().getServletContext();

		// Grab the MongoClient and create a database or get it if it already exists.
		MongoClient m = (MongoClient) context.getAttribute(mongoClient);
		DB db = m.getDB(databaseName);
		DBCollection coll = db.getCollection(itemCollection);
		
		// Do filtering based on user preferences.
		String genre = request.getParameter(itemCategory);
		String name = request.getParameter(itemName);
		BasicDBObject filterByGenre = new BasicDBObject();
		AggregationOutput output = null;
		CommandResult result = null;
		List<DBObject> objects = new ArrayList<DBObject>();
		
		// Search according to user preference.
		if(genre.equals("Any") && name.equals("")) { // Any genre, no title.
			filterByGenre.append("$match", new BasicDBObject(itemCategory, new BasicDBObject("$exists", true)));
			output = coll.aggregate(filterByGenre);
			
		} else if(genre.equals("Any") && !name.equals("")) { // Any genre, specific title.
			// Do the full text search on the collection.
			final DBObject textSearchCommand = new BasicDBObject();
		    textSearchCommand.put("text", itemCollection);
		    textSearchCommand.put("search", name);
		    result = db.command(textSearchCommand);   
		    DBObject objectResult = (DBObject) result;
		    @SuppressWarnings("unchecked")
		    List<DBObject> list = (List<DBObject>) objectResult.get("results");
		    objects = list;
						
		} else if(!genre.equals("Any") && name.equals("")) { // Specific genre, no title.
			filterByGenre.append("$match", new BasicDBObject(itemCategory, genre));
			output = coll.aggregate(filterByGenre);
		
		} else if(!genre.equals("Any") && !name.equals("")) { // Specific genre, specific title.
			filterByGenre.append(itemCategory, genre);
			final DBObject textSearchCommand = new BasicDBObject();
		    textSearchCommand.put("text", itemCollection);
		    textSearchCommand.put("search", name);
		    textSearchCommand.put("filter", filterByGenre);
		    result = db.command(textSearchCommand);   
		    DBObject objectResult = (DBObject) result;
		    @SuppressWarnings("unchecked")
		    List<DBObject> list = (List<DBObject>) objectResult.get("results");
		    objects = list;
			
		}
		
		// Set up all of our results in the checkbox form for being submitted to the .jsp
		ArrayList<String> searchResults = new ArrayList<String>();
		ArrayList<Integer> idNumbers = new ArrayList<Integer>();
		int counter = 0;
				
		// Now we need to iterate over this collection.
		for(int i = 0; i < objects.size(); i++) {
			DBObject obj = (DBObject) objects.get(i).get("obj");
			
			// Add to the arrays.
			searchResults.add(obj.get(itemName).toString());
			idNumbers.add(Integer.parseInt(obj.get(itemIDField).toString()));
			counter++;
		}
		
		// Read all of the AggregationOutput into the searchResults ArrayList.
		if(output != null) {
			for (DBObject obj : output.results()) {
				if(obj == null) break;
				searchResults.add(obj.get(itemName).toString());
				idNumbers.add(Integer.parseInt(obj.get(itemIDField).toString()));
				counter++;
			}
		}

		// Quickly re-populate the list of genres.
		ArrayList<String> genres = getGenres(request);
		
		// Forward to the searching page.
		request.setAttribute("genres", genres);
		request.setAttribute("results", searchResults);
		request.setAttribute("ids", idNumbers);
		request.setAttribute("arraysize", counter);
		request.setAttribute(userIDField, unique_id);
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
		String unique_id = request.getParameter(userIDField);
		
		// Check for unique_id - copy/paste.
		if(unique_id == null) {
			String message = "Please login.";
			request.setAttribute("message", message);
			request.getRequestDispatcher("login.jsp").forward(request, response);
			return;
		}
		
		// Get the database and collection.
		MongoClient m = (MongoClient) request.getServletContext().getAttribute(mongoClient);
		DB db = m.getDB(databaseName);
		DBCollection users = db.getCollection(userCollection);
		
		// Create a query to find the user by unique_id.
		DBObject query = new BasicDBObject(userIDField, unique_id);
		DBObject mainUser = users.findOne(query);
		
		// Get the list of the mainUser's favorite items to avoid double-adding.
		BasicBSONList favorites = (BasicBSONList) mainUser.get(userPrefs);
		
		// Quickly re-populate the list of genres.
		ArrayList<String> genres = getGenres(request);
		
		// Try=catch to ensure the user has not submitted an empty form.
		try {
			
			String[] addedMovies = request.getParameterValues("movie");
			
			int flag = 0;
			
			// Add to the user's favorites array.
			for(int i = 0; i < addedMovies.length; i++) {
				BasicDBObject idObj = new BasicDBObject(userPrefs, Integer.parseInt(addedMovies[i]));
				if(!(favorites.contains(Integer.parseInt(addedMovies[i])))) {
					DBObject updateQuery = new BasicDBObject("$push", idObj);
					DBObject searchQuery = new BasicDBObject(userIDField, unique_id);
					users.update(searchQuery, updateQuery);
				} else {
					flag = 1;
				}
			}
					
			// Forward to the searching page.
			request.setAttribute("genres", genres);
			request.setAttribute(userIDField, unique_id);
			if(flag==0) {
				request.setAttribute("message", "New movies successfully added to database.");
			} else {
				request.setAttribute("message", "One or more movies was already present in your collection," +
						" but requested movies not already in your favorites list have been added.");
			}
			request.getRequestDispatcher("search.jsp").forward(request, response);
			
		} catch(Exception e) {
			
			// Forward to the searching page.
			request.setAttribute("genres", genres);
			request.setAttribute(userIDField, unique_id);
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
		MongoClient m = (MongoClient) context.getAttribute(mongoClient);
		DB db = m.getDB(databaseName);
		DBCollection coll = db.getCollection(itemCollection);

		// Get the unique genres.
		BasicBSONList arr = (BasicBSONList) coll.distinct(itemCategory);

		// Iterate through the collection and put the names of every user in an
		// ArrayList.
		for (int i = 0; i < arr.size(); i++) {
			genres.add(arr.get(i).toString());
		}
		
		return genres;
	}

}
