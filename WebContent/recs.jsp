<%@include file="templates/header.jsp"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<body onload="activateTab()">

	<div class="container">

		<div class="row">
			<div class="span4">
				<h1>Recommender</h1>
			</div>
		</div>

		<br>

		<%@include file="templates/navbar.jsp"%>

		<h4 style="color:#C0C0C0">Films Recommended for You</h4>
		
		<br>
		
		<c:forEach var="film" items="${films}">
			<p>${film}</p>
		</c:forEach>

	</div>

	<script type="text/javascript">
		function activateTab() {
			var recTab = document.getElementById("recommendtab");
			recTab.className = "active";
		}
	</script>

	<%@include file="templates/footer.jsp"%>