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
				<h4>Description</h4>
				<p>
					Recommender is a content recommendation system built on MongoDB.<br>
					<br>Designed with simplicity and concurrency in mind,
					Recommender seeks to provide an example of how a developer might
					tackle a broad problem leveraging the capabilities of MongoDB. <br>
					<br> Recommender uses a sample data set of the one thousand
					most highly grossing movies of all time. Users are able to create
					profiles and select their favorite films. Based on the other users
					in the database, Recommender executes an aggregation pipeline that
					gets a list of films that the user might enjoy. The algorithm is
					purely in MongoDB, and can be viewed under the "View Source Code"
					tab, or on the GitHub repository.
				</p>
			</div>
			<div class="span7">
				<h4>Scope</h4>
				<h5 class="muted">Recommender is...</h5>
				<ul>
					<li>A simple example of a recommendation engine using MongoDB.</li>
					<li>A helpful starting point for developers looking to build
						their own applications.</li>
					<li>A showcase of MongoDB's features, particularly the
						aggregation framework.</li>
					<li>An overview of how MongoDB can be used in practice.</li>

				</ul>
				<h5 class="muted">Recommender is NOT...</h5>
				<ul>
					<li>A drag-and-drop, no modification solution for individuals
						and enterprises.</li>
					<li>A highly optimized machine learning system.</li>
					<li>A system so specialized that it would be difficult to
						adapt to other goals and data types.</li>
				</ul>
			</div>
			<div class="span7">
				<h4>Extension</h4>
				<p>
					Plugging highly specialized and optimized recommendation
					algorithms into Recommender is relatively simple, and we encourage
					it. <br>
					<br> The algorithm is executed in GetRecommendations.java,
					primarily in the identifyTopMovies method, which is where the
					aggregation pipeline is constructed and run. <br>
					<br> Simple modifications could include something as minor as
					scoring by an additional feature, or something as complex as
					bringing in an entirely new recommendation algorithm. <br>
					<br> Smaller modifications can easily be inserted into the
					pipeline (eg: add another field to a $project command or $match by
					more criteria). <br>
					<br> Larger modifications might include swapping out large
					portions of the pipeline for more complex code. One possible
					use case could be simply using the pipeline to filter out users
					who don't share any common movies, then executing the new algorithm
					over that set. <br>
					<br> The primary takeaways from this section are:
				</p>
				<ul>
					<li>Modifying Recommender is straightforward.</li>
					<li>Unless your goal application has the same functionality as
						Recommender in its current state, you will have to make
						modifications.</li>
				</ul>
			</div>
			<div class="span7">
				<h4>Installation</h4>
				<p>The installation instructions can be found <a href="https://github.com/10gen-interns/mongo-recsys">here</a>.</p>
			</div>
			<div class="span7" style="margin-bottom: 50px">
				<h4>Contact</h4>
				<p>
					Reach out to the sales team at: <a href="mailto:sales@10gen.com">sales@10gen.com</a>
				</p>
			</div>
		</div>

		<script type="text/javascript">
			function activateTab() {
				var recTab = document.getElementById("abouttab");
				recTab.className = "active";
			}
		</script>

	</div>
</body>

<%@ include file="templates/footer.jsp"%>