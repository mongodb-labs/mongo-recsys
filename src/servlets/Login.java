package servlets;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.QueryBuilder;

import static classes.Constants.*;

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
	 * Does nothing, as there is no GET method for Login.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}

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
