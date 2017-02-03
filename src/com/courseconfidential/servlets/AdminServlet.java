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

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import com.courseconfidential.core.CCUser;
import com.courseconfidential.utils.CCAuthenticationTool;
import com.courseconfidential.utils.Credentials;

/**
 * Servlet implementation class AdminServlet
 */
@WebServlet("/AdminServlet")
public class AdminServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		switch((String)request.getParameter("adminrequest"))
		{
			case "list-users":
				listAllUsers(request, response);
			break;
			case "search-user":
				searchUser(request, response);
			break;
			case "suspend-user":
				suspendUser(request, response);
			break;
			case "unlock-user":
				unlockUser(request, response);
			break;
			case "list-professor-courses":
				getCourses(request, response);
			break;
			
			case "list-professor-files":
				listProfFiles(request, response);
			break;
			case "list-professor-rating":
				listProfRating(request,response);
			break;
			case "list-tasks":
				listTasks(request,response);
			break;
			case "search-file":
				searchFile(request,response);
			break;
			case "store-report":
				storeReport(request, response);
			break;
			case "search-comment":
				searchComment(request, response);
			break;



		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		switch((String)request.getParameter("adminrequest"))
		{
			case "store-report":
				storeReport(request, response);
			break;
			case "remove-file":
				removeFile(request,response);
			break;
			case "remove-comment":
				removeComment(request,response);
			break;
		}
	}
	
	protected void removeComment(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Connection dbConnection = null;
		Statement statement = null;
		
		String commentid= request.getParameter("id");
		
		String query1 ="DELETE FROM courseconfidential.professor_ratings WHERE rating_id='"+commentid+"'";
		String query2 ="DELETE FROM courseconfidential.votes WHERE id='"+commentid+"'";
		
		try{
			
			Class.forName("com.mysql.jdbc.Driver");
			
	        dbConnection = DriverManager.getConnection(Credentials.DB_URL,Credentials.USERNAME,Credentials.PASSWORD);
			   
	        statement = dbConnection.createStatement();
	        
	        statement = dbConnection.createStatement();	        
	        System.out.println(query1);        
	        statement.executeUpdate(query1);
	        
	        statement = dbConnection.createStatement();	        
	        System.out.println(query2);        
	        statement.executeUpdate(query2);

	        JSONObject done = new JSONObject();
	        done.put("status", "success");
	        
	        sendResponse(done.toString(),response);
	        
	       
	        	        
		}catch (SQLException | ClassNotFoundException e){
			e.printStackTrace();
		}finally{
			try {
				dbConnection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	protected void searchComment(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection dbConnection = null;
		Statement statement = null;
		
		String commentid= request.getParameter("id");
		
		String query ="SELECT course_code, course_canonical, university_code, course_name, rating_id, username, prof_id, comment "+
					   " FROM courseconfidential.master_courselist mcl, courseconfidential.professor_ratings pr "+
					   " WHERE pr.rating_id='"+commentid+"' AND mcl.course_code=pr.course";
		
		try{
			
			Class.forName("com.mysql.jdbc.Driver");
			
	        dbConnection = DriverManager.getConnection(Credentials.DB_URL,Credentials.USERNAME,Credentials.PASSWORD);
			   
	        statement = dbConnection.createStatement();
	        
	        System.out.println(query);
	        
	        ResultSet rs = statement.executeQuery(query);
	        
	        JSONObject result = new JSONObject();
	        JSONArray resList = new JSONArray();
	        	        
	        while(rs.next())
	        {
	        	result.put("courseid",rs.getString("course_code"));
	        	result.put("coursename",rs.getString("course_name"));
	        	result.put("ratingid", rs.getString("rating_id"));
	        	result.put("username", rs.getString("username"));
	        	result.put("comment", rs.getString("comment"));
	        	result.put("profid", rs.getString("prof_id"));
	        	resList.add(result);
	        	
	        }
	        
	        System.out.println(resList.toString());
	        sendResponse(resList.toString(), response);
	        	        
		}catch (SQLException | ClassNotFoundException e){
			e.printStackTrace();
		}finally{
			try {
				dbConnection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	protected void searchFile(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection dbConnection = null;
		Statement statement = null;
		
		String fileid= request.getParameter("id");
		
		String query ="SELECT * "+
					   " FROM courseconfidential.course_file_list cfl, courseconfidential.master_file_list mfl "+
					   " WHERE mfl.uuid='"+fileid+"' AND cfl.file_uuid='"+fileid+"'";
		
		try{
			
			Class.forName("com.mysql.jdbc.Driver");
			
	        dbConnection = DriverManager.getConnection(Credentials.DB_URL,Credentials.USERNAME,Credentials.PASSWORD);
			   
	        statement = dbConnection.createStatement();
	        
	        System.out.println(query);
	        
	        ResultSet rs = statement.executeQuery(query);
	        
	        JSONArray resList = new JSONArray();
	        
	        	        
	        while(rs.next())
	        {
	        	JSONObject curr = new JSONObject();
	        	curr.put("uuid", rs.getString("uuid"));
	        	curr.put("category", rs.getString("category"));
	        	curr.put("description", rs.getString("description"));
	        	curr.put("type", rs.getString("type"));
	        	curr.put("pages", rs.getString("pages"));
	        	curr.put("size", rs.getString("size"));
	        	curr.put("name", rs.getString("name"));
	        	resList.add(curr);
	        }
	        
	        System.out.println(resList.toString());
	        sendResponse(resList.toString(), response);
	        	        
		}catch (SQLException | ClassNotFoundException e){
			e.printStackTrace();
		}finally{
			try {
				dbConnection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	protected void removeFile(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection dbConnection = null;
		Statement statement = null;
		
		String fileid= request.getParameter("id");
		
		String query1 ="DELETE FROM courseconfidential.master_file_list WHERE uuid='"+fileid+"'";
		String query2 ="DELETE FROM courseconfidential.votes WHERE id='"+fileid+"'";
		String query3 ="DELETE FROM courseconfidential.course_file_list WHERE file_uuid='"+fileid+"'";

		
		try{
			
			Class.forName("com.mysql.jdbc.Driver");
			
	        dbConnection = DriverManager.getConnection(Credentials.DB_URL,Credentials.USERNAME,Credentials.PASSWORD);
			   
	        statement = dbConnection.createStatement();	        
	        System.out.println(query1);        
	        statement.executeUpdate(query1);
	        
	        statement = dbConnection.createStatement();	        
	        System.out.println(query2);        
	        statement.executeUpdate(query2);
	        
	        statement = dbConnection.createStatement();	        
	        System.out.println(query3);        
	        statement.executeUpdate(query3);
	        
	        JSONObject done = new JSONObject();
	        done.put("status", "success");
	        
	        sendResponse(done.toString(),response);
	        
	        	        
		}catch (SQLException | ClassNotFoundException e){
			e.printStackTrace();
		}finally{
			try {
				dbConnection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	protected void listTasks(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection dbConnection = null;
		Statement statement = null;
		
		String profid=request.getParameter("professorid");
		String courseid=request.getParameter("courseid");
		
		String query ="SELECT * FROM courseconfidential.task_list WHERE 1";
		
		try{
			
			Class.forName("com.mysql.jdbc.Driver");
			
	        dbConnection = DriverManager.getConnection(Credentials.DB_URL,Credentials.USERNAME,Credentials.PASSWORD);
			   
	        statement = dbConnection.createStatement();
	        
	        System.out.println(query);
	        
	        ResultSet rs = statement.executeQuery(query);
	        
	        JSONArray resList = new JSONArray();
	        
	        	        
	        while(rs.next())
	        {
	        	JSONObject curr = new JSONObject();
	        	curr.put("taskid", rs.getString("task_id"));
	        	curr.put("task",rs.getString("task"));
	        	curr.put("source_url", rs.getString("source_url"));
	        	curr.put("description", rs.getString("description"));
	        	curr.put("name", rs.getString("name"));
	        	curr.put("email", rs.getString("email"));
	        	curr.put("username", rs.getString("username"));
	        	resList.add(curr);
	        	
	        }
	        
	        System.out.println(resList.toString());
	        sendResponse(resList.toString(), response);
	        	        
		}catch (SQLException | ClassNotFoundException e){
			e.printStackTrace();
		}finally{
			try {
				dbConnection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	protected void listProfRating(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection dbConnection = null;
		Statement statement = null;
		
		String profid=request.getParameter("professorid");
		String courseid=request.getParameter("courseid");
		
		String query ="SELECT rating_id, username, prof_id, course, easiness, clarity, helpfulness, grading, comment, course_canonical, course_name, prof_rated "+
					   " FROM courseconfidential.professor_ratings rat, courseconfidential.master_courselist mcl"+
					   " WHERE rat.course='"+courseid+"' AND rat.prof_id='"+profid+"' AND mcl.course_code = rat.course";	
		
		try{
			
			Class.forName("com.mysql.jdbc.Driver");
			
	        dbConnection = DriverManager.getConnection(Credentials.DB_URL,Credentials.USERNAME,Credentials.PASSWORD);
			   
	        statement = dbConnection.createStatement();
	        
	        System.out.println(query);
	        
	        ResultSet rs = statement.executeQuery(query);
	        
	        JSONArray resList = new JSONArray();
	        
	        
	        String preview =""; //preview string
	        
	        while(rs.next())
	        {
	        	JSONObject curr = new JSONObject();
	        	curr.put("rating", rs.getString("rating_id"));
	        	curr.put("user", rs.getString("username"));
	        	curr.put("course", rs.getString("course"));
	        	curr.put("easiness",rs.getInt("easiness"));
	        	curr.put("clarity", rs.getInt("clarity"));
	        	curr.put("helpfulness", rs.getInt("helpfulness"));
	        	curr.put("grading",rs.getInt("grading"));
	        	curr.put("comment", rs.getString("comment"));
	        	curr.put("coursenum", rs.getString("course_canonical"));
	        	curr.put("coursename", rs.getString("course_name"));
	        	
	        	int israted = rs.getInt("prof_rated");
	        	
	        	if(israted > 0)
		        	curr.put("israted", "true");
	        	else 
		        	curr.put("israted", "false");


	        	
	        	preview = rs.getString("comment");
	        	
	        	if(preview.length() >30)
	        	preview = preview.substring(0,30)+"...";
	        	
	        	curr.put("preview", preview);
	        	resList.add(curr);
	        	
	        }
	        
	        System.out.println(resList.toString());
	        sendResponse(resList.toString(), response);
	        	        
		}catch (SQLException | ClassNotFoundException e){
			e.printStackTrace();
		}finally{
			try {
				dbConnection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	protected void listProfFiles(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Connection dbConnection = null;
		Statement statement = null;
		
		String profid=request.getParameter("professorid");
		String courseid=request.getParameter("courseid");

		
		String query ="SELECT file_uuid, professor_id, course_code, term, category, description, pages, size, name"+
					  " FROM courseconfidential.course_file_list cfl, courseconfidential.master_file_list mfl"+
					  " WHERE course_code = '"+courseid+"' AND professor_id='"+profid+"' AND mfl.uuid = cfl.file_uuid";
							
		try{
			
			Class.forName("com.mysql.jdbc.Driver");
			
	        dbConnection = DriverManager.getConnection(Credentials.DB_URL,Credentials.USERNAME,Credentials.PASSWORD);
			   
	        statement = dbConnection.createStatement();
	        
	        System.out.println(query);
	        
	        ResultSet rs = statement.executeQuery(query);
	        
	        JSONArray resList = new JSONArray();
	        	        
	        while(rs.next())
	        {
	        	JSONObject curr = new JSONObject();
	        	curr.put("uuid", rs.getString("file_uuid"));
	        	curr.put("professor", rs.getString("professor_id"));
	        	curr.put("course", rs.getString("course_code"));
	        	curr.put("term",rs.getString("term"));
	        	curr.put("category", rs.getString("category"));
	        	curr.put("description", rs.getString("description"));
	        	curr.put("size",rs.getString("size"));
	        	curr.put("pages", rs.getString("pages"));
	        	curr.put("name", rs.getString("name"));
	        	resList.add(curr);
	        	
	        }
	        
	        System.out.println(resList.toString());
	        sendResponse(resList.toString(), response);
	        	        
		}catch (SQLException | ClassNotFoundException e){
			e.printStackTrace();
		}finally{
			try {
				dbConnection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	
	protected void getCourses(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		Connection dbConnection = null;
		Statement statement = null;
		
		String profid=request.getParameter("professorid");
		
		String query ="SELECT prof.prof_id AS professor_id, prof.first_name, prof.last_name, prof.university_code, prof.department, cl.course_code, cl.term, mcl.course_canonical, mcl.course_name"+
						" FROM "+
						"(SELECT * FROM courseconfidential.professor_list WHERE prof_id='"+profid+"') AS prof, courseconfidential.courselist cl, courseconfidential.master_courselist mcl"+
						" WHERE prof.prof_id = cl.professor_id AND mcl.course_code = cl.course_code";
		try{
			
			Class.forName("com.mysql.jdbc.Driver");
			
	        dbConnection = DriverManager.getConnection(Credentials.DB_URL,Credentials.USERNAME,Credentials.PASSWORD);
			   
	        statement = dbConnection.createStatement();
	        
	        System.out.println(query);
	        
	        ResultSet rs = statement.executeQuery(query);
	        
	        JSONArray resList = new JSONArray();
	        JSONObject resObj = new JSONObject();
	        
	        boolean firstrun = false;
	        
	        while(rs.next())
	        {
	        	//sloppily get this professors details
	        	if(!firstrun)
	        	{
	        		JSONObject profdeets = new JSONObject();
	        		profdeets.put("profid", rs.getString("professor_id"));
	        		profdeets.put("proffirst", rs.getString("first_name"));
	        		profdeets.put("proflast", rs.getString("last_name"));
	        		profdeets.put("department", rs.getString("department"));
	        		profdeets.put("university", rs.getString("university_code"));
	        		resObj.put("Professor", profdeets);
	        		firstrun = true;
	        	}
	        	
	        	JSONObject curr = new JSONObject();
	        	curr.put("courseid", rs.getString("course_code"));
	        	curr.put("term", rs.getString("term"));
	        	curr.put("coursecanonical", rs.getString("course_canonical"));
	        	curr.put("coursename", rs.getString("course_name"));
	        	resList.add(curr);    	
	        	
	        }
	        
	        resObj.put("Profcourses", resList);
	        System.out.println(resObj.toString());
	        sendResponse(resObj.toString(), response);
	        	        
		}catch (SQLException | ClassNotFoundException e){
			e.printStackTrace();
		}finally{
			try {
				dbConnection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	protected void unlockUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection dbConnection = null;
		PreparedStatement updateStatement = null;
		
		String idnum = (String)request.getParameter("userid");

				
		//response object to send to the user		

		try{
			
			Class.forName("com.mysql.jdbc.Driver");
			
			String query = "UPDATE courseconfidential.users SET status='ACTIVE' WHERE idnum= ?";
						   
	        dbConnection = DriverManager.getConnection(Credentials.DB_URL,Credentials.USERNAME,Credentials.PASSWORD);
	        
	        updateStatement = dbConnection.prepareStatement(query);
	        
	        System.out.println(query);
	        
	        updateStatement.setString(1, idnum);
	        
	        updateStatement.executeUpdate();
	        
	        JSONObject successObj = new JSONObject();
	        successObj.put("status", "success");
	        
	        sendResponse(successObj.toString(), response);

	        
		}catch (SQLException | ClassNotFoundException e){
			e.printStackTrace();
		}finally{
			try {
				dbConnection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	        
	       
	}	
	
	protected void suspendUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection dbConnection = null;
		PreparedStatement updateStatement = null;
		
		String idnum = (String)request.getParameter("userid");

				
		//response object to send to the user		

		try{
			
			Class.forName("com.mysql.jdbc.Driver");
			
			String query = "UPDATE courseconfidential.users SET status='SUSPENDED' WHERE idnum= ?";
						   
	        dbConnection = DriverManager.getConnection(Credentials.DB_URL,Credentials.USERNAME,Credentials.PASSWORD);
	        
	        updateStatement = dbConnection.prepareStatement(query);
	        
	        System.out.println(query);
	        
	        updateStatement.setString(1, idnum);
	        
	        updateStatement.executeUpdate();
	        
	        JSONObject successObj = new JSONObject();
	        successObj.put("status", "success");
	        
	        sendResponse(successObj.toString(), response);

	        
		}catch (SQLException | ClassNotFoundException e){
			e.printStackTrace();
		}finally{
			try {
				dbConnection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	        
	       
	}	
	protected void searchUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Connection dbConnection = null;
		Statement authQuery = null;
		
		String idnum = (String)request.getParameter("userid");
		String email  = (String)request.getParameter("email");
		String username = (String)request.getParameter("username");
				
		//response object to send to the user		

		try{
			
			Class.forName("com.mysql.jdbc.Driver");
			
			String query = "SELECT * "+
							 "FROM "+
							 "(SELECT * "+
							 "FROM courseconfidential.users "+
							 "WHERE email='"+email+"' OR username='"+username+"') AS u, courseconfidential.user_role r, courseconfidential.role_list rl "+
							 "WHERE u.idnum = r.idnum AND r.role_num = rl.role_num ";
						   
	        dbConnection = DriverManager.getConnection(Credentials.DB_URL,Credentials.USERNAME,Credentials.PASSWORD);
	        
	        authQuery = dbConnection.createStatement();
	        
	        System.out.println(query);
	        
	        ResultSet rs = authQuery.executeQuery(query);
	        
	        JSONArray resList = new JSONArray();
	        
	        while(rs.next())
	        {
	        	JSONObject curr = new JSONObject();
	        	curr.put("user_id", rs.getString("idnum"));
	        	curr.put("user_first", rs.getString("fname"));
	        	curr.put("user_last", rs.getString("lname"));
	        	curr.put("user_status", rs.getString("status"));
	        	curr.put("user_name", rs.getString("username"));
	        	curr.put("user_email", rs.getString("email"));
	        	curr.put("user_login", rs.getString("login_attempts"));
	        	curr.put("user_role", rs.getString("role"));
	        	resList.add(curr);
	        }
	        
	        System.out.println(resList.toString());
	        sendResponse(resList.toString(), response);
	        
		}catch (SQLException | ClassNotFoundException e){
			e.printStackTrace();
		}finally{
			try {
				dbConnection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	protected void listAllUsers(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		Connection dbConnection = null;
		Statement authQuery = null;
				
		//response object to send to the user		

		try{
			
			Class.forName("com.mysql.jdbc.Driver");
			
			String query = "SELECT *"+
						   " FROM courseconfidential.users u, courseconfidential.user_role r, courseconfidential.role_list rl"+
						   " WHERE u.idnum = r.idnum AND r.role_num = rl.role_num";
						   
	        dbConnection = DriverManager.getConnection(Credentials.DB_URL,Credentials.USERNAME,Credentials.PASSWORD);
	        
	        authQuery = dbConnection.createStatement();
	        
	        System.out.println(query);
	        
	        ResultSet rs = authQuery.executeQuery(query);
	        
	        JSONArray resList = new JSONArray();
	        
	        while(rs.next())
	        {
	        	JSONObject curr = new JSONObject();
	        	curr.put("user_id", rs.getString("idnum"));
	        	curr.put("user_first", rs.getString("fname"));
	        	curr.put("user_last", rs.getString("lname"));
	        	curr.put("user_status", rs.getString("status"));
	        	curr.put("user_name", rs.getString("username"));
	        	curr.put("user_email", rs.getString("email"));
	        	curr.put("user_login", rs.getString("login_attempts"));
	        	curr.put("user_role", rs.getString("role"));
	        	resList.add(curr);
	        }
	        
	        System.out.println(resList.toString());
	        sendResponse(resList.toString(), response);
	        
		}catch (SQLException | ClassNotFoundException e){
			e.printStackTrace();
		}finally{
			try {
				dbConnection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	protected void storeReport(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String url = request.getParameter("url");
		String description = request.getParameter("description");
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String task = request.getParameter("task");
		
		CCUser userCheck = new CCUser();
		
		String task_id = userCheck.generateUserId();
		
		System.out.println("New generated user ID is: " + task_id);
		
		Connection dbConnection = null;
		PreparedStatement taskInsert = null;
 
		String insertTask = "INSERT INTO courseconfidential.task_list"
				+ "(task_id, task, source_url, description, name, email, username) VALUES"
				+ "(?,?,?,?,?,?,?)";
 
		try {
			Class.forName("com.mysql.jdbc.Driver");
			dbConnection = DriverManager.getConnection(Credentials.DB_URL,Credentials.USERNAME,Credentials.PASSWORD);
			taskInsert = dbConnection.prepareStatement(insertTask);
			
			taskInsert.setString(1, task_id);
			taskInsert.setString(2, task);
			taskInsert.setString(3, url);
			taskInsert.setString(4, description);
			taskInsert.setString(5, name);
			taskInsert.setString(6, email);
			taskInsert.setString(7, username);			
			
			System.out.println("TaskInsert table prepared statement created.");
			// execute insert SQL statements
			taskInsert.executeUpdate();
 
			System.out.println("Records are inserted into task_list table!");
 
		} catch (SQLException | ClassNotFoundException e){
			e.printStackTrace();
		}finally{
			try {
				dbConnection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
		
	}
	
	protected void sendResponse(String output, HttpServletResponse response)throws ServletException, IOException
	{
		byte[] b = output.getBytes();
		ServletOutputStream outstream = response.getOutputStream();
		outstream.write(b);
		outstream.flush();
		outstream.close();
	}
}
