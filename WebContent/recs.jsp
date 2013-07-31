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
			<div class="span12">
				<ul class="media-list movie-list clearfix">
					<c:forEach var="film" items="${films}" varStatus="loop">
						<li class="media movie"><a class="pull-left movie-poster"
							href="#" onclick="return null;"> <img class="media-object"
								src="${images[loop.index]}">
						</a>
							<div class="media-body">
								<h4 class="media-heading">${films[loop.index]}</h4>
								<p>${plots[loop.index]}</p>
							</div></li>
					</c:forEach>
				</ul>
			</div>
		</div>
	</div>

	<script type="text/javascript">
		function activateTab() {
			var recTab = document.getElementById("recommendtab");
			recTab.className = "active";
		}
	</script>

	<%@include file="templates/footer.jsp"%>