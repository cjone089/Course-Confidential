package com.courseconfidential.servlets;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import com.courseconfidential.utils.Credentials;

/**
 * Servlet implementation class RatingServlet
 */
@WebServlet("/RatingServlet")
public class RatingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RatingServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		switch ((String) request.getParameter("request")) {
		case "profsearch":
			professorSearch(request, response);
			break;
		case "ratingsearch":
			getRating(request, response);
			break;
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		switch ((String) request.getParameter("postrequest")) {
		case "updateVotes":
			updateVotes(request, response);
			break;
		case "profresp":
			professorResponse(request, response);
			break;
		case "addRating":
			newRating(request, response);
			break;
		}
	}

	protected void professorSearch(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String profid = (String) request.getParameter("professorid");
		String proffirst = null;
		String profflast = null;
		String unicode = null;
		String uniname = null;
		String coursenum = null;
		String term = null;
		String coursename = null;

		Connection dbConnection = null;
		Statement authQuery = null;

		try {
			Class.forName("com.mysql.jdbc.Driver");

			String query = "SELECT prof.prof_id, prof.first_name, prof.last_name, prof.university_code, uni.name, mastcourse.course_canonical, course.term, mastcourse.course_name "
					+ "FROM courseconfidential.professor_list prof, courseconfidential.university_list uni, courseconfidential.master_courselist mastcourse, courseconfidential.courselist course "
					+ "WHERE prof.prof_id = '"
					+ profid
					+ "' AND uni.code = prof.university_code AND prof.prof_id = course.professor_id AND course.course_code = mastcourse.course_code";

			dbConnection = DriverManager.getConnection(Credentials.DB_URL,
					Credentials.USERNAME, Credentials.PASSWORD);

			authQuery = dbConnection.createStatement();

			System.out.println(query);

			ResultSet rs = authQuery.executeQuery(query);

			JSONArray courseList = new JSONArray();
			JSONObject resObj = new JSONObject();
			
			String uni= null;
			boolean universityFound = false;
			while (rs.next()) {
				if(!universityFound){
					uni = rs.getString("name");
					universityFound = true;
				}
				proffirst = rs.getString("first_name");
				profflast = rs.getString("last_name");
				unicode = rs.getString("university_code");
				uniname = rs.getString("name");
				coursenum = rs.getString("course_canonical");
				term = rs.getString("term");
				coursename = rs.getString("course_name");

				JSONObject courseObj = new JSONObject();
				courseObj.put("university_code", unicode);
				courseObj.put("name", uniname);
				courseObj.put("course_canonical", coursenum);
				courseObj.put("term", term);
				courseObj.put("course_name", coursename);
				courseList.add(courseObj);
			}

			JSONObject nameObj = new JSONObject();
			nameObj.put("proff_first", proffirst);
			nameObj.put("proff_last", profflast);
			nameObj.put("university", uni);
			
			resObj.put("courses", courseList);
			resObj.put("professor", nameObj);

			sendResponse(resObj.toString(), response);
			System.out.println(resObj.toString());

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				dbConnection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	protected void newRating(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		String profid = (String) request.getParameter("professorid");
		String coursenum = (String) request.getParameter("coursenumber");
		String semester = (String) request.getParameter("semester");
		String year = (String) request.getParameter("year");
		int helpfulness = Integer.parseInt((String) request.getParameter("helpfulness"));
		int easiness = Integer.parseInt((String) request.getParameter("easiness"));
		int clarity = Integer.parseInt((String) request.getParameter("clarity"));
		int grading = Integer.parseInt((String) request.getParameter("grading"));
		String comments = (String) request.getParameter("comments");
		String username = request.getParameter("user");
		String uuid = UUID.randomUUID().toString();

		Connection dbConnection = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			String query = "INSERT INTO courseconfidential.professor_ratings (rating_id, username, prof_id, course, easiness, clarity, helpfulness, grading, comment, prof_resp, prof_rated) VALUES(?,?,?,?,?,?,?,?,?,?,?)";

			dbConnection = DriverManager.getConnection(Credentials.DB_URL,
					Credentials.USERNAME, Credentials.PASSWORD);
			
			Statement authQuery = dbConnection.createStatement();
			
			ResultSet rs = authQuery.executeQuery("SELECT c.course_code FROM courseconfidential.master_courselist c WHERE c.course_canonical='"+coursenum+"'");
			while(rs.next()){
				coursenum = rs.getString("course_code");
			}		

			PreparedStatement stmt = dbConnection.prepareStatement(query);
			stmt.setString(1, uuid);
			stmt.setString(2, username);
			stmt.setString(3, profid);
			stmt.setString(4, coursenum);
			stmt.setInt(5, easiness);
			stmt.setInt(6, clarity);
			stmt.setInt(7, helpfulness);
			stmt.setInt(8, grading);
			stmt.setString(9, comments);
			stmt.setString(10, "");
			stmt.setInt(11, 0);
			
			System.out.println(stmt);
			stmt.executeUpdate();
			
			//insert into votes table as well
			 String query2 = "INSERT INTO courseconfidential.votes (id, vote_up, vote_down) values (?, ?, ?)";
	         PreparedStatement statement3 = dbConnection.prepareStatement(query2);
	         statement3.setString(1, uuid);
	         statement3.setInt(2, 0);
	         statement3.setInt(3, 0);   
	         statement3.executeUpdate();
			
			JSONObject resObj = new JSONObject();
			resObj.put("user", username);
			resObj.put("reason", "success");
			sendResponse(resObj.toString(), response);
			
		}catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				dbConnection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	protected void getRating(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String profid = (String) request.getParameter("professorid");

		//System.out.println("Started from the bottom now we here");

		Connection dbConnection = null;
		Statement authQuery = null;

		try {
			Class.forName("com.mysql.jdbc.Driver");

			String query = "SELECT * FROM"
					+ "(SELECT ratings.rating_id, profs.prof_id, profs.department, ratings.course, ratings.easiness, ratings.username, ratings.prof_resp, ratings.prof_rated, "
					+ " ratings.clarity, ratings.helpfulness, ratings.grading, ratings.comment, profs.first_name, profs.last_name, profs.university_code"
					+ " FROM courseconfidential.professor_ratings ratings, courseconfidential.professor_list profs"
					+ " WHERE ratings.prof_id='"
					+ profid
					+ "' AND profs.prof_id='"
					+ profid
					+ "')"
					+ " AS rating_result, courseconfidential.university_list uni, courseconfidential.votes votes, courseconfidential.master_courselist courses"
					+ " WHERE rating_result.university_code = uni.code AND rating_result.rating_id = votes.id AND courses.course_code = rating_result.course";

			dbConnection = DriverManager.getConnection(Credentials.DB_URL,
					Credentials.USERNAME, Credentials.PASSWORD);

			authQuery = dbConnection.createStatement();

			System.out.println(query);

			ResultSet rs = authQuery.executeQuery(query);

			JSONArray courseList = new JSONArray();
			JSONObject resObj = new JSONObject();

			int ratingCount = 0;
			double overallRating = 0;
			double overallEasiness = 0;
			double overallClarity = 0;
			double overallHelp = 0;
			double overallGrading = 0;
			boolean firstrecord = false;
			
			boolean ratingsExist = false;
			
			//concats first init and last name for a sig to be used after responding
			String professorSig = "";

			while (rs.next()) {
				

				if(!firstrecord)
				{
					ratingsExist = true;
					//add professor details to it
					JSONObject prof = new JSONObject();
					prof.put("professorfirst", rs.getString("first_name"));
					prof.put("professorlast", rs.getString("last_name"));
					prof.put("university",rs.getString("name"));
					prof.put("department", rs.getString("department"));
					resObj.put("Professor",prof);
					professorSig = rs.getString("first_name").charAt(0) + ". "+rs.getString("last_name");	
					firstrecord = true;
					
				}
				
				overallRating = (rs.getInt("easiness") + rs.getInt("clarity")
						+ rs.getInt("helpfulness") + rs.getInt("grading")) / 5;

				JSONObject curr = new JSONObject();
				curr.put("id", rs.getString("rating_id"));
				curr.put("course", rs.getString("course_name"));
				curr.put("easiness", (rs.getDouble("easiness") / 5) * 100);
				curr.put("clarity", (rs.getDouble("clarity") / 5) * 100);
				curr.put("helpfulness", (rs.getDouble("helpfulness") / 5) * 100);
				curr.put("grading", (rs.getDouble("grading") / 5) * 100);
				curr.put("comment", rs.getString("comment"));
				curr.put("overall", overallRating);
				curr.put("upvote", rs.getString("vote_up"));
				curr.put("downvote", rs.getString("vote_down"));
				curr.put("user", rs.getString("username"));
				curr.put("profresp",rs.getString("prof_resp"));
				//compensate for the fact that this professor has responded
				if(rs.getInt("prof_rated") == 1)
					curr.put("signature","- "+professorSig );
				else
					curr.put("signature"," ");
														
				courseList.add(curr);				
				// calculate the overall easiness based on each rating
				ratingCount++;
				overallEasiness = overallEasiness + (rs.getDouble("easiness") / 5) * 100;
				overallHelp = overallHelp + (rs.getDouble("helpfulness") / 5) * 100;
				overallClarity = overallClarity + (rs.getDouble("clarity") / 5) * 100;
				overallGrading = overallGrading + (rs.getDouble("grading") / 5) * 100;

			}
			
			if(ratingsExist){
			
			System.out.println(" Easiness: "+overallEasiness+" Help: "+overallHelp+" Clarity: "+overallClarity+" Grading: "+overallGrading);
			
			double oeasiness = (int)overallEasiness/ratingCount;
			double ohelp = (int)overallHelp/ratingCount;
			double oclarity = (int)overallClarity/ratingCount;
			double ograding = (int)overallGrading/ratingCount;
			
			System.out.println(" Easiness: "+oeasiness+" Help: "+ohelp+" Clarity: "+oclarity+" Grading: "+ograding);

			
			oeasiness = (oeasiness / 100) * 5;
			ohelp = (ohelp / 100) * 5;
			oclarity = (oclarity / 100) * 5;
			ograding = (ograding / 100) * 5;
			
			System.out.println(" Easiness: "+oeasiness+" Help: "+ohelp+" Clarity: "+oclarity+" Grading: "+ograding);


			System.out.println("Actual Average "+(int)(oeasiness+ohelp+oclarity+ograding)/4);

			
			//aggregate totals object
			JSONObject overall = new JSONObject();
			overall.put("oeasiness",(int)overallEasiness/ratingCount);
			overall.put("ohelp",(int)overallHelp/ratingCount);
			overall.put("oclarity",(int)overallClarity/ratingCount);
			overall.put("ograding",(int)overallGrading/ratingCount);
			overall.put("ooverall", (int)(oeasiness+ohelp+oclarity+ograding)/4);
			resObj.put("Overall", overall);
			


			resObj.put("Ratings", courseList);
			sendResponse(resObj.toString(), response);
			System.out.println(resObj.toString());
			}else{
				System.out.println("No ratings exist");
				query = "SELECT prof.prof_id, prof.first_name, prof.last_name, prof.university_code, uni.name, mastcourse.course_canonical, course.term, mastcourse.course_name, prof.department "
						+ "FROM courseconfidential.professor_list prof, courseconfidential.university_list uni, courseconfidential.master_courselist mastcourse, courseconfidential.courselist course "
						+ "WHERE prof.prof_id = '"
						+ profid
						+ "' AND uni.code = prof.university_code AND prof.prof_id = course.professor_id AND course.course_code = mastcourse.course_code";
				System.out.println(query);
				rs = authQuery.executeQuery(query);
				
				while(rs.next())
				{
					//add professor details to it
					JSONObject prof = new JSONObject();
					prof.put("professorfirst", rs.getString("first_name"));
					prof.put("professorlast", rs.getString("last_name"));
					prof.put("university",rs.getString("name"));
					prof.put("department", rs.getString("department"));
					resObj.put("Professor",prof);
				}
				sendResponse(resObj.toString(), response);
				System.out.println(resObj.toString());
			}

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				dbConnection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	protected void professorResponse(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String profid = request.getParameter("professorid");
		String ratingid = request.getParameter("ratingid");
		String entry = request.getParameter("presponse");
		String presponse = null;
		boolean rated = false;
		
		Connection dbConnection = null;
		Statement authQuery = null;
		JSONObject resObj;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			String query = "SELECT * FROM courseconfidential.professor_ratings r WHERE r.prof_id='"+profid+"' AND "
					+ "r.rating_id='"+ratingid+"'";

			dbConnection = DriverManager.getConnection(Credentials.DB_URL,
					Credentials.USERNAME, Credentials.PASSWORD);

			authQuery = dbConnection.createStatement();

			System.out.println(query);

			ResultSet rs = authQuery.executeQuery(query);
			
			while (rs.next()) {
				presponse = rs.getString("prof_resp");
				rated = rs.getBoolean("prof_rated");
			}
			
			if(!rated){
				//update the rating to have the prof response
				
				String updaterating = "UPDATE courseconfidential.professor_ratings "
						+ "SET prof_resp='"+entry+"', prof_rated= 1 WHERE professor_ratings.prof_id='"+profid+"' AND professor_ratings.rating_id='"+ratingid+"'";
				System.out.println(updaterating);
				PreparedStatement update = dbConnection.prepareStatement(updaterating);
				update.executeUpdate();
				
				resObj = new JSONObject();
	        	resObj.put("ratingid", ratingid);
	        	resObj.put("reason", "success");
	        	sendResponse(resObj.toJSONString(), response);
	        	System.out.println(resObj.toJSONString());
			}
			else{
				//prof already rated.
				resObj = new JSONObject();
	        	resObj.put("ratingid", ratingid);
	        	resObj.put("reason", "fail");
	        	resObj.put("response", presponse);
	        	sendResponse(resObj.toJSONString(), response);
	        	System.out.println(resObj.toJSONString());
			}
			
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				dbConnection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	protected void updateVotes(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String isUpvote = request.getParameter("isUpvote");
		String ratingid = request.getParameter("ratingid");	
		
		int upvotes = 0;
		int downvotes = 0;	
		
		Connection dbConnection = null;
		Statement authQuery = null;
		JSONObject resObj;

		try {
			//get current upvote/downvote values
			Class.forName("com.mysql.jdbc.Driver");
			String query = "SELECT * FROM courseconfidential.votes votes WHERE votes.id='"
					+ ratingid + "'";

			dbConnection = DriverManager.getConnection(Credentials.DB_URL,
					Credentials.USERNAME, Credentials.PASSWORD);

			authQuery = dbConnection.createStatement();

			System.out.println(query);

			ResultSet rs = authQuery.executeQuery(query);
			
			while (rs.next()) {
				upvotes = rs.getInt("vote_up");
				downvotes = rs.getInt("vote_down");
			}
			
			//before updating check to see if user already voted on this
			ResultSet alreadyvoted = authQuery.executeQuery("SELECT * FROM courseconfidential.voter_list vl WHERE vl.id='"+ratingid+"' AND vl.voter_username='"+username+"'");
			
			if (!alreadyvoted.isBeforeFirst()) {
				//the user has not voted on this obj thus we can update count
				if(isUpvote.equals("true")){ //update upvotes
					upvotes++;
					String updatevotes = "UPDATE courseconfidential.votes "
							+ "SET vote_up='"+upvotes+"' WHERE votes.id='"+ratingid+"'";
					System.out.println(updatevotes);
					PreparedStatement update = dbConnection.prepareStatement(updatevotes);
					update.executeUpdate();
					System.out.println("Upvotes updated!");
					
					//add user to the voterlist
					String updatevoter = "INSERT INTO courseconfidential.voter_list (id, voter_username) VALUES (?,?)";
					PreparedStatement userInsert = dbConnection.prepareStatement(updatevoter);
					userInsert.setString(1, ratingid);
					userInsert.setString(2, username);
					userInsert.executeUpdate();
					
					resObj = new JSONObject();
		        	resObj.put("votestatus", "success");
		        	resObj.put("reason", "Vote successful!");
		        	resObj.put("current", upvotes);
		        	sendResponse(resObj.toJSONString(), response);
		        	System.out.println(resObj.toJSONString());
					
				}
				else{ //update downvotes
					downvotes++;
					String updatevotes = "UPDATE courseconfidential.votes "
							+ "SET vote_down='"+downvotes+"' WHERE votes.id='"+ratingid+"'";
					System.out.println(updatevotes);
					PreparedStatement update = dbConnection.prepareStatement(updatevotes);
					update.executeUpdate();
					System.out.println("Downvotes updated!");
					
					//add the user to the voterlist
					String updatevoter = "INSERT INTO courseconfidential.voter_list (id, voter_username) VALUES (?,?)";
					PreparedStatement userInsert = dbConnection.prepareStatement(updatevoter);
					userInsert.setString(1, ratingid);
					userInsert.setString(2, username);
					userInsert.executeUpdate();
					
					resObj = new JSONObject();
		        	resObj.put("votestatus", "success");
		        	resObj.put("reason", "Vote successful!");
		        	resObj.put("current", downvotes);
		        	sendResponse(resObj.toJSONString(), response);
		        	System.out.println(resObj.toJSONString());
				}
			}
			else{
				//do not update
				resObj = new JSONObject();
	        	resObj.put("vote-status", "fail");
	        	resObj.put("reason", "User has already voted on this!");
	        	sendResponse(resObj.toJSONString(), response);
				
			}
			

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				dbConnection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	protected void sendResponse(String output, HttpServletResponse response)
			throws ServletException, IOException {
		byte[] b = output.getBytes();
		ServletOutputStream outstream = response.getOutputStream();
		outstream.write(b);
		outstream.flush();
		outstream.close();
	}
}
