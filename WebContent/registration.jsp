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
	<div class="col-md-2">
	</div>
	<div class="col-md-8">
		<div class="row">
			<div class="col-md-3">
				<img class="img-responsive" alt="Responsive image" src ="${pageContext.request.contextPath}/images/site/man-big-left-arm-raised.jpg">	
			</div>
			<div class="col-md-6">
			<h3>
				<strong>Register New User</strong>
			</h3>
				<br>
				<form class="form-horizontal" action="javascript:void(0);">
					<div class="form-group" id="input-fname">
						<label style="text-align: left" class="col-md-2 control-label">First Name</label>
						<div class="col-md-10" id="fname-field">
							<input type="text" class="form-control" id="inputFName">
	   	 				</div>
	  				</div>
					<div class="form-group" id="input-lname">
						<label style="text-align: left" class="col-md-2 control-label">Last Name</label>
						<div class="col-md-10" id="lname-field">
							<input type="text" class="form-control" id="inputLName">
	   	 				</div>
	  				</div>
					<div class="form-group" id="input-username">
						<label style="text-align: left" class="col-md-2 control-label">Username</label>
						<div class="col-md-10" id="username-field">
							<input type="text" class="form-control" id="inputUsername">
	   	 				</div>
	  				</div>
					<div class="form-group" id="input-email">
						<label style="text-align: left" class="col-md-2 control-label">Email</label>
						<div class="col-md-10" id="email-field">
							<input type="text" class="form-control" id="inputEmail">
	   	 				</div>
	  				</div>
					<div class="form-group" id="input-password">
						<label style="text-align: left" class="col-md-2 control-label">Password</label>
						<div class="col-md-10" id="password-field">
							<input type="password" class="form-control" id="inputPassword">
	   	 				</div>
	  				</div>
					<div class="form-group" id="input-cpassword">
						<label style="text-align: left" class="col-md-2 control-label">Confirm Password</label>
						<div class="col-md-10" id="cpassword-field">
							<input type="password" class="form-control" id="inputCPassword">
	   	 				</div>
	  				</div>
	  				<div class="form-group" id="input-university">
						<label style="text-align: left" class="col-md-10 control-label">University (required for professors)</label>
						<div class="col-md-10" id="university-field">
							<input type="text" class="form-control" id="inputUniversity">
	   	 				</div>
	  				  </div>
	  				  <div class="form-group" id="input-department">
						<label style="text-align: left" class="col-md-10 control-label">Department (required for professors)</label>
						<div class="col-md-10" id="department-field">
							<input type="text" class="form-control" id="inputDepartment">
	   	 				</div>
	  				  </div>
	  				<div class="form-group" id="input-userType">
	  				<label style="text-align: left" class="col-md-2 control-label">Account Type</label>
					    <select id="inputUserType">
					    	<option selected="selected">-</option>
					    	<option value="Registered User">Registered User</option>
					    	<option value="Professor">Professor</option>
					    </select>
					  </div>
					  <br>
					  <h4>
						<strong>Security Questions</strong>
					  </h4>
					  <br>
					  <div class="form-group" id="input-question1">
						<label style="text-align: left" class="col-md-10 control-label" id="question1"></label>
						<div class="col-md-10" id="question1-field">
							<input type="text" class="form-control" id="inputQuestion1">
	   	 				</div>
	  				  </div>
	  				  <div class="form-group" id="input-question2">
						<label style="text-align: left" class="col-md-10 control-label" id="question2"></label>
						<div class="col-md-10" id="question2-field">
							<input type="text" class="form-control" id="inputQuestion2">
	   	 				</div>
	  				  </div>
	  				  <div class="form-group" id="input-question3">
						<label style="text-align: left" class="col-md-10 control-label" id="question3"></label>
						<div class="col-md-10" id="question3-field">
							<input type="text" class="form-control" id="inputQuestion3">
	   	 				</div>
	  				  </div>
	  				  
					  <div class="form-group">
						<div class="col-md-offset-2 col-md-10">
							<button id="inputResetForm" class="btn btn-primary">Reset Form</button>
						</div>
					</div>
					<div class="form-group">
						<div class="col-md-offset-2 col-md-10">
							<button id="inputSubmit" class="btn btn-warning">Submit</button>
						</div>
					</div>
					<div class="form-group">
						<p id="error"></p>
					</div>
					<div class="form-group">
						<p id="error2"></p>
					</div>
				</form>
			</div>
			<div class="col-md-3"></div>
		</div>
	</div>
	<div class="col-md-2"></div>
