<div class="row">
	<form action="Login" method="post" id="navbar">
		<button name="userid" type="submit" value="${unique_id}">Home</button>
	</form>
	<form action="Recommend" method="post" id="navbar">
		<button id="sexybutton" name="unique_id" type="submit"
			value="${unique_id}">Get Recommendations</button>
	</form>
	<form action="Recommend" method="get" id="navbar">
		<button name="unique_id" type="submit" value="${unique_id}">Update
			Movies</button>
	</form>
	<form action="login.jsp">
		<button type="submit">Sign Out</button>
	</form>
</div>