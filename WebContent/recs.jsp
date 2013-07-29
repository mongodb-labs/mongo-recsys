<%@include file="templates/header.jsp"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<body onload="activateTab()">

	<div class="container">

		<div class="row">
			<div class="span4">
				<h1>Recommender</h1>
			</div>
		</div>

		<br>

		<%@include file="templates/navbar.jsp"%>

		<h4 style="color: #C0C0C0">Films Recommended for You</h4>

		<br>

		<div class="row">
			<c:forEach var="film" items="${films}">
				<div class="span6">
					<div class="tile">
						<h5 style="color: white">${film}</h5>
						<br>
						<div class="row">
							<div class="span2">
								<img src="http://bit.ly/18Hum1v">
							</div>
							<!--  Movie descriptions can go here. -->
							<div class="span3">
								<div class="textblock">In the future Jake a paraplegic war veteran
									is brought to another planet Pandora which is inhabited by the
									Na'vi a humanoid race with their own language and culture.
									Those from Earth find themselves at odds with each other and
									the local culture.</div>
							</div>
						</div>

					</div>
				</div>
			</c:forEach>
		</div>

	</div>

	<script type="text/javascript">
		function activateTab() {
			var recTab = document.getElementById("recommendtab");
			recTab.className = "active";
		}
	</script>

	<%@include file="templates/footer.jsp"%>