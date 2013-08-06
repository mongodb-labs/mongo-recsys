package servlets;

import static classes.Constants.userIDField;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class DisplayCode
 */
@WebServlet("/About")
public class About extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/*
	 * Constructor: DisplayCode
	 * ------------------------
	 * Invokes the parent class' constructor.
	 */
	public About() {
		super();
	}

	/*
	 * HTTP Method: doGet
	 * ------------------
	 * Serves the user the source code in
	 * the form of a servlet. Does minimal maintenence of the unique_id
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// Get user information from the request.
		String unique_id = request.getParameter(userIDField);

		// Quick check to see if the user is logged in.
		if (unique_id == null) {
			String message = "Please login.";
			request.setAttribute("message", message);
			request.getRequestDispatcher("login.jsp")
					.forward(request, response);
			return;
		}
		
		request.setAttribute(userIDField, unique_id);
		request.getRequestDispatcher("about.jsp").forward(request, response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

}
