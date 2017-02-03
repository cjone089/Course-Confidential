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
	<div class="col-md-2">
		<img class="img-responsive" alt="Responsive image" src ="${pageContext.request.contextPath}/images/site/man-big-left-arm-raised.jpg">
	</div>
	<div class="col-md-2">
		<table class="table">
			<tr>
			 <td>
				<a href="rating.jsp"><img class="img-responsive" alt="Responsive image" src ="${pageContext.request.contextPath}/images/site/computer-professor.jpg"></a>
			</td>
			</tr>
			<tr>
				<td><p class="text-center"><strong>Rate a Professor</strong></p></td>
			</tr>
		</table>
	</div>
	<div class="col-md-2">
		<table class="table">
			<tr>
			 <td>
			  <a href="search.jsp"><img class="img-responsive" alt="Responsive image" src ="${pageContext.request.contextPath}/images/site/course-icon.jpg"></a>
			</td>
			</tr>
			<tr>
				<td><p class="text-center"><strong>Search for A Course</strong></p></td>
			</tr>
		</table>
	</div>
		<div class="col-md-2">
		<table class="table">
			<tr>
			 <td>
			<a href="upload.jsp"><img class="img-responsive" alt="Responsive image" src ="${pageContext.request.contextPath}/images/site/file-upload-computer.jpg"></a>
			</td>
			</tr>
			<tr>
				<td><p class="text-center"><strong>Upload a File</strong></p></td>
			</tr>
		</table>
	</div>
	
	<div class="col-md-1">
	</div>
</div>
</body>
</html>