</div>


<script>
$(document).ready(function(){
	listQuestions();
	deleteAllCookies();
	
	$("#inputSubmit").click(function(){
		register();
	});
	$("#inputResetForm").click(function(){
		clear();
	});
});
function verify(){
	resetFields();
	var correct = 0;
	if($("#inputFName").val() == ''){
		correct = 1;
		$("#input-fname").addClass("has-error has-feedback");
		$("#fname-field").append("<span id='span1' class='glyphicon glyphicon-remove form-control-feedback' aria-hidden='true'></span>");
	}
	if($("#inputLName").val() == ''){
		correct = 1;
		$("#input-lname").addClass("has-error has-feedback");
		$("#lname-field").append("<span id='span2' class='glyphicon glyphicon-remove form-control-feedback' aria-hidden='true'></span>");
	}
	if($("#inputUsername").val() == ''){
		correct = 1;
		$("#input-username").addClass("has-error has-feedback");
		$("#username-field").append("<span id='span3' class='glyphicon glyphicon-remove form-control-feedback' aria-hidden='true'></span>");
	}
	if($("#inputEmail").val() == ''){
		correct = 1;
		$("#input-email").addClass("has-error has-feedback");
		$("#email-field").append("<span id='span4' class='glyphicon glyphicon-remove form-control-feedback' aria-hidden='true'></span>");
	}
	if($("#inputPassword").val() == '' || $("#inputPassword").val() != $("#inputCPassword").val()){
		correct = 1;
		$("#input-password").addClass("has-error has-feedback");
		$("#password-field").append("<span id='span5' class='glyphicon glyphicon-remove form-control-feedback' aria-hidden='true'></span>");
	}
	if(($("#inputCPassword").val() == '') || ($("#inputPassword").val() != $("#inputCPassword").val())){
		correct = 1;
		$("#input-cpassword").addClass("has-error has-feedback");
		$("#cpassword-field").append("<span id='span6' class='glyphicon glyphicon-remove form-control-feedback' aria-hidden='true'></span>");
	}
	if($("#inputUniversity").val() == '' && $("#inputUserType").val() == "Professor"){
		correct = 1;
		$("#input-university").addClass("has-error has-feedback");
		$("#university-field").append("<span id='span7' class='glyphicon glyphicon-remove form-control-feedback' aria-hidden='true'></span>");
	}
	if($("#inputDepartment").val() == '' && $("#inputUserType").val() == "Professor"){
		correct = 1;
		$("#input-department").addClass("has-error has-feedback");
		$("#department-field").append("<span id='span8' class='glyphicon glyphicon-remove form-control-feedback' aria-hidden='true'></span>");
	}
	if($("#inputUserType").val() == "-"){
		correct = 1;
		$("#input-userType").addClass("has-error has-feedback");
	}
	if($("#inputQuestion1").val() == ''){
		correct = 1;
		$("#input-question1").addClass("has-error has-feedback");
		$("#question1-field").append("<span id='span9' class='glyphicon glyphicon-remove form-control-feedback' aria-hidden='true'></span>");
	}
	if($("#inputQuestion2").val() == ''){
		correct = 1;
		$("#input-question2").addClass("has-error has-feedback");
		$("#question2-field").append("<span id='span10' class='glyphicon glyphicon-remove form-control-feedback' aria-hidden='true'></span>");
	}
	if($("#inputQuestion3").val() == ''){
		correct = 1;
		$("#input-question3").addClass("has-error has-feedback");
		$("#question3-field").append("<span id='span11' class='glyphicon glyphicon-remove form-control-feedback' aria-hidden='true'></span>");
	}
	if($("#inputPassword").val() != $("#inputCPassword").val()){
		correct = 1;
		error2("Enter matching passwords");
	}
	
	
	if(correct != 0){
		error("There is missing or incorrect information");
	}
	
	return correct;
}

