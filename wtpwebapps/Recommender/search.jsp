<%@include file="templates/header.jsp"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<body onload="activateTab()">

	<%@include file="templates/navbar-top.jsp"%>

	<div class="container main">
		<div class="row">
			<div class="span12">
				<%@include file="templates/navbar.jsp"%>
			</div>
		</div>

		<h4 class="muted">Click Movie to Remove from Favorites</h4>

		<br>

		<div>
			<h4 style="color: #C0C0C0">Find New Favorites</h4>
			<br>
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
				<button class="btn" style="margin-bottom: 11px" type="submit"
					name="unique_id" value="${unique_id}">Search!</button>
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

		<form action="UpdateMovies" method="post">

			<c:if test="${not empty results}">
				<button class="btn btn-primary" name="unique_id" type="submit"
					value="${unique_id}">Add to Favorites</button>
				<br>
				<br>
			</c:if>

			<div style="margin-left: -30px; height: 800px; overflow: auto">

				<div class="row">
					<c:forEach items="${results}" varStatus="loop">
						<div class="span6">
							<label for="${ids[loop.index]}" id="${ids[loop.index]}label"
								class="tile"
								style="margin-left: 30px; background-color: #8593ff;"> <!-- Hidden Checkbox -->
								<input type="checkbox" style="display: none" name="movie"
								value="${ids[loop.index]}" id="${ids[loop.index]}"
								onchange="toggle(this.id)">

								<div class="titlediv">
									<h2>${fn:toUpperCase(results[loop.index])}</h2>
								</div>
								
								<div class="row">

									<!--  Replace this with the actual picture. -->
									<div class="span2">
										<div class="crop">
											<img src="${images[loop.index]}">
										</div>
									</div>

									<!--  We will replace this with actual descriptions. -->
									<div class="span3">
										<div class="textblock">${plots[loop.index]}</div>
									</div>

								</div>
							</label>
						</div>
					</c:forEach>
				</div>

			</div>
		</form>
	</div>

	<script type="text/javascript">
		function activateTab() {
			var updateTab = document.getElementById("updatetab");
			updateTab.className = "active";
		}
		function toggle(id) {
			var checkbox = document.getElementById(id);
			if (checkbox.checked == true) {
				document.getElementById(id + 'label').style.backgroundColor = '#C41E3A';
				document.getElementById(id + 'label').style.borderColor = 'black';
			} else {
				document.getElementById(id + 'label').style.backgroundColor = '#779ECB';
				document.getElementById(id + 'label').style.borderColor = '#C0C0C0';
			}
		}
	</script>
</body>

<%@include file="templates/footer.jsp"%>