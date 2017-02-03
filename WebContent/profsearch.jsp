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

<!-- put your lovely page code here. Keep the footer jsp at the bottom of the body!-->
<div data-ng-app="searchApp" data-ng-controller="searchController"> 	   <!--  Angular JS Controller for this element  -->
<div class="row">
	<div class="col-md-3"></div>
	<div class="col-md-4">
		<h1>Professor Search</h1><br>
		<table class = table>
			<tr>
				<td>University</td>
				<td>
				  <select id="select-university">
				    <option>-</option>
					<option data-ng-repeat="item in data">{{item.name}}</option>
				  </select>
				</td>
			</tr>
			<tr>
			<td>Course</td>
				 <td>
				  <select id="select-courses">
				   <option>-</option>
				  </select>
				</td>
			</tr>
			<tr>
				<td>Professor</td>
				<td>
				 <select id="select-professor">
				  <option>-</option>
				  </select>
				 </td>
			</tr>	
		</table>
		<br>
		<div class="row">
			<div class="col-md-3">
			 <button id="search-button" type="button" class="btn btn-default">Submit</button>
			</div>
			<div class="col-md-3">
		      <button id="reset-button" type="button" class="btn btn-primary">Reset</button>
			</div>
		</div>
	</div>
	<div class="col-md-3">
	
	</div>
</div>
<br>
<div class="row">
	<div class="col-md-3">
	
	</div>
	<div class="col-md-7">
		<div class="row">
			<div id ="display">
				
			</div>
		</div>
	</div>
	<div class="col-md-2">
	</div>
</div>


	<br><jsp:directive.include file="sitefooter.html" />

</div>

<!-- Footer !!Must be included in every page !! -->

<script>

var university;
var courses;
var professors;

//event handler binding 
$(function(){
	
	$("#search-button").click(function(){
		submitSearch();
	});
	
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
	  		professors =  $("#select-professor option:selected").text();
		})
  	});
	
}


function submitSearch()
{
	var profid;
	
	$.ajax({
		
   	 	type: "GET",
    	url: "${pageContext.request.contextPath}/SearchServlet",
   		data: {query: "proff-search", school: university, professor: professors}
    	
  	}).done(function( msg ) {	  	
	  	response = $.parseJSON(msg);
	  	profid = response.profid;
	  	window.location.replace("${pageContext.request.contextPath}/rating.jsp?profid='"+profid+"'");	
	  	
  	});
	
	
}

function listFiles()
{
	
	
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


</script>
</body>
</html>