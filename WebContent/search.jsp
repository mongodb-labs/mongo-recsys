<%@include file="templates/header.jsp"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<body onload="activateTab()">

	<div style="padding-top:20px;">
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
			<input type="text" name="title" autofocus>
			<button class="btn btn-primary" style="margin-bottom: 11px"
				type="submit" name="unique_id" value="${unique_id}">Search!</button>
				
				<c:if test="${not empty results}">
			<button class="btn btn-primary" type="button"
				style="color: white; background-color: #FF0000; margin-bottom:11px;" onclick="document.getElementById('add').click()">Add to Favorites</button>

			</c:if>
				
				
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


	<form action="UpdateMovies" method="post">

			<button class="btn btn-primary"
				style="display: none" name="unique_id"
				type="submit" value="${unique_id}" id="add">Add to Favorites</button>

		<div style="height: 400px; overflow: auto">

			<div class="row">
			
			<div class="span12">

				<ul class="media-list movie-list clearfix">
				
					<c:forEach items="${results}" varStatus="loop">
					
					
						<li class="media movie" id="iframe-movie"
						onclick="toggle('${ids[loop.index]}'); toggleClass(this)">
							
							<input
								type="checkbox" style="display: none" name="movie"
								value="${ids[loop.index]}" id="${ids[loop.index]}"
								onchange="toggle(this.id)">

								<a class="pull-left movie-poster" href="#"
								onclick="return false;"> <img class="media-object"
									src="${images[loop.index]}">
								</a>

								<div class="media-body">
									<h4 class="media-heading">${fn:toUpperCase(results[loop.index])}</h4>
									<p>${plots[loop.index]}</p>
								</div>
																
						</li>
								
								
								
								
					</c:forEach>

				</ul>
			</div>
			
			</div>

		</div>
	</form>
	</div>
	
		</div>
	

	<script type="text/javascript">
		function activateTab() {
			var updateTab = document.getElementById("updatetab");
			updateTab.className = "active";
		}
		function toggle(id) {
			var checkbox = document.getElementById(id);
			checkbox.checked = !checkbox.checked;
		}
		function toggleClass(elem) {
			if(elem.className == 'media movie') {
				elem.className = 'media movie selected';
			} else {
				elem.className = 'media movie';
			}
		}
	</script>
</body>

<%@include file="templates/footer.jsp"%>