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


<div data-ng-app="ratingApp" data-ng-controller="ratingController"> 	   <!--  Angular JS Controller for this element  -->
<!-- --------------Main Container--------------------- -->
<div class="row">
    <!-- left spacer -->
	<div class="col-md-2"></div>
	
	<!-- center content -->
	<div class="col-md-8">
	
	<!-- professor info box -->
	 <div class="row">
	   <div class="col-md-12">
	   	<h1>{{data.Professor.professorfirst}} {{data.Professor.professorlast}}</h1><br>
	   </div>
	 </div>
	 <div class="row">
		<div id="professor-img" class="col-md-3"><img class="img-responsive" alt="Responsive image" src ="${pageContext.request.contextPath}/images/site/professor-unknown.jpg"></div>
		<div class="col-md-6">
			<table class="table">
			  <tr><td><h5>{{data.Professor.university}}</h5></td></tr>
			  <tr><td><h5>{{data.Professor.department}}</h5></td></tr>
			  <tr>
			  	<td><br><button type="button" class="btn btn-primary btn-lg" aria-label="Left Align"><span class="glyphicon glyphicon-edit" aria-hidden="true"></span>   Rate Professor</button></td>
			  </tr>
			</table>
		</div>
	</div>
	<!-- professor ratings box -->
	 <div class="row">
	   <div class="col-md-3">
	   <br><br><br><br><h3>Overall Rating</h3><br><h1><b>{{data.Overall.ooverall}}</b></h1></div>
	   <br><br><br><br><br>
	   <!--  professor ratings bars -->
	   <div class="col-md-6">
	   <span class="label label-default">Easiness</span>
		<div class="progress">
  			<div class="progress-bar progress-bar-success progress-bar-striped" role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100" style="width: {{data.Overall.oeasiness}}%">
    			<span class="sr-only">40% Complete (success)</span>
  			</div>
		</div>
	   <span class="label label-default">Clarity</span>
		<div class="progress">
  			<div class="progress-bar progress-bar-info progress-bar-striped" role="progressbar" aria-valuenow="20" aria-valuemin="0" aria-valuemax="100" style="width: {{data.Overall.oclarity}}%">
    			<span class="sr-only">20% Complete</span>
  			</div>
		</div>
	   <span class="label label-default">Helpfulness</span>
		<div class="progress">
  			<div class="progress-bar progress-bar-warning progress-bar-striped" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width: {{data.Overall.ohelp}}%">
    			<span class="sr-only">60% Complete (warning)</span>
  			</div>
		</div>
	  <span class="label label-default">Grading</span>
		<div class="progress">
  			<div class="progress-bar progress-bar-danger progress-bar-striped" role="progressbar" aria-valuenow="80" aria-valuemin="0" aria-valuemax="100" style="width: {{data.Overall.ograding}}%">
    			<span class="sr-only">80% Complete (danger)</span>
  			</div>
		</div>
	   </div>
	   <div class="col-md-3"></div>
	 </div>
	 
	 
	 
	 
	 
	 <!-- Comments and Ratings Box -->
	 <div class="row" data-ng-repeat="item in list">
	   <div class="col-md-2">
	   <br><br><br><br><br><h1><b>{{item.overall}}</b></h1></div>
	   <br><br><br><br><br>
	   <!--  professor ratings bars -->
	   <div class="col-md-4">
	   <span class="label label-default">Easiness</span>
		<div class="progress">
  			<div class="progress-bar progress-bar-active" role="progressbar" aria-valuenow="{{item.easiness}}" aria-valuemin="0" aria-valuemax="100" style="width: {{item.easiness}}%">
    			<span class="sr-only">{{item.easiness}}% Complete (success)</span>
  			</div>
		</div>
	   <span class="label label-default">Clarity</span>
		<div class="progress">
  			<div class="progress-bar progress-bar-active" role="progressbar" aria-valuenow="{{item.clarity}}" aria-valuemin="0" aria-valuemax="100" style="width: {{item.clarity}}%">
    			<span class="sr-only">{{item.clarity}}% Complete</span>
  			</div>
		</div>
	   <span class="label label-default">Helpfulness</span>
		<div class="progress">
  			<div class="progress-bar progress-bar-active" role="progressbar" aria-valuenow="{{item.helpfulness}}" aria-valuemin="0" aria-valuemax="100" style="width: {{item.helpfulness}}%">
    			<span class="sr-only">{{item.helpfulness}}% Complete (warning)</span>
  			</div>
		</div>
	  <span class="label label-default">Grading</span>
		<div class="progress">
  			<div class="progress-bar progress-bar-active" role="progressbar" aria-valuenow="{{item.grading}}" aria-valuemin="0" aria-valuemax="100" style="width: {{item.grading}}%">
    			<span class="sr-only">{{item.grading}}% Complete (danger)</span>
  			</div>
		</div>
	   </div>
	   <div class="col-md-6">
	   	<div class="panel panel-primary">
	   	 <div class="panel-heading"><h3 class="panel-title">{{item.course}}</h3></div>
	     <div class="panel-body">
	         <p><b>{{item.user}}</b><br>{{item.comment}}</p>
	         <br><p><em>{{item.profresp}}</em></p>{{item.signature}}
	     </div>
	    </div>
	    <br>
	    <div id="{{item.id}}" data-alt-src="{{item.id}}">
	    	<button id="upvotebutton" type="button" class="btn btn-success" onclick='upvote($(this))'><span class="glyphicon glyphicon-circle-arrow-up" aria-hidden="true">    {{item.upvote}}</span></button>    
	    	<button id="downvotebutton" type="button" class="btn btn-danger" onclick='downvote($(this))'><span class="glyphicon glyphicon-circle-arrow-down" aria-hidden="true">    {{item.downvote}}</span></button>
	    	<button id="flag-button" onclick="report()" type="button" class="btn btn-default"><span class="glyphicon glyphicon-flag" aria-hidden="true">    </span> Report Rating</button>
            <p id="vote-notifier" class="bg-danger"></p></div>   
	   </div>
	 </div>
	
	
	
	
	
	
	
	
	<!-- right spacer -->
	<div class="col-md-3"></div>
	</div>
