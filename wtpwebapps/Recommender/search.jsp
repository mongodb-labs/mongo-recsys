<%@include file="templates/header.jsp"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div align="left">

	<div class="row" align="left">
		<h1>Recommender</h1>
	</div>

	<%@include file="templates/navbar.jsp"%>

	<div>
		<h2>Find New Favorites</h2>
		<h5>Search by Genre</h5>
		<br>
		<!-- Making the values persist on submit. -->
		<script type="text/javascript">
			function cache() {
				var selectBox = document.getElementById("selectBox");
				var selectedText = selectBox.options[selectBox.selectedIndex].text;
				if (typeof window.localStorage != 'undefined') {
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
			</select>
			<h5>Search by Title</h5>
			<br> <input type="text" name="title" autofocus>
			<button type="submit" name="unique_id" value="${unique_id}">Search!</button>
		</form>

		<!-- If the user had previously selected a value, select that. -->
		<script type="text/javascript">
			if (typeof window.localStorage != 'undefined') {
				var lastSelection = localStorage.getItem("last");
				if (lastSelection != null) {
					var text = lastSelection, sel = document
							.getElementById('selectBox');
					for ( var i, j = 0; i = sel.options[j]; j++) {
						if (i.value == text) {
							sel.selectedIndex = j;
							break;
						}
					}
				}
			}
		</script>

		${message}

	</div>

	<div align="left">
		<form action="UpdateMovies" method="post">

			<c:if test="${not empty results}">
				<button name="unique_id" type="submit" value="${unique_id}"
					id="floatingbutton">
					<p id="buttontext">+</p>
				</button>
			</c:if>

			<c:forEach items="${results}" varStatus="loop">
				<div>
					<input type="checkbox" name="movie" value="${ids[loop.index]}">${results[loop.index]}<br>
				</div>
				<br>
			</c:forEach>

		</form>
	</div>

</div>

<%@include file="templates/footer.jsp"%>