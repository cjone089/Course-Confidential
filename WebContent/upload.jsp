<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<!-- Site Header and CDN Files !!Must be include in every page!! -->
<jsp:directive.include file="header.html" />
<title>Upload File to Course Confidential</title>
</head>


<body>
	<!-- Navigation Bar !!Must be included in every page !! -->
	<jsp:directive.include file="sitenavigation.html" />


	<!-- --------------Main Container--------------------- -->
	<div data-ng-app="uploadApp" data-ng-controller="uploadController">
		<div class="row">
			<div class="col-md-3"></div>
			<div class="col-md-6">
				<h1>
					<em>Upload a File</em>
				</h1>
				<br>
				<table class="table" id="fileinfo">
					<tr>
						<td><strong><em>File Name</em></strong></td>
						<td><input id="filename" type="text" placeholder="-"></td>
					</tr>
					<tr>
						<td><strong><em>Category</em></strong></td>
						<td><select id="filecategory">
								<option>-</option>
								<option>Syllabus</option>
								<option>Notes</option>
								<option>Exam</option>
						</select></td>
					</tr>
					<tr>
						<td><strong><em>University</em></strong></td>
						<td><select id="select-university">
								<option>-</option>
								<option data-ng-repeat="item in data">{{item.name}}</option>
						</select></td>
					</tr>
					<tr>
						<td><strong><em>Course</em></strong></td>
						<td><select id="select-courses">
								<option>-</option>
						</select></td>
					</tr>
					<tr>
						<td><strong><em>Professor</em></strong></td>
						<td><select id="select-professor">
								<option>-</option>
						</select></td>
					</tr>
					<tr>
						<td><strong><em>Term</em></strong></td>
						<td><select id="select-term">
								<option>-</option>
						</select></td>
					</tr>
					<tr>
						<td><strong><em>No. of Pages</em></strong></td>
						<td><input id="filepages" type="text" placeholder="-"></td>
					</tr>
					<tr>
						<td><strong><em>Type</em></strong></td>
						<td><select id="select-type">
								<option>-</option>
								<option>doc</option>
								<option>pdf</option>
								<option>Image</option>
							</select>
						</td>
					</tr>
				</table>

				<p>
					Please specify a file:<br> <input type="file" id="filedata" name="filedata">
				</p>
				<br>

				<p>
					File Thumbnail:<br> <input type="file" id="filescreenshot" name="filescreenshot">
				</p>
				<br>

				<p>
					<strong><em>File Description</em></strong>
				</p>
				<textarea id="filedescription" rows="4" cols="50"></textarea>
				<br>

				<button id="submit" type="button" class="btn btn-warning">Submit</button>
				<button id="cancel" type="button" class="btn btn-default">Cancel</button>
			</div>
			<div class="col-md-3">
				<img class="img-responsive" alt="Responsive image"
					src="${pageContext.request.contextPath}/images/site/man-big-right-arm-raised.jpg">
			</div>
		</div>
	</div>

	<!-- Footer !!Must be included in every page !! -->
	<br><jsp:directive.include file="sitefooter.html" />

</body>
<script>
	var university;
	var courses;
	var professors;
	$(document).ready(function(){
		checkCookies();
		initialize();
	});
	
	function initialize(){
		//bind to the university select, detect when it changes, and then get the value
		$("#select-university").on('change', function() {
			university = $("option:selected", this);
			university = this.value;
			fillForm(university);
		})
		$("#submit").click(function(){
			submitFile();
		});
		$("#cancel").click(function(){
			location.reload();
		});
	}

	function checkCookies(){
		if(checkCookie("user-type") == 1){
			if(usertype == "guest"){
				window.location.replace("${pageContext.request.contextPath}/index.jsp");	
			}
		}
	}
	
	function submitFile(){
		var jForm = new FormData();
		
		jForm.append("name", $("#filename").val());
		jForm.append("university", $("#select-university").val());
		jForm.append("course", $("#select-courses").val());
		jForm.append("pages", $("#filepages").val());
		jForm.append("category", $("#filecategory").val());
		jForm.append("professor", $("#select-professor").val());
		jForm.append("term", $("#select-term").val());
		jForm.append("description", $("#filedescription").val());
		jForm.append("type", $("#select-type").val())
		jForm.append("postrequest","addFile");
		jForm.append("file", $('#filedata').get(0).files[0]);
		jForm.append("filescreenshot", $('#filescreenshot').get(0).files[0]);
				
		$.ajax({
			type: "POST",
			url: "${pageContext.request.contextPath}/FileServlet?postrequest=addFile",
			data: jForm,
			mimeType: "multipart/form-data",
            contentType: false,
            cache: false,
            processData: false,
	      }).done(function( msg ) {
	    	  window.location.replace("${pageContext.request.contextPath}/index.jsp");	
	        }
	      );
	}
	
	//gets the necessary data to fill parts of the form
	function fillForm(uni) {
		var test;
		$.ajax({

			type : "GET",
			url : "${pageContext.request.contextPath}/SearchServlet",
			data : {
				query : "list-all-courses",
				school : uni
			}

		}).done(
				function(msg) {
					response = $.parseJSON(msg);

					$.each(response, function(key, value) {
						$("#select-courses").append(
								"<option>" + value.name + "</option>")

					})

					$("#select-courses").on('change', function() {
						listProfessor();
					})
				});

	}

	//fills the professor list
	function listProfessor(courseObj) {
		courses = $("#select-courses option:selected").text();

		$.ajax({

			type : "GET",
			url : "${pageContext.request.contextPath}/SearchServlet",
			data : {
				query : "list-professor",
				school : university,
				course : courses
			}

		}).done(
				function(msg) {
					response = $.parseJSON(msg);

					$.each(response, function(key, value) {
						$("#select-professor").append(
								"<option>" + value.professor + "</option>")
					})

					$("#select-professor").on('change', function() {
						listTerm();
					})
				});

	}

	function listTerm() {
		professors = $("#select-professor option:selected").text();

		$.ajax({

			type : "GET",
			url : "${pageContext.request.contextPath}/SearchServlet",
			data : {
				query : "list-term",
				school : university,
				course : courses,
				professor : professors
			}

		}).done(function(msg) {
			response = $.parseJSON(msg);

			$.each(response, function(key, value) {
				$("#select-term").append("<option>" + value.term + "</option>")
			})

			$("#select-term").on('change', function() {
				$("#search-button").removeClass("btn-default");
				$("#search-button").addClass("btn-success");
			})
		});

	}

	//Angular JS Controller code abandon all hope ye who read here
	var responseObject;

	var searchApp = angular.module("uploadApp", []);
	searchApp.controller("uploadController", function($scope, $http) {

		$http({
			url : "${pageContext.request.contextPath}/SearchServlet",
			method : "GET",
			params : {
				query : "list-all-universities"
			}
		}).success(function(response) {
			responseObject = angular.fromJson(response);
			$scope.data = responseObject;

		}

		);

	});
</script>
</html>