<%@include file="templates/header.jsp"%>

<body>

	<div class="text-center">
		<h1>Recommender</h1>
	</div>

	<div class="text-center">
		<h4 style="color: #C0C0C0">Sign In / Create Account</h4>
		<br>
	</div>

	<div class="container" id="loginbox">
		<div class="content">
			<div class="row">
				<div class="login-form">
					<form action="Login" method="post">
						<fieldset>
							<div class="clearfix">
								<input type="text" name="userid" placeholder="Username"
									style="border-color: black; border-style: solid; border-width: 2px;"
									autofocus>
							</div>
						</fieldset>
						<button class="btn btn-primary" type="submit">Sign In</button>
						<button class="btn" type="button" onClick="unhide(); hideMsg(); hideLogin();"
							id="displaynewuserbutton">New User?</button>

					</form>
					<div id="messagediv">${message}</div>
				</div>
			</div>
		</div>
	</div>

	<div class="container" style="visibility: hidden" id="newuser">
		<div class="content">
			<div class="row">
				<div class="login-form">
					<form action="CreateUser" method="post">
						<fieldset>
							<div class="clearfix">
								<input type="text" id="newfield" name="userid" placeholder="New Username"
									style="border-color: black; border-style: solid; border-width: 2px;"
									autofocus><br> <input type="text" name="realname"
									placeholder="Real Name"
									style="border-color: black; border-style: solid; border-width: 2px;">
							</div>
						</fieldset>
						<button class="btn btn-primary" type="submit">Create Account</button>
					</form>
				</div>
			</div>
		</div>
	</div>

	<script type="text/javascript">
		function unhide() {
			var hiddenDiv = document.getElementById('newuser');
			hiddenDiv.style.visibility = 'visible';
			var field = document.getElementById('newfield');
			field.focus();
		}
		function hideLogin() {
			var login = document.getElementById('loginbox');
			login.style.display = 'none';
		}
		function hideMsg() {
			var msgdiv = document.getElementById('messagediv');
			msgdiv.style.display = 'none';
		}
	</script>

	<%@include file="templates/footer.jsp"%>