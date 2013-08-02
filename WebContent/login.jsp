<%@include file="templates/header-landing.jsp"%>

<body id="splash">
  <div class="navbar navbar-inverse navbar-static-top">
    <div class="navbar-inner">
      <a class="brand" href="/Recommender/login.jsp">Recommender</a>
      <span class="powered-by">Powered by MongoDB</span>
    </div>
  </div>

  <div class="container">
    <div class="row">
      <div class="span6 offset3">
        <h1 class="tagline">Get customized movie recommendations based on your favorite movies.</h1>
        <div class="login-box" id="loginbox">
          <div class="login-heading clearfix">
            <h2>Sign In</h2>
            <p class="login-switch">Need an account? <a href="#" onclick="unhideCreate(); hideMsg(); hideLogin(); return false">Create New Account</a></p>
          </div>
          
          <form class="form-horizontal login-form" action="Login" method="post">
            <div class="control-group">
              <label class="control-label" for="unique_id">Username</label>
              <div class="controls">
                <input class="span3" type="text" name="unique_id" id="userid" placeholder="Username" autofocus>
              </div>
            </div>
            <div class="control-group">
              <div class="controls">
                <button class="btn btn-primary btn-large" type="submit">Sign In</button>
              </div>
            </div>
          <div id="messagediv" class="text-center">${message}</div>
          </form>
        </div>
        <div class="login-box" id="newuser" style="display:none;">
          <div class="login-heading clearfix">
            <h2>Create Account</h2>
            <p class="login-switch">Already have an account? <a href="#" onclick="hideCreate(); unhideLogin(); return false">Log In</a></p>
          </div>
          <form class="form-horizontal login-form" action="CreateUser" method="post">
            <div class="control-group">
              <label class="control-label" for="newfield">New Username</label>
              <div class="controls">
                  <input class="span3" type="text" id="newfield" name="unique_id" placeholder="New Username" autofocus><br> 
              </div>
            </div>
            <div class="control-group">
              <label class="control-label" for="realname">Real Name</label>
              <div class="controls">
                <input class="span3" type="text" name="realname" id="realname" placeholder="Real Name">
              </div>
            </div>
            <div class="control-group">
              <div class="controls">
                <button class="btn btn-primary btn-large" type="submit">Create Account</button>
              </div>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>

  <script type="text/javascript">
    function unhideCreate() {
      var hiddenDiv = document.getElementById('newuser');
      hiddenDiv.style.display = 'block';
      var field = document.getElementById('newfield');
      field.focus();
    }
    function hideCreate() {
        var hiddenDiv = document.getElementById('newuser');
        hiddenDiv.style.display = 'none';
    }
    function hideLogin() {
      var login = document.getElementById('loginbox');
      login.style.display = 'none';
    }
    function unhideLogin() {
    	var login = document.getElementById('loginbox');
    	login.style.display = 'block';
    	var field = document.getElementById('userid');
    	field.focus();
    }
    function hideMsg() {
      var msgdiv = document.getElementById('messagediv');
      msgdiv.style.display = 'none';
    }
  </script>

  <%@include file="templates/footer.jsp"%>
