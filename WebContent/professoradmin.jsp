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
<div data-ng-app="professorAdminApp" data-ng-controller="professorAdminController"> 	   <!--  Angular JS Controller for this element  -->
<div class="row">
	<div class="col-md-1"></div>
	<div class="col-md-10">
		<div class="row">
			<div class="col-md-5">
				<h1>Hello, Professor {{professor.proffirst}} {{professor.proflast}}</h1>
				<br><br><br>
				<p>Instructions:</p>
				 <ul style="list-style-type:disc">
  		 			<li>Click on "Files" to see a list of course files submitted for a course or to upload your own files</li>
  		 			<li>Click on "Ratings" to see the ratings students have left for a course or to respond to a student rating</li>
		 		</ul>
		  	<br>
		 		<h3>Your Courses</h3>
		 	<br>
		 	
			</div>
			<div class="col-md-4">
				<br><br>
				<div class="row">
					<div id="response-display">
					</div>
				</div>
			</div>
		</div>
		<div class="row">
		  <div class="col-md-5">
		 		<table class="table table-condensed table-striped">
		 			<tr id="{{course.courseid}}" data-ng-repeat="course in list">
		 			   <td><button type="button" class="btn btn-primary btn-sm" onclick="loadfiles($(this))"><span class="glyphicon glyphicon-duplicate" aria-hidden="true"></span>   Files</button></td>
		 			   <td><button type="button" class="btn btn-info btn-sm" onclick="loadratings($(this))"><span class="glyphicon glyphicon-comment" aria-hidden="true"></span>  Ratings</button></td>
		 			   <td><b>{{course.coursecanonical}}</b>: {{course.coursename}}</td>
		 			</tr>
		 		</table>
		 </div>
		 
		 <div id="display" class="col-md-7">
		 </div>
		 	
		</div>
	</div>
	<div class="col-md-1"></div>
</div>
</div>


<!-- Footer !!Must be included in every page !! -->
<br><jsp:directive.include file="sitefooter.html" />
<script>

var profid = <%=request.getParameter("profid")%>;
var ratid;
var ratuserid;
var ratcomment;



var professorAdminApp = angular.module("professorAdminApp", []);
professorAdminApp.controller("professorAdminController", function($scope, $http) {
		
	 $http({
	        url: "${pageContext.request.contextPath}/AdminServlet", 
	        method: "GET",
	        params: {adminrequest: "list-professor-courses", professorid: profid}
	     })
	     .success
	     (
	    		 function(response) 
	    		 {
	    			responseObject = angular.fromJson(response);
	    			$scope.list = responseObject.Profcourses;
	    			$scope.professor = responseObject.Professor;
	   
	    		 }
	    		  
	     );
    
});

function download(obj)
{
	var fileid = obj.parent().parent().attr("id");
	
	var servurl = "${pageContext.request.contextPath}/FileServlet";
	
	
}

function respond(obj)
{
	
	//get the row-parent object of this button
	var ratingrow = obj.parent().parent();
	
	//get rating
	ratid = ratingrow.attr('id');
	ratuserid = 	ratingrow.find(".ratinguser").html();
	ratcomment = ratingrow.find(".ratingcomment").attr('data-content');
	
	$("#response-display").html("<table class='table'><tr><h3>Respond To Rating</h3></tr>");
	$("#response-display").append("<tr><td><strong>Rating ID:    </strong>"+ratid+"</td></tr>");
	
	//user = obj.parent().parent().child();
	$("#response-display").append("<tr><td><strong>User: </strong>   "+ratuserid+"</td></tr>");
	$("#response-display").append("<tr><td><br><p><b>"+ratcomment+"</b></p></td></tr></table>");
	$("#response-display").append("<br>Enter your response below...<br><textarea id='profcomment' rows='5'' cols='80'></textarea>");
	$("#response-display").append("<br><br><button id='submit-prof-response' type='button' class='btn btn-success' onclick='profresponse()'>Submit Response </button>");
	$("#submit-prof-response").click(function(){profresponse});
}
function profresponse()
{
	var profresp = $("#profcomment").val();
	
	$.ajax({
		
   	 	type: "POST",
    	url: "${pageContext.request.contextPath}/RatingServlet",
   		data: {postrequest: "profresp", professorid: profid, ratingid: ratid, presponse: profresp}
    	
  	}).done(function( msg ) {	  	
	  	$("#response-display").html("<br><br><br><br><div class='alert alert-success' role='alert'>Thank You for your response!</div>");
  	});
	
}

