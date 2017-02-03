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
<div data-ng-app="courseApp" data-ng-controller="courseController">
<div class="row">
	<div class="col-md-2"></div>
	
	<!--  main page container -->
	<div class="col-md-9">
	  <h1>{{data.filename}}</h1><br>
		<div class="row">
		  <div class="col-md-8">
		  	 <table class="table">
		  		 <tr><td><strong>Course:</strong></td><td >{{data.coursename}}</td></tr>
			     <tr><td><strong>Term:</strong></td><td >{{data.term}}</td></tr>
			     <tr><td><strong>Professor:</strong></td><td>{{data.proffirst}}  {{data.proflast}}</td></tr>
			     <tr><td><strong>University:</strong></td><td>{{data.uniname}}</td></tr>
			     <tr><td><strong>Size:</strong></td><td>{{data.filesize}} KB</td></tr>
			     <tr><td><strong>Description:</strong></td><td>{{data.descrip}}</td></tr>
			     <tr><td><strong>Votes</strong></td><td><button id="upvote" type="button" class="btn btn-success" onclick='upvote($(this))'><span class="glyphicon glyphicon-circle-arrow-up" aria-hidden="true">   {{data.upvotes}}</span></button>
			     	<button id="downvote" type="button" class="btn btn-danger" onclick='downvote($(this))'><span class="glyphicon glyphicon-circle-arrow-down" aria-hidden="true">   {{data.downvotes}}</span></button></td></tr>
		     </table>
		     <p id="vote-notifier" class="bg-danger"></p>
		     <br>
		     
		     <table class="table">
		      <tr>
		        <td>
		         <button id="download" type="button" onclick="download()" class="btn btn-success btn-sm" aria-label="Left Align"><span class="glyphicon glyphicon-download-alt" aria-hidden="true"></span>   Download</button>
			   	 <small><a href="${pageContext.request.contextPath}/contactadmin.jsp">Report this File</a></small></td>
			  </tr>
			 </table>
			 <br>
			 
			 <h4><strong>Related Files:</strong></h4>
			 <table id="related" class="table">
			 </table>
		  </div>
		  <div id="fileimage" class="col-md-3">
		  		  	<img class='img-responsive' alt='Responsive image' src ="${pageContext.request.contextPath}/FileServlet?request=image&uuid=<%=request.getParameter("uuid")%>">
		  </div>
		</div>
	</div>
</div>
</div>

<script>
var uuid = <%=request.getParameter("uuid")%>;
var theusername;
var currentrating;
var response;
	
function getRelated(){
	$.ajax({
        type: "GET",
        url: "${pageContext.request.contextPath}/FileServlet",
        data: {request: "getRelated", uuid : uuid}
      }).done(function( msg ) {
    	  response = $.parseJSON(msg);
    	  $.each(response, function(key,value) {
 	  		 $("#related").append("<tr><td><a href='file.jsp?uuid=&#39;"+value.id+"&#39;'>"+value.name+"</a></td><td><strong>Pages:</strong></td><td>"+value.pages+"</td><td><strong>Size:</strong></td><td>"+value.size+"</td><td><button id='' type='button' class='btn btn-success' onclick='upvote($(this))'><span class='glyphicon glyphicon-circle-arrow-up' aria-hidden='true'>     "+value.upvotes+"</span></button></td><td><button id='' type='button' class='btn btn-danger' onclick='downvote($(this))'><span class='glyphicon glyphicon-circle-arrow-down' aria-hidden='true'>     "+value.downvotes+"</span></button></td></tr>");
 	  	 });
        }
      );
}


function upvote(id){
	
	//get the span child of this button
	var spanObj = id.find("span"); 
	
	currentrating = spanObj.html(); //get current rating count
	
	//get the username and ratingid of the thing
	theusername = getCookie("user-name");
	$.ajax({
        type: "POST",
        url: "${pageContext.request.contextPath}/RatingServlet",
        data: {postrequest: "updateVotes", username : theusername, isUpvote : "true", ratingid : uuid}
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
	
	//get the username and ratingid of the thing
	theusername = getCookie("user-name");	
	$.ajax({
        type: "POST",
        url: "${pageContext.request.contextPath}/RatingServlet",
        data: {postrequest: "updateVotes", username : theusername, isUpvote : "false", ratingid : uuid}
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

function checkCookies(){
	if(checkCookie("user-type") == 1){
		if(usertype == "guest"){
			return 1;	
		}
		return 0;
	}
}

function download()
{
	var checkguest = checkCookies();
	if(!checkguest){
		window.location.replace("${pageContext.request.contextPath}/FileServlet?request=download&ID='"+uuid+"'");
	}
}

var courseApp = angular.module("courseApp", []);
courseApp.controller("courseController", function($scope, $http){
		
	 $http({
	        url: "${pageContext.request.contextPath}/FileServlet", 
	        method: "GET",
	        params: {request: "file", theuuid: uuid}
	     })
	     .success
	     (
	    		 function(response) 
	    		 {
	    			responseObject = angular.fromJson(response);
	    			console.log(responseObject);
	    			$scope.data = responseObject;
	    		 });
		getRelated();

});

</script>

<!-- Footer !!Must be included in every page !! -->
</body>
</html>