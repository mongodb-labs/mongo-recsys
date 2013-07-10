<%@include file="templates/header.jsp"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="row" align="left">

	<div class="row" align="left">
		<h1>Recommender</h1>
	</div>

	<div class="four columns">
		<p id="smallheader">Your Movies</p>
		<form action="Recommend" method="post">
			<button name="unique_id" type="submit" value="${unique_id}">Get
				Recommendations</button>
		</form>
		<form action="Recommend" method="get">
			<button name="unique_id" type="submit" value="${unique_id}">Update
				Movies</button>
		</form>
	</div>


	<!--  This part here is in progress -->
	<c:forEach items="${titles}" varStatus="loop">
		<div>
			<c:choose>
				<c:when test="${ids[loop.index] == -1}">
					<h5>${titles[loop.index]}</h5>
				</c:when>
				<c:when test="${ids[loop.index] != -1}">
			${titles[loop.index]}
					<input type="checkbox" name="movie" value="${ids[loop.index]}">
				</c:when>
			</c:choose>
		</div>
		<br>
	</c:forEach>

	<c:if test="${not empty message}">
		<div class="offset-by-three">
			<div class="ten columns">
				<div>${message}</div>
			</div>
		</div>
	</c:if>

</div>

<%@include file="templates/footer.jsp"%>