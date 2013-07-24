<ul class="nav nav-tabs">
	<li><a href="#" onclick="document.getElementById('b1').click(); return false;" id="home">My Favorites</a></li>
	<li><a href="#" onclick="document.getElementById('b2').click(); return false;" id="update">Update Movies</a></li>
	<li><a href="#" onclick="document.getElementById('b3').click(); return false;" id="recommend">Recommendations</a></li>
	<li><a href="login.jsp">Log Out</a></li>
</ul>

<div style="display: none">
	<form action="Login" method="post" name="home">
		<button id="b1" name="userid" type="submit" value="${unique_id}">
		</button>
	</form>
	<form action="Recommend" method="get" name="update">
		<button id="b2" name="unique_id" type="submit" value="${unique_id}">
		</button>
	</form>
	<form action="Recommend" method="post">
		<button id="b3" name="unique_id" type="submit" value="${unique_id}"></button>
	</form>
</div>