function loadratings(obj)
{

	var course = obj.parent().parent().attr('id');
	$.ajax({
		
   	 	type: "GET",
    	url: "${pageContext.request.contextPath}/AdminServlet",
   		data: {adminrequest: "list-professor-rating", professorid: profid, courseid: course}
    	
  	}).done(function( msg ) {	  	
	  	response = $.parseJSON(msg);

	  	$("#display").html("");
	  	 $("#display").append("<h3>Ratings: "+course+"</h3>");
	  	 $("#display").append("<table id='display-table' class='table table-bordered table-condensed'><th>Respond</th><th>Rating Id</th><th>User</th><th>E</th><th>C</th><th>G</th><th>H</th><th>Comment</th></table>");
	  	 $.each(response, function(key,value) {
	  		 
	  		 if(value.israted =="true")
	       	 $("#display-table").append("<tr id='"+value.rating+"'><td><span class='glyphicon glyphicon-download-ok' aria-hidden='true'></span>Responded</td><td>"+value.rating+"</td><td class='ratinguser'>"+value.user+"</td><td>"+value.easiness+"</td><td>"+value.clarity+"</td><td>"+value.grading+"</td><td>"+value.helpfulness+"</td><td>"+value.preview+"<a tabindex='0' role='button' class='btn btn-xs btn-danger ratingcomment' data-trigger='focus' title='Comment' data-toggle='popover'  data-container='body' data-placement='top' title='Popover title' data-content='"+value.comment+"'>More</button></td></tr>");
	  		 else
	       	 $("#display-table").append("<tr id='"+value.rating+"'><td><button type='button' class='btn btn-success btn-xs' aria-label='Left Align' onclick='respond($(this))''><span class='glyphicon glyphicon-download-alt' aria-hidden='true'></span>   Respond</button></td><td>"+value.rating+"</td><td class='ratinguser'>"+value.user+"</td><td>"+value.easiness+"</td><td>"+value.clarity+"</td><td>"+value.grading+"</td><td>"+value.helpfulness+"</td><td>"+value.preview+"<a tabindex='0' role='button' class='btn btn-xs btn-danger ratingcomment' data-trigger='focus' title='Comment' data-toggle='popover'  data-container='body' data-placement='top' title='Popover title' data-content='"+value.comment+"'>More</button></td></tr>");

	  	 });
	  	 
	  	$(function () {
	  	  $('[data-toggle="popover"]').popover()
	  	})
  	});

}

function loadfiles(obj)
{
	
	var course = obj.parent().parent().attr('id');
	$.ajax({
		
   	 	type: "GET",
    	url: "${pageContext.request.contextPath}/AdminServlet",
   		data: {adminrequest: "list-professor-files", professorid: profid, courseid: course}
    	
  	}).done(function( msg ) {	  	
	  	response = $.parseJSON(msg);
	  	 $("#display").html("");
	  	 $("#display").append("<h3>Files: "+course+"</h3>");
	  	 $("#display").append("<table id='display-table' class='table table-bordered table-condensed'><th>Download</th><th>File Name</th><th>Pages</th><th>Description</th></table>");
	  	 $.each(response, function(key,value) {
       	 	$("#display-table").append("<tr id="+value.uuid+"><td><a href=${pageContext.request.contextPath}/FileServlet?request=download&ID="+value.uuid+" class='btn btn-success btn-xs' aria-label='Left Align' onclick='download($(this))'><span class='glyphicon glyphicon-download-alt' aria-hidden='true'></span>   Download</a></td><td>"+value.name+"</td><td>"+value.pages+"</td><td>"+value.description+"</td></tr>");
       	});
	  	$("#display").append("<br><a href='upload.jsp' type='button' class='btn btn-warning'>Submit File</button>");
  	});

}
</script>
</body>
</html>