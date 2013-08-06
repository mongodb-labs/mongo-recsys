<%@ include file="templates/header.jsp"%>

<body onload="activateTab()">

	<%@include file="templates/navbar-top.jsp"%>

	<div class="container main">
		<div class="row">
			<div class="span12">
				<%@include file="templates/navbar.jsp"%>
			</div>
		</div>

		<h4 class="muted">About Recommender</h4>

		<br>

		<div class="row">
			<div class="span7">
				<h4>Recommender Framework</h4>
				<br><img src="http://oi42.tinypic.com/2qb4n7m.jpg">
				<p style="margin-top: 50px; margin-bottom: 50px">The overall flow of the application follows
				a standard client-server model in which the server acts as an intermediary between the browser
				and MongoDB. Users sign in using their unique usernames, and interact with
				the server mostly via HTML GET methods. The reason for this is that we maintain a simple
				URL-packing system with which we keep track of the user's unique_id. Security on this
				application is minimal because we are trying to keep Recommender as simple as possible, and URL-packing
				is an easy way to ensure basic sanity checks while maintaining speed and high concurrency.</p>
			</div>
			<br><br>
			<div class="span7">
				<img src="http://oi41.tinypic.com/95x8bl.jpg">
				<p style="margin-top: 50px; margin-bottom: 50px">
				The aggregation pipeline is the heart of this application. The aggregation
				algorithm we use has several steps, but is quite simple. In essence, we start
				with the users collection, filtering out any users that share no common movies
				with the user for which we're generating recommendations. Then, we project and
				unwind, which first creates new documents containing only the favorite items
				array from the user documents, then creates a new document for each subelement
				within those arrays. Following that, we count the number of ocurrences for each
				film with the group command. Then we remove any movies that are already in the
				user's list of favorite films so as not to be redundant. Following that, we sort by the score
				of each movie, then limit the number of movies we give to the browser using the
				limit command.
				</p>
			</div>
		</div>

		<script type="text/javascript">
			function activateTab() {
				var recTab = document.getElementById("techtab");
				recTab.className = "active";
			}
		</script>

	</div>
</body>

<%@ include file="templates/footer.jsp"%>