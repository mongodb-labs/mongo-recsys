package servlets;

import static classes.Constants.*;
import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.bson.types.BasicBSONList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.QueryBuilder;

/**
 * Servlet implementation class CreateUser
 */
@WebServlet("/CreateUser")
public class CreateUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	/*
	 * Class Variables
	 */
	private String creationSuccessful = "Success! Please use your new ID to login.";
       
    /*
     * Constructor: CreateUser
     * -----------------------
     * Just calls the parent constructor.
     */
    public CreateUser() {
        super();
    }

	/*
	 * HTTP Method: doGet
	 * ------------------
	 * There is no GET method for CreateUser.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}

	/*
	 * HTTP Method: doPost
	 * -------------------
	 * Receives the user's request to either login or create a new user and handles it accordingly.
	 * No users with duplicate unique_id attributes are permitted.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// Grab the ServletContext.
		ServletContext context = request.getSession().getServletContext();

		// Grab the MongoClient and create a database or get it if it already exists.
		MongoClient m = (MongoClient) context.getAttribute(mongoClient);
		DB db = m.getDB(databaseName);
		DBCollection coll = db.getCollection(userCollection);
		
		// Get unique user ID info from the request.
		String uniqueID = request.getParameter("unique_id");
		String fullName = request.getParameter("realname");
	
		// Check to see if the user's unique ID already exists. If not, create the new user.
		QueryBuilder mainUserQuery = new QueryBuilder();
		mainUserQuery.put(userIDField).is(uniqueID);
		DBObject check = coll.findOne(mainUserQuery.get());
		if(check != null) {
			request.setAttribute("message", "This unique user ID is already taken. Please choose another.");
			request.getRequestDispatcher("login.jsp").forward(request, response);
			return;
		}
		
		// Create the user and insert into the collection.
		BasicDBObject newUser = new BasicDBObject();
		newUser.put(userIDField, uniqueID);
		newUser.put("user", fullName);
		newUser.put(userPrefs, new BasicBSONList());
		coll.insert(newUser);
		
		// Send back a message notifying the user that creation was successful.
		request.setAttribute("message", creationSuccessful);
		request.getRequestDispatcher("login.jsp").forward(request, response);
		
	}

}
