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
					<button name="unique_id" type="submit" value="${unique_id}">Get
						Recommendations</button>
				</form>
				Search by genre.
				
				<!-- Making the values persist on submit. -->
				<script type="text/javascript">
					function cache() {
						var selectBox = document.getElementById("selectBox");
						var selectedText = selectBox.options[selectBox.selectedIndex].text;
						if(typeof window.localStorage != 'undefined') {
							localStorage.setItem("last", selectedText);
						}
					}
				</script>
				
				<form method="get" action="UpdateMovies" name="searchbar">
					<select name="genre" id="selectBox" onchange="cache();">
						<option>Any</option>
						<c:forEach var="genre" items="${genres}">
							<option>${genre}</option>
						</c:forEach>
					</select> Search by Title<input type="text" name="title" autofocus>
					<button type="submit" name="unique_id" value="${unique_id}">Search!</button>
				</form>
				
				<!-- If the user had previously selected a value, select that. -->
				<script type="text/javascript">
					if(typeof window.localStorage != 'undefined') {
						var lastSelection = localStorage.getItem("last");
						if(lastSelection != null) {
							var text = lastSelection, sel = document.getElementById('selectBox');
							for(var i, j = 0; i = sel.options[j]; j++) {
						        if(i.value == text) {
						            sel.selectedIndex = j;
						            break;
						        }
						    }
						}
					}
				</script>

			</div>

			${message}

		</div>

	</div>

	<div class="offset-by-three">
		<div class="ten columns" align="center">
			<form action="UpdateMovies" method="post">
				<c:forEach items="${results}" varStatus="loop">
					<div id="moviebox">
						${results[loop.index]}<br> <input type="checkbox"
							name="movie" value="${ids[loop.index]}"><br> <br>
					</div>
					<br>
				</c:forEach>
				<c:if test="${not empty results}">
					<button name="unique_id" type="submit" value="${unique_id}">Add
						Items.</button>
				</c:if>
			</form>
		</div>
	</div>

</div>

<%@include file="templates/footer.jsp"%>