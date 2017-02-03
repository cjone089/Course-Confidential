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


<div data-ng-app="ratingsApp" data-ng-controller="ratingsController">
	<div class="row">
		<div id="colA" class="col-md-2"></div>

		<div id="colB" class="col-md-8">
			<div class="row">
				<div class="col-md-3">
					<img class="img-responsive" alt="Responsive image" src ="${pageContext.request.contextPath}/images/site/man-arm-raised-exclaimation.jpg">
				</div>
				<div class="col-md-6">
					<h1 >{{data.proff_first}} {{data.proff_last}}</h1>
					<h4>{{data.university}}</h4>
					<br>
					<table class="table" id="rate">
						<tr>
							<td><strong><em>Course Number</em></strong></td>
							<td><select id="select-course">
									<option>-</option>
									<option data-ng-repeat="professor in courses">{{professor.course_canonical}}</option>
							</select></td>
						</tr>
						<tr>
							<td><strong><em>Semester</em></strong></td>
							<td><select id="select-term">
								 <option>-</option>
								 <option>Spring</option>
								 <option>Summer</option>
								 <option>Fall</option>
							</select></td>
						</tr>
						<tr>
							<td><strong><em>Year</em></strong></td>
							<td><select id="select-year">
								 <option>-</option>
								 <option>2014</option>
								 <option>2015</option>
							</select></td>
						</tr>
						<tr>
							<td><strong><em>Helpfulness</em></strong></td>
							<td> </td>
							<td><input type="radio" name="helpfulness" value="1"><small>1</small></td>
							<td><input type="radio" name="helpfulness" value="2"><small>2</small></td>
							<td><input type="radio" name="helpfulness" value="3"><small>3</small></td>
							<td><input type="radio" name="helpfulness" value="4"><small>4</small></td>
							<td><input type="radio" name="helpfulness" value="5"><small>5</small></td>
						</tr>
						<tr>
							<td><strong><em>Easiness</em></strong></td>
							<td> </td>
							<td><input type="radio" name="easiness" value="1"><small>1</small></td>
							<td><input type="radio" name="easiness" value="2"><small>2</small></td>
							<td><input type="radio" name="easiness" value="3"><small>3</small></td>
							<td><input type="radio" name="easiness" value="4"><small>4</small></td>
							<td><input type="radio" name="easiness" value="5"><small>5</small></td>
						</tr>
						<tr>
							<td><strong><em>Clarity</em></strong></td>
							<td> </td>
							<td><input type="radio" name="clarity" value="1"><small>1</small></td>
							<td><input type="radio" name="clarity" value="2"><small>2</small></td>
							<td><input type="radio" name="clarity" value="3"><small>3</small></td>
							<td><input type="radio" name="clarity" value="4"><small>4</small></td>
							<td><input type="radio" name="clarity" value="5"><small>5</small></td>
						</tr>
						<tr>
							<td><strong><em>Grading</em></strong></td>
							<td> </td>
							<td><input type="radio" name="grading" value="1"><small>1</small></td>
							<td><input type="radio" name="grading" value="2"><small>2</small></td>
							<td><input type="radio" name="grading" value="3"><small>3</small></td>
							<td><input type="radio" name="grading" value="4"><small>4</small></td>
							<td><input type="radio" name="grading" value="5"><small>5</small></td>
						</tr>
					</table>
					<p>
						<strong><em>Comments</em></strong>
					</p>
					<textarea id="comments" rows="4" cols="50"></textarea>
					<br>
					<button id="submit" type="button" class="btn btn-warning">Submit</button>
					<button id="clear" type="button" class="btn btn-primary">Clear</button>

				</div>
				<div class="col-md-3"></div>
			</div>
		</div>

		<div id="colC" class="col-md-2"></div>
	</div>
		<br><jsp:directive.include file="sitefooter.html" />
	
</div>
</body>
<script>
var profid = <%=request.getParameter("profid")%>;

$(document).ready(function(){
	$("#submit").click(function(){
		submitRating();
	})
	$("#clear").click(function(){
		location.reload();
	})
})

function verify(){
	var correct = 0;
	if($("#select-courses option:selected").text() == "-") {
		correct = 1;
		return correct;
	}
}

function getRadios(name){
	var radios = document.getElementsByName(name);

	for (var i = 0, length = radios.length; i < length; i++) {
	    if (radios[i].checked) {
	        // do whatever you want with the checked radio
	        return radios[i].value;

	        // only one radio can be logically checked, don't check the rest
	        break;
	    }
	}
}

function submitRating(){
	var coursenum;
	var sem;
	var year;
	var help;
	var clarity;
	var easy; 
	var grade;
	var comments;
	var user;
	
	var fill = verify();
	if(!verify){
		alert("Form incomplete!");
	}
	else{
		coursenum = $("#select-course option:selected").text();
		sem = $("#select-semester option:selected").text();
		year = $("#select-year option:selected").text();
		help = getRadios("helpfulness");
		easy = getRadios("easiness");
		clarity = getRadios("clarity");
		grade = getRadios("grading");
		comments = document.getElementById("comments").value;
		user = getCookie("user-name");	
		
		$.ajax({
	        type: "POST",
	        url: "${pageContext.request.contextPath}/RatingServlet",
	        data: {postrequest: "addRating", professorid : profid, user : username, coursenumber : coursenum, semester : sem, term : year, helpfulness : help, easiness : easy, clarity : clarity, grading : grade, comments : comments}
	      }).done(function( msg ) {
	    	  window.location.replace("${pageContext.request.contextPath}/professor.jsp?profid='"+profid+"'");
	        });
	}
}

var ratingsApp = angular.module("ratingsApp", []);
ratingsApp.controller("ratingsController", function($scope, $http) {
		
	 $http({
	        url: "${pageContext.request.contextPath}/RatingServlet", 
	        method: "GET",
	        params: {request: "profsearch", professorid: profid}
	     })
	     .success
	     (
	    		 function(response) 
	    		 {
	    			responseObject = angular.fromJson(response);
	    			console.log(responseObject);
	    			$scope.courses = responseObject.courses;
	    			$scope.data = responseObject.professor;
	   
	    		 }
	    		  
	     );
    
});
</script>
</html>