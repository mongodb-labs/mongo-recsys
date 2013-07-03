<%@include file="templates/header.jsp"%>

<div class="container">

	<div class="row">
		<div class="offset-by-six">
			<div class="four columns">
				<h6>A recommendation engine built with MongoDB.</h6>
				<br>
				<form action="Recommend" method="post">
					<button name="unique_id" type="submit" value="${unique_id}">Get Recommendations</button>
				</form>
				<form action="Recommend" method="get">
					<button name="unique_id" type="submit" value="${unique_id}">Update Movies</button>
				</form>
				<p>${message}</p>
			</div>
		</div>
	</div>
	
</div>

<%@include file="templates/footer.jsp"%>