<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<!-- Site Header and CDN Files !!Must be include in every page!! -->
<jsp:directive.include file="header.html" />
<title>Course Confidential</title>
</head>


<body>
<!-- Navigation Bar !!Must be included in every page !! -->
<jsp:directive.include file="sitenavigation.html" />


<!-- --------------Main Container--------------------- -->
<div class="row">
	<div class="col-md-3"></div>
	<div class="col-md-6">
		<div id ="display" class="row">
			<h2>Forgot Password</h2>
			<br>
			<p id="forgotmessage">
			<div id="userbox" class="col-md-12">
				<h3 id="passquestion">Enter your email address</h3>
				<br>
				<input type="text" class="form-control" id="passwordanswer">
				<br>
				<button id='submit' type='button' class='btn btn-success' onclick="loadQuestion()">Submit</button>
				<br>
				<br>
				<p id="warn"><p>
			</div>
		</div>
	</div>
	<div class="col-md-3"></div>
</div>


<!-- Footer !!Must be included in every page !! -->
<br><jsp:directive.include file="sitefooter.html" />
</body>
<script>

var passanswer;
var useranswer;
var passuser;

$(document).ready(function(){
	authUser();
	init()
})

function init()
{
	$("#forgotmessage").html("Instructions: To reset your password you must first answer the email address associated with your account");		
}

function loadQuestion()
{
	useranswer = $("#passwordanswer").val();
	
	$.ajax({
		
   	 	type: "POST",
    	url: "${pageContext.request.contextPath}/LoginServlet",
   		data: {postrequest: "forgot-password", useremail: useranswer}
    	
  	}).done(function( msg ) {	  	
	  	response = $.parseJSON(msg);
	  	
	    
	  	
	  	if(response.validation=="fail")
	  	{ 
	  		$("#warn").html("<div class='alert alert-danger' role='alert'><span class='glyphicon glyphicon-exclamation-sign' aria-hidden='true'></span><span class='sr-only'>Error:</span>   Email not found...please try again.</div>");
	  		
	  	}else{
	  		$("#warn").html("");
	  		//assuming success
	  		passanswer = response.answer;
	  		passuser = response.userid;
	  		$("#forgotmessage").html("Account lookup is successful! Please answer the question below to change your password");
	  		$("#passquestion").html(response.question);
	  		//rebind button
			$("#submit").attr('onclick','validate()');
	  		$("#passwordanswer").val("");
	  	}

  	});
}

function validate()
{
	useranswer = $("#passwordanswer").val();
	if(passanswer == useranswer){
		//remove warning
		$("#warn").html("");
	
		$("#userbox").html(
			"<h3>Enter New Password</h3><input id='pass1' type=password class='form-control'><br><h3>Confirm Password</h3><input id='pass2' type=password class='form-control'><br><button id='submit' type='button' class='btn btn-success' onclick='change()'>Submit</button><br>"
		);
	}
	else
		$("#warn").html("<div class='alert alert-danger' role='alert'><span class='glyphicon glyphicon-exclamation-sign' aria-hidden='true'></span><span class='sr-only'>Error:</span>   Your submission is incorrect...please try again.</div>")
		
}

function change()
{
	var pass1 = $("#pass1").val();
	var pass2 = $("#pass2").val();
	
	if(pass1 == pass2)
	{
		$.ajax({
			
	   	 	type: "POST",
	    	url: "${pageContext.request.contextPath}/LoginServlet",
	   		data: {postrequest: "reset-password", password: pass1, username: passuser}
	    	
	  	}).done(function( msg ) {	  	
		  	$("#userbox").html("<h3>Your password has been sucessfully reset. Thank You!");
	  	});
		
	}
	else
		$("#warn").html("<div class='alert alert-danger' role='alert'><span class='glyphicon glyphicon-exclamation-sign' aria-hidden='true'></span><span class='sr-only'>Error:</span>   Passwords do not match...please try again.</div>")

	
}
</script>


</html>