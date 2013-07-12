<%@include file="templates/header.jsp"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<body>

	<div align="left">

		<div class="row" align="left">
			<h1>Recommender</h1>
		</div>

		<%@include file="templates/navbar.jsp"%>

		<h2>Films Recommended for You</h2>
		<c:forEach var="film" items="${films}">
			${film}<br>
			<br>
		</c:forEach>

	</div>

	<%@include file="templates/footer.jsp"%>