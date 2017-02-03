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
	<div class="col-md-2"></div>
	<div class="col-md-8">
		<div class="row">
			<div class="col-md-3">
				<img class="img-responsive" alt="Responsive image" src ="${pageContext.request.contextPath}/images/site/man-big-left-arm-raised.jpg">
			</div>
			<div class="col-md-6">
				<h3>
					<strong>Contact Administrator</strong>
				</h3>
				<br>
				<form class="form-horizontal" action="javascript:void(0);">
				
				<div class="form-group" id="input-name">
					<label style="text-align: left" class="col-md-2 control-label">Name</label>
					<div class="col-md-10" id="name-field">
						<input type="text" class="form-control" id="inputName">
   	 				</div>
	  			</div>
	  			
	  			<div class="form-group" id="input-email">
					<label style="text-align: left" class="col-md-2 control-label">Contact Email</label>
					<div class="col-md-10" id="email-field">
						<input type="text" class="form-control" id="inputEmail">
   	 				</div>
	  			</div>
				
				<div class="form-group" id="input-task">
	  				<label style="text-align: left" class="col-md-2 control-label">Reason for contact</label>
				    <select id="inputTask">
				    	<option selected="selected">-</option>
				    	<option value="Report File">Report a file</option>
				    	<option value="Report Comment">Report a comment</option>
				    	<option value="Other">Other</option>
				    </select>
				</div>
				
				<div class="form-group" id="input-url">
					<label style="text-align: left" class="col-md-2 control-label ">URL</label>
					<div class="col-md-10" id="url-field"> 
						<input type="text" class="form-control" id="inputUrl" disabled="disabled">
   	 				</div>
	  			</div>
				<script>
					$(document).ready(function(){
						$("#inputUrl").attr("value", document.referrer);
					})	
				</script>
				<div class="form-group" id="input-desc">
	  				<label style="text-align: left" class="col-md-10 control-label">Description of the problem</label>
	  				<div class="col-md-10" id="desc-field">
						<textarea class="form-control" rows="5" id="inputDesc"></textarea>					
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
				</form>
			</div>
			<div class="col-md-3"></div>
		</div>
	</div>
	<div class="col-md-2"></div>
</div>

<script>
$(document).ready(function(){
	$("#inputSubmit").click(function(){
		register();
	});
});
function verify(){
	resetFields();
	var correct = 0;
	if($("#inputEmail").val() == ''){
		correct = 1;
		$("#input-email").addClass("has-error has-feedback");
		$("#email-field").append("<span id='span1' class='glyphicon glyphicon-remove form-control-feedback' aria-hidden='true'></span>");
	}
	if($("#inputTask").val() == "-"){
		correct = 1;
		$("#input-task").addClass("has-error has-feedback");
	}
	if($("#inputDesc").val() == ''){
		correct = 1;
		$("#input-desc").addClass("has-error has-feedback");
		$("#desc-field").append("<span id='span2' class='glyphicon glyphicon-remove form-control-feedback' aria-hidden='true'></span>");
	}
	
	if(correct != 0){
		$("#error").html("There is missing or incorrect information");
		$("#error").css("color: red");
	}
	
	return correct;
}
function register(){
	if(verify() != 0){
		return;
	}

	var newName = $("#inputName").val();
	var newEmail = $("#inputEmail").val();
	var newTask = $("#inputTask").val();
	var newUrl = $("#inputUrl").val();
	var newDescription = $("#inputDesc").val();
	var newUsername = getCookie("user-name");

	 $.ajax({
	        type: "POST",
	        url: "${pageContext.request.contextPath}/AdminServlet",
	        data: {adminrequest: "store-report", name : newName, email : newEmail, task : newTask, url : newUrl, description : newDescription, username : newUsername }
	      }).done(function( msg ) {
	    	  window.location.replace("${pageContext.request.contextPath}/index.jsp");
	        }
	      );

}

function resetFields()
{
	$("#input-name").removeClass("has-error has-feedback");
	$("#span1").remove();
	$("#input-email").removeClass("has-error has-feedback");
	$("#span2").remove();
	$("#input-desc").removeClass("has-error has-feedback");
	$("#input-task").removeClass("has-error has-feedback");
	$("#error").text("");
}

$(document).ready(function(){
	$("#inputResetForm").click(function(){
		clear();
	});
});

function clear(){
	$("#inputName").val('');
	$("#inputEmail").val('');
	$("#inputTask").val('-');
	$("#inputDesc").val('');
	resetFields();
}

function getCookie(cname) {
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for(var i=0; i<ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0)==' ') c = c.substring(1);
        if (c.indexOf(name) == 0) return c.substring(name.length, c.length);
    }
    return "";
}
</script>
<!-- Footer !!Must be included in every page !! -->
<br><jsp:directive.include file="sitefooter.html" />
</body>
</html>