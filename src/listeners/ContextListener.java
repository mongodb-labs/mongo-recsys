package listeners;

import java.net.UnknownHostException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.mongodb.MongoClient;

/**
 * Application Lifecycle Listener implementation class ContextListener
 * 
 */
@WebListener
public class ContextListener implements ServletContextListener {

	/*
	 * Class variables.
	 */
	private MongoClient mongo;

	/*
	 * Constructor: ContextListener
	 * ----------------------------
	 * No setup required for the listener, so this method does nothing.
	 */
	public ContextListener() {}

	/*
	 * Function: contextInitialized
	 * ----------------------------
	 * Sets up the connection to MongoDB, puts in in the ServletContext.
	 */
	public void contextInitialized(ServletContextEvent arg0) {

		// Connects to database in try-catch block, sets context.
		try {

			// Initializing MongoDB.
			mongo = new MongoClient("localhost", 27017);

			// Get the context so we can add the database to it.
			arg0.getServletContext().setAttribute("mongo", mongo);

		} catch (UnknownHostException e) {
			
			// Throw an error if a connection issue arises (eg: mongod not running).
			System.err.println("Connection to database failed: " + e);
			
		}

	}

	/*
	 * Function: contextDestroyed
	 * --------------------------
	 * Closes the MongoClient.
	 */
	public void contextDestroyed(ServletContextEvent arg0) {
		
		mongo.close();
		
	}

}
