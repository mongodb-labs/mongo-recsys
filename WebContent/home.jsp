<%@include file="templates/header.jsp"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<body onload="loadScroll(); activateTab()" onunload="saveScroll()">

	<div class="container">

		<div class="row">
			<div class="span4">
				<h1>Recommender</h1>
			</div>
		</div>

		<br>

		<%@include file="templates/navbar.jsp"%>

		<h4 style="color:#C0C0C0">Click Movie to Remove from Favorites</h4>
		
		<br>

		<div class="text-left">
			<!--  This part here prints all of the user's preferences. -->
			<c:forEach items="${titles}" varStatus="loop">
				<div>
					<c:choose>
						<c:when test="${ids[loop.index] == -1}">
							<h5>${titles[loop.index]}</h5>
						</c:when>
						<c:when test="${ids[loop.index] != -1}">
							<input type="checkbox" name="movie" value="${ids[loop.index]}">
							${titles[loop.index]}
						</c:when>
					</c:choose>
				</div>
				<br>
			</c:forEach>

			${message}

		</div>

		<!-- Hidden form to allow us to grab the user's item prefs. -->
		<form id="grabItems" action="Login" method="get" hidden=true>
			<input name="unique_id" value="${unique_id}">
		</form>

		<!--  Another hidden form to allow for seamless removal/refresh of movies -->
		<form name="removeRefresh" action="RemoveItem" method="post"
			hidden="true">
			<input id="removethis" name="remove" value=""> <input
				name="unique_id" value="${unique_id}">
		</form>

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

<!--  JQuery deletions in response to box-checking. -->
<script type="text/javascript"
	src="http://code.jquery.com/jquery-latest.min.js"></script>
<script>
	$('input:checkbox').change(function() {
		if ($(this).is(':checked')) {
			var tempScrollTop = $(window).scrollTop();
			var value = $(this).val();
			var input = $('#removethis');
			input.val(value); // Set the form value.
			// Submit the form.
			$('form[name="removeRefresh"]').submit();
			$(window).scrollTop(tempScrollTop);
		}
	});

	function activateTab() {
		var homeTab = document.getElementById("hometab");
		homeTab.className = "active";
	}
</script>

<%@include file="templates/scroll.jsp"%>
<%@include file="templates/footer.jsp"%>