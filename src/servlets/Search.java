package servlets;

import static classes.Constants.databaseName;
import static classes.Constants.itemCategory;
import static classes.Constants.itemCollection;
import static classes.Constants.userIDField;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.types.BasicBSONList;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;


/**
 * Servlet implementation class Recommend
 */
@WebServlet("/Search")
public class Search extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/*
	 * Constructor: Recommend
	 * ----------------------
	 * Invokes the parent class' constructor.
	 */
	public Search() {
		super();
	}

	/*
	 * HTTP Method: doGet
	 * ------------------
	 * Used by home.jsp to redirect the user to search.jsp, passing the unique_id as well.
	 * This method also gets the genres and other information.
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
		BasicBSONList arr = (BasicBSONList) coll.distinct(itemCategory);

		// Iterate through the collection and put the names of every genre in an ArrayList.
		ArrayList<String> genres = new ArrayList<String>();
		for (int i = 0; i < arr.size(); i++) {
			genres.add(arr.get(i).toString());
		}
		
		String unique_id = request.getParameter(userIDField);
		request.setAttribute("genres", genres);
		request.setAttribute(userIDField, unique_id);
		request.getRequestDispatcher("search.jsp").forward(request, response);

	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {}
}