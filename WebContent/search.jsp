<%@include file="templates/header.jsp"%>

<%@ page
	import="com.mongodb.*,java.util.*,java.io.PrintWriter,org.bson.types.BasicBSONList,
	org.bson.types.ObjectId,static classes.Constants.*;"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="container" align="center">

	<div class="offset-by-six">
		<div class="four columns">
			<h6>A recommendation engine built with MongoDB.</h6>
			<br>

			<div>

				<form action="Recommend" method="post">
					<button name="unique_id" type="submit" value="${unique_id}">Get Recommendations</button>
				</form>
				Search by genre.
				<form method="get" action="UpdateMovies">
					<select name="genre">
						<c:forEach var="genre" items="${genres}">
							<option>${genre}</option>
						</c:forEach>
					</select> Search by Title<input type="text" name="title">
					<button type="submit" name="unique_id" value="${unique_id}">Search!</button>
				</form>
				<form action="UpdateMovies" method="post">
					<c:forEach items="${results}" varStatus="loop">
						<input type="checkbox" name="movie" value="${ids[loop.index]}">${results[loop.index]}<br>
					</c:forEach>
					<c:if test="${not empty results}">
						<button name="unique_id" type="submit" value="${unique_id}">Go!</button>
					</c:if>
				</form>

			</div>

			${message}

		</div>

	</div>

</div>

<%@include file="templates/footer.jsp"%>