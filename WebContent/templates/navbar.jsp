<ul class="nav nav-tabs">
	<li id="hometab"><a href="#" onclick="document.getElementById('b1').click(); return false;" id="home">My Favorites</a></li>
	<li id="recommendtab"><a href="#" onclick="document.getElementById('b3').click(); return false;" id="recommend">Recommendations</a></li>
</ul>

<div style="display: none">
	<form action="Login" method="post" name="home">
		<button id="b1" name="unique_id" type="submit" value="${unique_id}">
		</button>
	</form>
	<form action="GetRecommendations" method="get" name="recommend">
		<button id="b3" name="unique_id" type="submit" value="${unique_id}">
		</button>
	</form>
</div>