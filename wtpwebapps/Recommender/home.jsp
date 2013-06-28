<%@include file="templates/header.jsp"%>

<div class="container">
	<div class="offset-by-six">
		<div class="four columns">
			<p>A simple recommendation engine built using MongoDB.</p>
			<form action="ImportData" method="post">
				<button name="subject" type="submit" value="load">Load
					Movie Database</button>
			</form>
		</div>
	</div>
</div>

<%@include file="templates/footer.jsp"%>