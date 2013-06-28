<%@include file="templates/header.jsp"%>

<%@ page
	import="com.mongodb.*,java.util.*,java.io.PrintWriter,org.bson.types.ObjectId;"%>

<div class="container" align="center">
	<div class="offset-by-six">
		<div class="four columns">
			Please select the user for which you would like to generate
			recommendations then click the "Recommend" button to run the engine.
			<br> <br>
			<%
				// Grab the MongoClient from the ServletContext.
				ServletContext context = request.getSession().getServletContext();

				// Grab the MongoClient, the database, and the collection.
				MongoClient m = (MongoClient) context.getAttribute("mongo");
				DB db = m.getDB("daviddb");
				DBCollection coll = db.getCollection("users");

				// Iterate through the collection and put the names of every user in an ArrayList.
				ArrayList<String> usernames = new ArrayList<String>();
				ArrayList<ObjectId> ids = new ArrayList<ObjectId>();
				DBCursor cursor = coll.find();
				int counter = 0;
				while (cursor.hasNext()) {
					DBObject current = cursor.next();
					usernames.add((String) current.get("user"));
					ids.add((ObjectId) current.get("_id"));
					counter++;
				}
			%>
			<div align=center>
				<form method="post" action="Recommend">
					<select name="user">
						<%
							for (int i = 0; i < counter; i++) {
								out.println("<option value=" + ids.get(i).toString() + ">"
										+ usernames.get(i) + "</option>");
							}
						%>
					</select> <INPUT TYPE="submit" name="submit" />
				</form>
				<p>${message}</p>
			</div>
		</div>
	</div>
</div>

<%@include file="templates/footer.jsp"%>