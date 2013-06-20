package servlets;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.types.BasicBSONList;
import org.bson.types.ObjectId;

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
		DB db = m.getDB("daviddb");
		
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
		
		// Prints the username and ID number. For testing purposes.
		System.out.println(mainUserObject.get("_id"));
		System.out.println(mainUserObject.get("user"));
		
		// Grab the user's array from the user object.
		BasicBSONList arr = (BasicBSONList) mainUserObject.get("favorites");
		
		// Dispatch the movies that the user likes.
		String message = createRecommendation(db, mainUserObject, arr);
		
		// Set the value of the message and dispatch to the JSP.
		request.setAttribute("message", message);
		request.getRequestDispatcher("search.jsp").forward(request, response);
	}
	
	/*
	 * String: createMessage
	 * ---------------------
	 * Creates the message that is dispatched to search.jsp in the doPost function.
	 */
	private String createRecommendation(DB db, DBObject mainUserObject, BasicBSONList arr) {
		String message = "Favorites list for user [" + mainUserObject.get("user") + "]:<br>";
		DBCollection dataColl = db.getCollection("movies");
		
		// This prints all of the mainUser's favorite items.
		for(int i = 0; i < arr.size(); i++) {
			QueryBuilder q = new QueryBuilder();
			q.put("movie_id").is(arr.get(i));
			DBCursor cur = dataColl.find(q.get());
			DBObject movieEntry = cur.next();
			message += movieEntry.get("title") + "<br>";
		}
		
		// I've created the algorithm structure, simply need to implement it and return the message.
		
		
		
		
		
		return message;
	}

}
