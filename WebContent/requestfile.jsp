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
<div data-ng-app="searchApp" data-ng-controller="searchController"> 	   <!--  Angular JS Controller for this element  -->
<div class="row">
	<div class="col-md-2"></div>
	<div class="col-md-8">
		<div class="row">
			<div class="col-md-3">
				<img align="left" class="img-responsive" alt="Responsive image" src ="${pageContext.request.contextPath}/images/site/computer-professor.jpg">
			</div>
			<div class="col-md-6">
				<h3>
					<strong>Request Course File</strong>
				</h3>
				<br>
				<h4>Select a course</h4>
				<table class = table>
					<tr>
						<td><font id="university-row" color="black">University</font></td>
						<td>
						  <select id="select-university">
						    <option>-</option>
							<option data-ng-repeat="item in data">{{item.name}}</option>
						  </select>
						</td>
					</tr>
					<tr>
					<td><font id="courses-row" color="black">Course</font></td>
						 <td>
						  <select id="select-courses">
						   <option>-</option>
						  </select>
						</td>
					</tr>
					<tr>
						<td><font id="professor-row" color="black">Professor</font></td>
						<td>
						 <select id="select-professor">
						  <option>-</option>
						  </select>
						 </td>
					</tr>
					<tr>
						<td><font id="term-row" color="black">Term</font></td>
						<td>
						<select id="select-term">
						<option>-</option></select></td>
					</tr>	
			</table>
			
			<form class="form-horizontal" action="javascript:void(0);">
				<div class="form-group" id="input-type">
	  				<label style="text-align: left" class="col-md-4 control-label">Select the type of file</label>
				    <select id="inputType">
				    	<option selected="selected">-</option>
				    	<option value="Syllabus">Syllabus</option>
				    	<option value="Notes">Notes</option>
				    	<option value="Exams">Exams</option>
				    </select>
				</div>
	  			<div class="form-group" id="input-desc">
	  				<label style="text-align: left" class="col-md-10 control-label">Extra details about the file, if any</label>
	  				<div class="col-md-10" id="desc-field">
						<textarea class="form-control" rows="3" id="inputDesc"></textarea>					
					</div>
				</div>
				<div class="form-group">
						<button id="inputResetForm" class="btn btn-primary">Reset Form</button>
						<button id="inputSubmit" class="btn btn-warning">Submit</button>
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
</div>

<script>

var university;
var courses;
var professors;

//event handler binding 
$(function(){
	
	//bind to the university select, detect when it changes, and then get the value
	$("#select-university").on('change', function(){
		 university =  $("option:selected", this);
		 university = this.value;
		fillForm(university);
	})
})


//gets the necessary data to fill parts of the form
function fillForm(uni)
{
	var test;
	$.ajax({
		
   	 	type: "GET",
    	url: "${pageContext.request.contextPath}/SearchServlet",
   		data: {query: "list-all-courses", school: uni}
    	
  	}).done(function( msg ) {	  	
	  	response = $.parseJSON(msg);
	  	
	  	$.each(response,function(key,value){
	  		$("#select-courses").append("<option>"+value.name+"</option>")
	  		
	  	})
	  	
	  	$("#select-courses").on('change', function(){
				listProfessor();
			})
  	});
	
	
}

//fills the professor list
function listProfessor(courseObj)
{
	courses  = $("#select-courses option:selected").text();
	
	
	$.ajax({
		
   	 	type: "GET",
    	url: "${pageContext.request.contextPath}/SearchServlet",
   		data: {query: "list-professor", school: university, course: courses }
    	
  	}).done(function( msg ) {	  	
	  	response = $.parseJSON(msg);
	  	
	  	$.each(response,function(key,value){
	  		$("#select-professor").append("<option>"+value.professor+"</option>")
	  	})
	  	
	  	$("#select-professor").on('change', function(){
				listTerm();
		})
  	});
	
}

function listTerm()
{
	professors =  $("#select-professor option:selected").text();
	
	$.ajax({
		
   	 	type: "GET",
    	url: "${pageContext.request.contextPath}/SearchServlet",
   		data: {query: "list-term", school: university, course: courses, professor: professors}
    	
  	}).done(function( msg ) {	  	
	  	response = $.parseJSON(msg);
	  	
	  	$.each(response,function(key,value){
	  		$("#select-term").append("<option>"+value.term+"</option>")
	  	})
	  	
	  	 	
  	});
	
}



//Angular JS Controller code abandon all hope ye who read here
var responseObject;

var searchApp = angular.module("searchApp", []);
searchApp.controller("searchController", function($scope, $http) {
		
	 $http({
	        url: "${pageContext.request.contextPath}/SearchServlet", 
	        method: "GET",
	        params: {query: "list-all-universities"}
	     })
	     .success
	     (
	    		 function(response) 
	    		 {
	    			responseObject = angular.fromJson(response);
	    			$scope.data = responseObject;
	   
	    		 }
	    		  
	     );
    
});

$(document).ready(function(){
	$("#inputSubmit").click(function(){
		register();
	});
});
function verify(){
	resetFields();
	var correct = 0;
	if($("#select-university").val() == '-'){
		correct = 1;
		$("#university-row").attr("color", "red");
	}
	if($("#select-courses").val() == "-"){
		correct = 1;
		$("#courses-row").attr("color", "red");
	}
	if($("#select-professor").val() == '-'){
		correct = 1;
		$("#professor-row").attr("color", "red");
	}
	if($("#select-term").val() == '-'){
		correct = 1;
		$("#term-row").attr("color", "red");
	}
	if($("#inputType").val() == "-"){
		correct = 1;
		$("#input-type").addClass("has-error has-feedback");
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
	
	var newType = $("#inputType").val();
	var newDescription = $("#inputDesc").val();
	var newUsername = getCookie("user-name");
	var newUniversity = $("#select-university").val();
	var newCourses = $("#select-courses").val();
	var newProfessor = $("#select-professor").val();
	var newTerm = $("#select-term").val();

	 $.ajax({
	        type: "POST",
	        url: "${pageContext.request.contextPath}/FileServlet",
	        data: {postrequest: "file_request", type : newType, description : newDescription, 
	        	username : newUsername, university : newUniversity, courses : newCourses, professor : newProfessor, term : newTerm  }
	      }).done(function( msg ) {
	    	  window.location.replace("${pageContext.request.contextPath}/index.jsp");
	        }
	      );
}
function resetFields()
{
	$("#university-row").attr("color", "black");
	$("#courses-row").attr("color", "black");
	$("#professor-row").attr("color", "black");
	$("#term-row").attr("color", "black");
	$("#input-type").removeClass("has-error has-feedback");
	$("#error").text("");
}

$(document).ready(function(){
	$("#inputResetForm").click(function(){
		clear();
	});
});
function clear(){
	$("#select-university").val('-');
	$("#select-courses").val('-');
	$("#select-professor").val('-');
	$("#select-term").val('-');
	$("#inputDesc").val('');
	resetFields();
}

</script>
<!-- Footer !!Must be included in every page !! -->
<br><jsp:directive.include file="sitefooter.html" />
</body>
</html>