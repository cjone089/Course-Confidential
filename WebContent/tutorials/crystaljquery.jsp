<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Welcome to the JQuery Tutorial</title>
<!--Bootstrap Latest compiled and minified CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">

<!-- jQuery --->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>

</head>
<body>

<center>
	<table>
		<tr>
			<td><img src ="${pageContext.request.contextPath}/tutorials/K20AC_Kirby5.png"></td>
			<td><h1>WELCOME TO THE JQUERY TUTORIAL!</h1></td>
			<td><img src ="${pageContext.request.contextPath}/tutorials/K20AC_Kirby5.png"></td>
		</tr>
	</table>
</center>


<!-- Note 1 -->
<div class="well">
<p> 1. JQuery is a javascript library that allows you to make changes to a webpage after a webpage has already loaded! <P><br>
<p> 2. We need JQuery because after a web page has loaded the html code that makes up a page cannot be changed...but with JQuery it can!  <p><br>
</div>
<br>
<p>Children's Names</p>
<table id="childs">
	<tr>
		<td>First Name<input id="first" type="text"></td><td>Last Name<input id="last" type="text"></td>
	</tr>
	<tr>
		<td>Street Address<input id="street" type="text"></td>
	</tr>
	<tr>
		<td>City<input id="city" type="text"></td><td>State<input id="state" type="text"></td>
	</tr>
	<tr>
		<td>Zip<input id="zip" type="text"></td>
	</tr>
</table>
<br>
<p id="error"></p>
<button id="addbutt">Add</button><button id="submit">Submit</button>
<script>
	$(document).ready(function(){
		$("#addbutt").click(function(){
			addRow();
		})
		$("#submit").click(function(){
			verify();
		})
	});
	
	
function addRow(){
	$("#childs").append("<tr><td>First Name<input type='text'></td><td>Last Name<input type='text'></td></tr>")
}
function verify(){
	if($("#first").val() == ''){
	    $("#error").html('First Name can not be left blank');
	   }
	if($('#last').val() == ''){
		$("#error").html('Last Name can not be left blank');
	   }
	if($('#street').val() == ''){
		$("#error").html('Street can not be left blank');
	   }
	if($('#city').val() == ''){
		$("#error").html('City can not be left blank');
	   }
	if($('#state').val() == ''){
		$("#error").html('State can not be left blank');
	   }
	if($('#zip').val() == ''){
		$("#error").html('Zip can not be left blank');
	   }
}
</script>

</body>
</html>