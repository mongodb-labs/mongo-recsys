<%@include file="templates/header.jsp" %>

	<p>A simple recommendation engine built using MongoDB.</p>
	<form action="ImportData" method="post">
  		<button name="subject" type="submit" value="load">Load Movie Database</button>
	</form>

<%@include file="templates/footer.jsp" %>