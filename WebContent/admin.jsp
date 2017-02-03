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

<div class="row">
	<div class="col-md-1">
	</div>
	<!-- view port -->
	<div id="result-pane"class="col-md-8 col-md-offset-1">
		<h1 style="text-align: center;">Administration Console</h1>
	</div>
	<div class="col-md-1">
	</div>
</div>	

<div class="row">
	<div class="col-md-1">
	</div>
	<div class="col-md-1">
	   <div class="btn-group-vertical" role="group" aria-label="...">
  		<button id="list-tasks-button" type="button" class="btn btn-info btn-block">Tasks</button>
  		<button id="search-users-button" type="button" class="btn btn-info btn-block">Search User</button>
  		<button id="list-users-button" type="button" class="btn btn-info btn-block">List All Users</button>
  		<button id="unlock-users-button" type="button" class="btn btn-info btn-block">Unlock Account</button>
  		<button id="suspend-users-button" type="button" class="btn btn-info btn-block">Suspend Account</button>
  		<button id="remove-files-button" type="button" class="btn btn-info btn-block">Remove File</button>
  		<button id="remove-comments-button" type="button" class="btn btn-info btn-block">Remove Comment</button>
	  </div>
	</div>
	
	<!-- view port -->
	<div id="resultpane"class="col-md-8 col-md-offset-1">
	</div>
	<div class="col-md-1">
	</div>
</div>	

<!-- Footer !!Must be included in every page !! -->
</body>
<script>

var id;
var mail;
var user;

$(document).ready(function(){
  $("#list-users-button").click(function(){listUsers();});
  $("#search-users-button").click(function(){initUserSearch();});
  $("#suspend-users-button").click(function(){initUserSearch();});
  $("#unlock-users-button").click(function(){initUserSearch();});
  $("#list-tasks-button").click(function(){listTasks();});
  $("#remove-files-button").click(function(){initRemoveFile();});
  $("#remove-comments-button").click(function(){initRemoveComment();});

	   });
  


function initRemoveFile()
{
	 $("#resultpane").html("<h3>Enter the File ID of the File to be deleted</h3><br>File ID: <input id='fileid' type='text'><br><br><button id='submit-user-search' class='btn btn-warning' onclick='searchFile()'>Seach File</button>");
}

function initRemoveComment()
{
	
	 $("#resultpane").html("<h3>Enter the Comment ID of the Comment to be deleted</h3><br>Comment ID: <input id='commentid' type='text'><br><br><button id='submit-user-search' class='btn btn-warning' onclick='searchComment()'>Seach Comment</button>");

}

function searchComment(commentid)
{
	var  commentid = $("#commentid").val();
	
	
	$.ajax({
        type: "GET",
        url: "${pageContext.request.contextPath}/AdminServlet",
        data: {adminrequest: "search-comment" , id: commentid}
      }).done(function( msg ) {
         response = $.parseJSON(msg);
         
           
          $("#resultpane").html("<table id='comments_table' class='table table-bordered table-striped'><tr><td>Button</td><td>Rating Id</td><td>Course Name</td><td>Username</td><td>Comment</td></tr></table>");
         
 		 $.each(response, function(key,value) {
        	 
 			 $("#comments_table").append(
  	 				"<tr><td id="+value.ratingid+"><button id='remove-comments-button' class='btn btn-warning' onclick='removeComment($(this))'><span class='glyphicon glyphicon-remove' aria-hidden='true'></span>     Remove</button><td>"+value.ratingid+"</td><td>"+value.coursename+"</td><td>"+value.username+"</td><td>"+value.comment+"</td></tr>"
  	         	);
 	       //$("#remove-comments-button").click(function(){removeComment($(this))});
         });
 		 
     });
}

function removeComment(obj)
{
	var commentid = obj.parent().attr('id');
		
	$.ajax({
        type: "POST",
        url: "${pageContext.request.contextPath}/AdminServlet",
        data: {adminrequest: "remove-comment", id: commentid}
      }).done(function( msg ) {
         $("#"+commentid).html("Comment Removed");
     });
	
}

function removeFile(obj)
{
	var fileid = obj.parent().attr('id');
	
	$.ajax({
        type: "POST",
        url: "${pageContext.request.contextPath}/AdminServlet",
        data: {adminrequest: "remove-file", id: fileid}
      }).done(function( msg ) {
         $("#"+fileid).html("File Removed");
     });
	
}


function searchFile(fileid)
{
	var fileid = $("#fileid").val();
	
	$.ajax({
        type: "GET",
        url: "${pageContext.request.contextPath}/AdminServlet",
        data: {adminrequest: "search-file" , id: fileid}
      }).done(function( msg ) {
         response = $.parseJSON(msg);
    
         
          $("#resultpane").html("<table id='files_table' class='table table-bordered table-striped'><tr><td></td><td>id</td><td>category</td><td>name</td><td>description</td><td>type</td><td>pages</td><td>size</td></tr></table>");
         
 		 $.each(response, function(key,value) {
        	 
 			 $("#files_table").append(
  	 				"<tr><td id="+value.uuid+"><button id='remove-file-button' class='btn btn-warning'><span class='glyphicon glyphicon-remove' aria-hidden='true'></span>     Remove</button><td>"+value.uuid+"</td><td>"+value.category+"</td><td>"+value.name+"</td><td>"+value.description+"</td><td>"+value.type+"</td><td>"+value.pages+"</td><td>"+value.size+"</td></tr>"
  	         	);
 	        $("#remove-file-button").click(function(){removeFile($(this))});
         });
 		 
     });
}

