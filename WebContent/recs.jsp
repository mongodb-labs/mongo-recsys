<%@include file="templates/header.jsp"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<body onload="activateTab()">

	<div align="left">

		<div class="row" align="left">
			<h1>Recommender</h1>
		</div>

		<%@include file="templates/navbar.jsp"%>

		<h2>Films Recommended for You</h2>
		<c:forEach var="film" items="${films}">
			<p id="15px">${film}</p>
		</c:forEach>

	</div>

	<script type="text/javascript">
		function activateTab() {
			var recTab = document.getElementById("recommend");
			recTab.className = "active";
		}
	</script>

	<%@include file="templates/footer.jsp"%>