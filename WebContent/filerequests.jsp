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
		<img align="left" class="img-responsive" alt="Responsive image" src ="${pageContext.request.contextPath}/images/site/man-arm-raised-exclaimation.jpg">
	</div>
	<div class="col-md-8">
		<h3>
			<strong>File Requests</strong>
		</h3>
		<br>	
		<div id="results"></div>
	</div>
	<div class="col-md-2"></div>
</div>

<script>
$(document).ready(function(){
	  listUsers();
});
		
function listUsers()
{
	$.ajax({
        type: "GET",
        url: "${pageContext.request.contextPath}/FileServlet",
        data: {request: "list-requests"}
      }).done(function( msg ) {
         response = $.parseJSON(msg);
          
          //add table element to the view
          $("#results").html("<table id='requests_table' class='table table-bordered table-striped'><tr><td>Username</td><td>Description</td><td>Type</td><td>University</td><td>Course Name</td><td>Professor First Name</td><td>Professor Last Name</td><td>Term</td></tr></table>");
          
         $.each(response, function(key,value) {
         	$("#requests_table").append(
 				"<tr><td>"+value.request_username+"</td><td>"+value.request_desc+"</td><td>"+value.request_type+"</td><td>"+value.request_name+"</td><td>"+value.request_course_name+"</td><td>"+value.request_first_name+"</td><td>"+value.request_last_name+"</td><td>"+value.request_term+"</td></tr>"
         	);
         	
         });
     });
}
</script>

<!-- Footer !!Must be included in every page !! -->
<br><jsp:directive.include file="sitefooter.html" />
</body>
</html>