<%@include file="templates/header.jsp"%>

<body>

	<div align="left">

		<div class="row" align="left">
			<h1>Recommender</h1>
		</div>

		<div class="row">
			<div align="left">
				<form action="Login" method="get"> <!--  Normally this is POST -->
					<p id="smallheader">Returning Users</p>
					<div id="descriptor">Unique ID:</div>
					&nbsp;
					<div id="loginbar">
						<input type="text" name="userid">
					</div>
					<div id="loginbutton">
						<button name="subject" type="submit" value="login">Login</button>
					</div>
				</form>
			</div>
		</div>

		<div class="row">
			<button onClick="unhide(); hideButton(); hideMsg();" id="displaynewuserbutton">New User?</button>
		</div>

		<script type="text/javascript">
			function unhide() {
				var hiddenDiv = document.getElementById('newuser');
				hiddenDiv.style.display = 'inline';
			}
			function hideButton() {
				var button = document.getElementById('displaynewuserbutton');
				button.style.display = 'none';
			}
			function hideMsg() {
				var msgdiv = document.getElementById('messagediv');
				msgdiv.style.display = 'none';
			}
		</script>

		<!-- This form allows for new user creation, and remains hidden until user activation. -->
		<div class="row" style="display:none" id="newuser">
			<div align="left">
				<form action="CreateUser" method="post">
					<p id="smallheader">New Users</p>
					<div id="descriptor">Unique ID:</div>
					<div id="loginbar">
						<input type="text" name="userid"><br>
					</div>
					<div id="descriptor">Name:</div>
					<div id="loginbar">
						<input type="text" name="realname">
					</div>
					<div id="loginbutton">
						<button name="subject" type="submit" value="create">Create
							New User</button>
					</div>
				</form>
			</div>
		</div>
		<div class="row" id="messagediv">
			<p>${message}</p>
		</div>
	</div>

	<%@include file="templates/footer.jsp"%>