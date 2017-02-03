<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<!-- Site Header and CDN Files !!Must be include in every page!! -->
<jsp:directive.include file="header.html" />
<title>Login on Course Confidential</title>
</head>


<body>
	<!-- Navigation Bar !!Must be included in every page !! -->
	<jsp:directive.include file="sitenavigation.html" />

	<!-- <div style="margin: 0 auto; width: 35%; text-align: left"> -->
	<div class="row">
		<div class="col-md-3"></div>
		<div class="col-md-5">
			<h3>
				<strong>Log in to Course Confidential</strong>
			</h3>
			<br>
			<div id="input-email" class="form-group">
				<label for="email" style="text-align: left"
					class="col-sm-2 control-label">Email</label>
				<div id="email-field" class="col-sm-10">
					<input type="email" class="form-control" id="loginEmail"
						placeholder="Email"><br>
				</div>
			</div>

			<div id="input-pass" class="form-group">
				<label for="password" style="text-align: left"
					class="col-sm-2 control-label">Password</label>
				<div id="pass-field" class="col-sm-10">
					<input type="password" class="form-control" id="loginPassword"
						placeholder="Password"><br>
				</div>
			</div>

			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<button id="submit" class="btn btn-warning">Login</button>
					<a href="forgotpassword.jsp"><small>Forgot
							Password?</small></a>
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<p style="color: #847f93">
						<em><small>New to Course Confidential?</small></em> <a
							href="http://69.164.206.63:8080/CourseConfidential/registration.jsp"><small>Sign
								up now!</small></a>
					</p>
					<br><p id="error"></p>
				</div>
			</div>	
		</div>
		
		<div class="col-md-4"></div>
	</div>
	<!-- Footer !!Must be included in every page !! -->
	<br><jsp:directive.include file="sitefooter.html" />
</body>
<script>
var response;

$(document).ready(function(){
	checkCookies();
	$("#submit").click(function(){
		login();
	});
});


function error(msg)
{
	$("#error").html("<div class='alert alert-danger' role='alert'><span class='glyphicon glyphicon-exclamation-sign' aria-hidden='true'></span><span class='sr-only'>Error:</span>   "+msg+"</div>")	
}


function verify(){
	var correct = 0;
	if($("#loginEmail").val() == ''){
		$("#input-email").addClass("has-error has-feedback");
		$("#email-field").append("<span id='span1' class='glyphicon glyphicon-remove form-control-feedback' aria-hidden='true'></span>");
		correct = 1;
	}
	if($("#loginPassword").val() == ''){
		$("#input-pass").addClass("has-error has-feedback");
		$("#pass-field").append("<span id='span2' class='glyphicon glyphicon-remove form-control-feedback' aria-hidden='true'></span>");
		correct = 1;
	}
	if(correct != 0){
		if($("#loginEmail").val() == ''){
			error("Email required for login");
		}
		if($("#loginPassword").val() == ''){
			error("Password required for login");
		}
	}
	return correct;
}
function login(){
	var loginEmail;
	var loginPassword;
	
	if($("#input-email").hasClass("has-error has-feedback")){
		$("#input-email").removeClass("has-error has-feedback");
		$("#span1").remove();
	}
	if($("#input-pass").hasClass("has-error has-feedback")){
		$("#input-pass").removeClass("has-error has-feedback");
		$("#span2").remove();
	}
	$("#error").empty();
	
	if(verify() != 0){
		return;
	}
	loginEmail = $("#loginEmail").val();
	loginPassword = $("#loginPassword").val();
		
	 $.ajax({
	        type: "POST",
	        url: "${pageContext.request.contextPath}/LoginServlet",
	        data: {postrequest: "login", email : loginEmail, password : loginPassword }
	      }).done(function( msg ) {
	    	response = $.parseJSON(msg);
	    	if(response.loginstatus === "fail"){
	    		error(response.reason);
	    	}
	    	else{
	    		if(getCookie("user-type") == "Administrator"){
	    			window.location.replace("${pageContext.request.contextPath}/admin.jsp");
	    		}else{
	    			window.location.replace("${pageContext.request.contextPath}/index.jsp");
	    		}
	    			
	    	}
	        }
	      );
}
function checkCookies(){
	if(checkCookie("user-type") == 1){
		if(usertype != "guest")
		window.location.replace("${pageContext.request.contextPath}/index.jsp");	
	}
}
</script>
</html>