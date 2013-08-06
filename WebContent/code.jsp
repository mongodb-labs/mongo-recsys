<%@ include file="templates/header.jsp"%>

<body onload="activateTab()">

	<%@include file="templates/navbar-top.jsp"%>

	<div class="container main">
		<div class="row">
			<div class="span12">
				<%@include file="templates/navbar.jsp"%>
			</div>
		</div>

		<h4 class="muted">Source Code for the Recommendation Algorithm</h4>

		<br>

		<div class="row" style="margin-bottom:50px">
			<div class="span12">
				<script
					src="https://gist.github.com/dkhavari/073985da71301ee9c27b.js"></script>
			</div>
		</div>
	</div>

	<script type="text/javascript">
		function activateTab() {
			var recTab = document.getElementById("codetab");
			recTab.className = "active";
		}
	</script>


</body>

<%@ include file="templates/footer.jsp"%>