function error(msg)
{
	$("#error").html("<div class='alert alert-danger' role='alert'><span class='glyphicon glyphicon-exclamation-sign' aria-hidden='true'></span><span class='sr-only'>Error:</span> "+msg+"</div>")	
}
function error2(msg)
{
	$("#error2").html("<div class='alert alert-danger' role='alert'><span class='glyphicon glyphicon-exclamation-sign' aria-hidden='true'></span><span class='sr-only'>Error:</span> "+msg+"</div>")	
}


function register(){
	if(verify() != 0){
		return;
	}
	
	var newFirstName = $("#inputFName").val();
	var newLastName = $("#inputLName").val();
	var newUsername = $("#inputUsername").val();
	var newEmail = $("#inputEmail").val();
	var newPassword = $("#inputPassword").val();
	var newUserType = $("#inputUserType").val();
	var newAnswer1 =  $("#inputQuestion1").val();
	var newAnswer2 =  $("#inputQuestion2").val();
	var newAnswer3 =  $("#inputQuestion3").val();
	var newUniversity = $("#inputUniversity").val();
	var newDepartment = $("#inputDepartment").val();
	
	
	 $.ajax({
	        type: "POST",
	        url: "${pageContext.request.contextPath}/RegistrationServlet",
	        data: {postrequest: "register", email : newEmail, password : newPassword, firstName : newFirstName, lastName : newLastName, username : newUsername, userType : newUserType, answer1 : newAnswer1 , answer2 : newAnswer2 , answer3 : newAnswer3, university : newUniversity, department : newDepartment  }
	      }).done(function( msg ) {
	    	  response = $.parseJSON(msg);
		    if(response.registerstatus === "fail"){
		    		error(response.reason);
		    }
		    else{
		    	window.location.replace("${pageContext.request.contextPath}/index.jsp");
		    }
	        }
	      );
	

}
function resetFields()
{
	$("#input-fname").removeClass("has-error has-feedback");
	$("#span1").remove();
	$("#input-lname").removeClass("has-error has-feedback");
	$("#span2").remove();
	$("#input-username").removeClass("has-error has-feedback");
	$("#span3").remove();
	$("#input-email").removeClass("has-error has-feedback");
	$("#span4").remove();
	$("#input-password").removeClass("has-error has-feedback");
	$("#span5").remove();
	$("#input-cpassword").removeClass("has-error has-feedback");
	$("#span6").remove();
	$("#input-university").removeClass("has-error has-feedback");
	$("#span7").remove();
	$("#input-department").removeClass("has-error has-feedback");
	$("#span8").remove();
	$("#input-userType").removeClass("has-error has-feedback");
	$("#input-question1").removeClass("has-error has-feedback");
	$("#span9").remove();
	$("#input-question2").removeClass("has-error has-feedback");
	$("#span10").remove();
	$("#input-question3").removeClass("has-error has-feedback");
	$("#span11").remove();
	$("#error").text("");
	$("#error2").text("");
}


function clear(){
	$("#inputFName").val('');
	$("#inputLName").val('');
	$("#inputUsername").val('');
	$("#inputEmail").val('');
	$("#inputPassword").val('');
	$("#inputCPassword").val('');
	$("#inputUniversity").val('');
	$("#inputDepartment").val('');
	$("#inputUserType").val('-');
	$("#inputQuestion1").val('');
	$("#inputQuestion2").val('');
	$("#inputQuestion3").val('');
	$("#error").text("");
	$("#error2").text("");
	resetFields();
}


function listQuestions()
{
	$.ajax({
        type: "GET",
        url: "${pageContext.request.contextPath}/RegistrationServlet",
        data: {request: "list_questions"}
      }).done(function( msg ) {
         response = $.parseJSON(msg);
          
         $.each(response, function(key,value) {
         	$("#question1").text(value.question_1);
         	$("#question2").text(value.question_2);
         	$("#question3").text(value.question_3);
         	
         });
     });
}

function deleteAllCookies() {
	var cookies = document.cookie.split(";");
	for (var i = 0; i < cookies.length; i++) { var cookie = cookies[i]; var eqPos = cookie.indexOf("="); var name = eqPos > -1 ? cookie.substr(0, eqPos) : cookie;
	document.cookie = name+'="";-1; path=/';
	}

}
</script>

<!-- Footer !!Must be included in every page !! -->
<br><jsp:directive.include file="sitefooter.html" />
</body>
</html>