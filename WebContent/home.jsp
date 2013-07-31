<%@ include file="templates/header.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<body onload="loadScroll(); activateTab()" onunload="saveScroll()">

	<%@include file="templates/navbar-top.jsp"%>

	<div class="container main">
		<div class="row">
			<div class="span12">
				<%@include file="templates/navbar.jsp"%>
			</div>
		</div>
		<div class="row">
			<div class="span12">
				<a class="btn btn-primary" id="addMovies">Add Favorites</a>
			</div>
		</div>

		<h4 class="muted">Click Movie to Remove from Favorites</h4>

		<br>

		<div class="row">

			<div class="span12">

				<ul class="media-list movie-list clearfix">

					<!--  This part here prints all of the user's preferences. -->
					<c:forEach items="${titles}" varStatus="loop">
						<div>
							<c:choose>
								<c:when test="${ids[loop.index] != -1}">

									<li class="media movie" id="${ids[loop.index]}label" onclick="toggle('${ids[loop.index]}'); deleteMovie('${ids[loop.index]}')">

											<a class="pull-left movie-poster" href="#"
											onclick="return false;"> <img class="media-object"
											src="${images[loop.index]}">
											</a>
											
											<div class="media-body">
												<h4 class="media-heading">${titles[loop.index]}</h4>
												<p>${plots[loop.index]}</p>
											</div>
											
											<input style="display: none" type="checkbox"
											id="${ids[loop.index]}" name="movie"
											value="${ids[loop.index]}">
									</li>
									
								</c:when>
							</c:choose>
						</div>
					</c:forEach>

				</ul>

				${message}

			</div>

			<!-- Hidden form to allow us to grab the user's item prefs. -->
			<form id="grabItems" action="Login" method="get" hidden=true>
				<input name="unique_id" value="${unique_id}">
			</form>

			<!--  Another hidden form to allow for seamless removal/refresh of movies -->
			<form name="removeRefresh" action="RemoveItem" method="post" hidden="true">
				<input id="removethis" name="remove" value=""> <input
					name="unique_id" value="${unique_id}">
			</form>

		</div>

	</div>

	<!--  In this modal we're using an iframe to allow for search functionality. -->
	<div class="modal hide fade" id="addMoviesModal">
		<div class="modal-header">
			<button type="button" class="close"
				onclick="window.setInterval('location.reload(true)', 400);"
				data-dismiss="modal" aria-hidden="true">&times;</button>
			<h3>Add Favorite Movies</h3>
		</div>
		<iframe id="addMoviesFrame" seamless="seamless"
			src="Search?unique_id=${unique_id}"></iframe>
		<div class="modal-footer">
			<a href="#" class="btn"
				onclick="window.setInterval('location.reload(true)', 400);"
				data-dismiss="modal">Cancel</a>
		</div>
	</div>

</body>

<!--  AJAX call to get the user's item preferences. -->
<c:if test="${titles == null}">
	<script type="text/javascript">
		(function() {
			var getInfo = document.getElementById("grabItems");
			getInfo.submit();
		})();
	</script>
</c:if>

<!-- Javascript for Box-Checking -->
<script type="text/javascript">
		function toggle(id) {
			var checkbox = document.getElementById(id);
			checkbox.checked = !checkbox.checked;
		}
</script>

<!--  JQuery deletions in response to box-checking. -->
<script>

	function deleteMovie(id) {
		var form = document.forms['removeRefresh'];
		var input = $('#removethis');
		input.val(id);
		form.submit();
	}
	
	$(document).ready(function() {
		$('input:checkbox').change(function() {
			if ($(this).is(':checked')) {
				var tempScrollTop = $(window).scrollTop();
				var value = $(this).val();
				var input = $('#removethis');
				input.val(value);
				$('form[name="removeRefresh"]').submit();
				$(window).scrollTop(tempScrollTop);
			}
		});

		$('#addMovies').click(function() {
			$('#addMoviesModal').modal('show');
		});
	});
	function activateTab() {
		var homeTab = document.getElementById("hometab");
		homeTab.className = "active";
	}
</script>

<%@include file="templates/scroll.jsp"%>
<%@include file="templates/footer.jsp"%>
