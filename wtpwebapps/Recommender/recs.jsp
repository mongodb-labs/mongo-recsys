<%@include file="templates/header.jsp"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<body onload="activateTab()">

	<%@include file="templates/navbar-top.jsp"%>

	<div class="container main">
		<div class="row">
			<div class="span12">
				<%@include file="templates/navbar.jsp"%>
			</div>
		</div>

		<h4 class="muted">Films Recommended for You</h4>

		<div class="row">
			<c:forEach var="film" items="${films}" varStatus="loop">
				<div class="span6">
					<div class="tile">
						<div class="titlediv">
							<h2>${fn:toUpperCase(film)}</h2>
						</div>
						<br>
						
						<div class="row">
							<div class="span2">
								<div class="crop">
									<img src="${images[loop.index]}">
								</div>
							</div>
							<!--  Movie descriptions can go here. -->
							<div class="span3">
								<div class="textblock">${plots[loop.index]}</div>
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