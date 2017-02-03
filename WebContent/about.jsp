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
	<div class="col-md-1"></div>
	<div class="col-md-10">
		<div class="row">
			<h1>About Course Confidential</h1>
			<div class="col-md-4">
				<p>Have you ever wondered what a professors exams look like, what a professors grading scale is like or maybe you missed class and need the notes for that day.
				 Well we have designed Course Confidential to be a wealth of course information with features that will help students be more successful. Course Confidential is a university and college course review website that allows students to rate professors, share course related files and learn details about a course that are usually only available to students that have already taken the course. If you are familiar with the popular professor rating site ratemyprofessor.com, then imagine Course Confidential as ratemyprofessor.com with the added benefit of being able to download files for a specific course such as student notes, course syllabus, and course exams. Many students already rely on rating sites when trying to decide if a course and professor are the right fit for them but have to do additional research to determine whether or not they will truly be able to handle a course. </p>
			</div>
			<div class="col-md-8">
				<img class="img-responsive" alt="Responsive image" src="${pageContext.request.contextPath}/images/site/man-big-right-arm-raised.jpg">
			</div>
		</div>
	</div>
	<div class="col-md-1"></div>
</div>


<!-- Footer !!Must be included in every page !! -->
<br><jsp:directive.include file="sitefooter.html" />
</body>
</html>