function listTasks()
{
	$.ajax({
        type: "GET",
        url: "${pageContext.request.contextPath}/AdminServlet",
        data: {adminrequest: "list-tasks"}
      }).done(function( msg ) {
         response = $.parseJSON(msg);
         
          $("#resultpane").html("<table id='users_table' class='table table-bordered table-striped'><tr><td>Task Number</td><td>Task</td><td>User</td><td>Requester</td><td>Requester Email</td><td>Description</td></tr></table>");
         
 		 $.each(response, function(key,value) {
        	 
 	         $("#users_table").append(
 	 				"<tr><td>"+value.taskid+"</td><td>"+value.task+"</td><td>"+value.username+"</td><td>"+value.name+"</td><td>"+value.email+"</td><td>"+value.description+"</td></tr>"
 	         	);
         });
         
     });
	
}


function initUserSearch()
{
	   $("#resultpane").html(
	   "<table class='table'><tr><td><input id='user-email' type='text' placeholder='Or User email'></td><td><input id='user-name' type='text' placeholder='Or username'></td></tr></table>"+
	   "<br><button id='submit-user-search' class='btn btn-warning'>Search</button><br><br><br><div id='user-search-results'></div>"
	    );
	   
	   
	   
	   $("#submit-user-search").click(function(){searchUser();});
}

function activateUser(userId)
{
	$.ajax({
        type: "GET",
        url: "${pageContext.request.contextPath}/AdminServlet",
        data: {adminrequest: "unlock-user", userid: userId}
      }).done(function( msg ) {
         response = $.parseJSON(msg);
         
         //a little bit of sneaky codefu - we assume the user is suspended and re-call the search with the current userid to refresh the table
         id = userId;
         searchUser();
         
     });
}


//suspends user by id
function suspendUser(userId)
{
	$.ajax({
        type: "GET",
        url: "${pageContext.request.contextPath}/AdminServlet",
        data: {adminrequest: "suspend-user", userid: userId}
      }).done(function( msg ) {
         response = $.parseJSON(msg);
         
         //a little bit of sneaky codefu - we assume the user is suspended and re-call the search with the current userid to refresh the table
         id = userId;
         searchUser();
         
     });
}

function searchUser()
{
	
	id = $("#user-id").val();
	mail = $("#user-email").val();
	user = $("#user-name").val();
	
	$.ajax({
        type: "GET",
        url: "${pageContext.request.contextPath}/AdminServlet",
        data: {adminrequest: "search-user", userid: id, email: mail, username: user}
      }).done(function( msg ) {
         response = $.parseJSON(msg);
          
          //add table element to the view
          $("#user-search-results").html("<table id='users_table' class='table table-bordered table-striped'><tr><td>Action</td><td>Status</td><td>User Id</td><td>Role</td><td>Username</td><td>Email</td><td>First</td><td>Last</td><td>Login Failures</td></tr></table>");
          
         $.each(response, function(key,value) {
        	 
        	 //if the user is  active we dont want a suspend button
        	if(value.user_status=='ACTIVE'){
         		$("#users_table").append(
 					"<tr><td><button id='suspend-button' class='btn btn-warning'><span class='glyphicon glyphicon-alert' aria-hidden='true'></span>  Suspend</button></td><td>"+value.user_status+"</td><td class='userid'>"+value.user_id+"</td><td>"+value.user_role+"</td><td>"+value.user_name+"</td><td>"+value.user_email+"</td><td>"+value.user_first+"</td><td>"+value.user_last+"</td><td>"+value.user_login+"</td></tr>"
         		);
         	
         		//bind event handler to button click, search for user id class and retrieve value from the td that has it
            	$("#suspend-button").click(function(){
            		user = $(this).parent().siblings(".userid").html();
            		suspendUser(user);
            	})
        	}else if(value.user_status=='SUSPENDED'){
        		$("#users_table").append(
     					"<tr><td><button id='suspend-unlock' class='btn btn-success'><span class='glyphicon glyphicon-ok' aria-hidden='true'></span>  Unlock</button></td><td>"+value.user_status+"</td><td class='userid'>"+value.user_id+"</td><td>"+value.user_role+"</td><td>"+value.user_name+"</td><td>"+value.user_email+"</td><td>"+value.user_first+"</td><td>"+value.user_last+"</td><td>"+value.user_login+"</td></tr>"
             		);
             	
             		//bind event handler to button click, search for user id class and retrieve value from the td that has it
                	$("#suspend-unlock").click(function(){
                		user = $(this).parent().siblings(".userid").html();
                		activateUser(user);
                	})
        	}
         });
         

     });
}

//list all users 
function listUsers()
{
	$.ajax({
        type: "GET",
        url: "${pageContext.request.contextPath}/AdminServlet",
        data: {adminrequest: "list-users"}
      }).done(function( msg ) {
         response = $.parseJSON(msg);
          
          //add table element to the view
          $("#resultpane").html("<table id='users_table' class='table table-bordered table-striped'><tr><td>Status</td><td>User Id</td><td>Role</td><td>Username</td><td>Email</td><td>First</td><td>Last</td><td>Login Failures</td></tr></table>");
          
         $.each(response, function(key,value) {
         	$("#users_table").append(
 				"<tr><td>"+value.user_status+"</td><td>"+value.user_id+"</td><td>"+value.user_role+"</td><td>"+value.user_name+"</td><td>"+value.user_email+"</td><td>"+value.user_first+"</td><td>"+value.user_last+"</td><td>"+value.user_login+"</td></tr>"
         	);
         	
         });
     });
}
</script>
</html>