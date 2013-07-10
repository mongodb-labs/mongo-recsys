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

	<c:if test="${not empty message}">
		<div class="offset-by-three">
			<div class="ten columns">
				<div id="moviebox">${message}</div>
			</div>
		</div>
	</c:if>

</div>

<%@include file="templates/footer.jsp"%>