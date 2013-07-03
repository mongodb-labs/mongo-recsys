<%@include file="templates/header.jsp"%>

<div class="container">

	<div class="row">
		<div class="offset-by-six">
			<div class="four columns">
				<h6>A recommendation engine built with MongoDB.</h6>
				<br>
				<form action="Login" method="post">
					<p id="smallheader">Returning Users</p>
					<p>
						Unique ID:<input type="text" name="userid">
					</p>
					<button name="subject" type="submit" value="login">Login</button>
				</form>
			</div>
		</div>
	</div>

	<div class="row">
		<div class="offset-by-six">
			<div class="four columns">
				<form action="CreateUser" method="post">
					<p id="smallheader">New Users</p>
					<p>
						Unique ID:<input type="text" name="userid"><br> Full
						Name:<input type="text" name="realname">
					</p>
					<button name="subject" type="submit" value="create">Create
						New User</button>
				</form>
			</div>
		</div>
	</div>

	<div class="row">
		<div class="offset-by-six">
			<div class="four columns">
				<p>${message}</p>
			</div>
		</div>
	</div>

</div>

<%@include file="templates/footer.jsp"%>