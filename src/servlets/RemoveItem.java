package servlets;

import static classes.Constants.*;
import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

/**
 * Servlet implementation class RemoveItem
 */
@WebServlet("/RemoveItem")
public class RemoveItem extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /*
     * Constructor: RemoveItem
     * -----------------------
     * Does nothing other than invoke the parent class' constructor.
     */
    public RemoveItem() {
        super();
    }

	/*
	 * HTTP Method: doGet
	 * ------------------
	 * This method does nothing, and is not called.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}

	/*
	 * HTTP Method: doPost
	 * -------------------
	 * Receives an item to be deleted from the user and deletes it.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
				
		// Get user information from the request.
		String unique_id = request.getParameter(userIDField);
		String removeThis = request.getParameter("remove");
		
		// Quick check to see if the user is logged in.
		if (unique_id == null) {
			String message = "Please login.";
			request.setAttribute("message", message);
			request.getRequestDispatcher("login.jsp")
					.forward(request, response);
			return;
		}

		// Grab the ServletContext.
		ServletContext context = request.getSession().getServletContext();

		// Grab the MongoClient and create a database or get it, if it exists.
		MongoClient m = (MongoClient) context.getAttribute(mongoClient);
		DB db = m.getDB(databaseName);

		// Retrieve the appropriate user object from the collection.
		DBCollection coll = db.getCollection(userCollection);
		DBObject mainUserQuery = new BasicDBObject();
		mainUserQuery.put(userIDField, unique_id);
		
		// Update the user collection by pulling the movie_id from the favorites list.
		coll.update(mainUserQuery, new BasicDBObject("$pull",
				new BasicDBObject(userPrefs, Integer.parseInt(removeThis))));
		
		// Return everything to the home page as necessary.
		request.setAttribute("unique_id", unique_id);
		request.getRequestDispatcher("home.jsp").forward(request, response);
	}

}
