package servlets;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

/**
 * Servlet implementation class ImportData
 */
@WebServlet("/ImportData")
public class ImportData extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ImportData() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("get");
	}

	/*
	 * Response: doPost
	 * ----------------
	 * Takes the POST request from the home.jsp page and responds to it.
	 * Dispatches data to data.jsp.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// Grab the MongoClient from the ServletContext.
		ServletContext context = request.getSession().getServletContext();
		
		// Grab the MongoClient and create a database.
		MongoClient m = (MongoClient) context.getAttribute("mongo");
		DB db = m.getDB("daviddb");
		
		// Bring all of the movie data into the database.
		String filename =
				"/Users/epithelialbiology/Desktop/Work/2013/10gen/Recommender/data/top_100_movies_ids.dsv";
		readIntoDatabase(filename, db);

		String message = "";
//		// Gets the appropriate collection and sets a cursor to the query.
//		DBCollection movieCollection = db.getCollection("movies");
//		DBCursor cursor = movieCollection.find();
//		
//		// Puts data into a readable format for the JSP page.
//		while (cursor.hasNext()) {
//			DBObject current = cursor.next();
//			message += current.get("title");
//			message += " - ";
//			message += current.get("movie_id");
//			message += "<br>";
//		}
		
		// Set the value of the message and dispatch to the JSP.
		request.setAttribute("message", message);
        request.getRequestDispatcher("search.jsp").forward(request, response);
		
	}
	
	/*
	 * Helper Function: readIntoDatabase
	 * ---------------------------------
	 * Reads in data from the top 100 movies file, and makes
	 * documents with the title and the corresponding movie id.
	 */
	private void readIntoDatabase(String filename, DB db) {
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			DBCollection coll = db.getCollection("movies");
			String currentLine;
			while((currentLine = br.readLine()) != null) {
				
				// Tokenizes the string over bar delimiters, getting name & ID number.
				StringTokenizer tok = new StringTokenizer(currentLine, "|");
				int movieID = Integer.parseInt(tok.nextToken());
				String movieName = tok.nextToken();
				
				// Creates a document, stores and saves the tokenized data.
				BasicDBObject document = new BasicDBObject();
				document.put("movie_id", movieID);
				document.put("title", movieName);
				coll.insert(document);
				
			}
			br.close();
		} catch(IOException ex) {
			System.out.println("An error ocurred while reading file: " + filename + " with a BufferedReader.");
			System.out.println("Please ensure that the filepath is correct.");
		}
	}

}