</div>
</div>

<script>
var profid = <%=request.getParameter("profid")%>;
var theusername;
var theratingid;
var currentrating;
var response;

$(document).ready(function(){
	checkUser();
	
});

function report(){
	window.location.replace("${pageContext.request.contextPath}/contactadmin.jsp");
}

function checkUser()
{
	$('.btn-primary').hide();
}

function upvote(id){
	//get the span child of this button
	var spanObj = id.find("span"); 
	
	currentrating = spanObj.html(); //get current rating count
	theratingid = id.parent().attr('id');
	
	//get the username and ratingid of the thing
	theusername = getCookie("user-name");	
	
	$.ajax({
        type: "POST",
        url: "${pageContext.request.contextPath}/RatingServlet",
        data: {postrequest: "updateVotes", username : theusername, isUpvote : "true", ratingid : theratingid}
      }).done(function( msg ) {
    	  response = $.parseJSON(msg);
    	  if(response.votestatus =="success")
          {
    		spanObj.html(response.current);  
          }
    	  else{
    		  $("#vote-notifier").html("<strong>Error: You have already voted and you may not vote again...</strong>");
    	  }
        }
      );
}

function downvote(id){
	//get the span child of this button
	var spanObj = id.find("span"); 
	
	currentrating = spanObj.html(); //get current rating count
	theratingid = id.parent().attr('id');
	
	//get the username and ratingid of the thing
	theusername = getCookie("user-name");	
	$.ajax({
        type: "POST",
        url: "${pageContext.request.contextPath}/RatingServlet",
        data: {postrequest: "updateVotes", username : theusername, isUpvote : "false", ratingid : theratingid}
      }).done(function( msg ) {
    	  response = $.parseJSON(msg);
    	  if(response.votestatus =="success")
          {
    		spanObj.html(response.current);  
          }
    	  else{
    		  $("#vote-notifier").html("<strong>Error: You have already voted and you may not vote again...</strong>");
    	  }
        }
      );
}

//Angular JS Controller code abandon all hope ye who read here
var responseObject;

var ratingApp = angular.module("ratingApp", []);
ratingApp.controller("ratingController", function($scope, $http) {
			
	 $http({
	        url: "${pageContext.request.contextPath}/RatingServlet", 
	        method: "GET",
	        params: {request: "ratingsearch", professorid: profid}
	     })
	     .success
	     (
	    		 function(response) 
	    		 {
	    			responseObject = angular.fromJson(response);
	    			$scope.data = responseObject;
	    			$scope.list = responseObject.Ratings;
	   
	    		 } 		  
	     );
        checkUser();
      
});


</script>

<!-- Footer !!Must be included in every page !! -->
</body>
</html>