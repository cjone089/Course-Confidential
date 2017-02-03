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
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import com.courseconfidential.core.CCUser;
import com.courseconfidential.utils.CCAuthenticationTool;
import com.courseconfidential.utils.CCCookieMonster;
import com.courseconfidential.utils.Credentials;

/**
 * Servlet implementation class RegistrationServlet
 */
@WebServlet("/RegistrationServlet")
public class RegistrationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegistrationServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		switch((String)request.getParameter("request"))
		{
			case "list_questions":
				System.out.println("Entered list questions case...");
				listQuestions(request, response);
			break;
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("Entered doPost");
		switch((String)request.getParameter("postrequest"))
		{
			case "register":
				registerUser(request, response);
			break;
			case "create_guest":
				System.out.println("Entering create_guest case.");
				createGuest(request, response);
			break;
		}
	}
	
	protected void registerUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String username = request.getParameter("username");
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String userType = request.getParameter("userType");
		String university = request.getParameter("university");
		String department = request.getParameter("department");
		String[] answers = new String[3]; 
		answers[0] = request.getParameter("answer1");
		answers[1] = request.getParameter("answer2");
		answers[2] = request.getParameter("answer3");
		CCAuthenticationTool authTool = new CCAuthenticationTool();
		
		CCUser userCheck = new CCUser();
		
		String ID = userCheck.generateUserId();
		
		System.out.println("New generated user ID is: " + ID);
		
		JSONObject resObj;
		
		if(userCheck.userExists(ID, email, username) == 1)
		{
			System.out.println("This email is already in use.");
			resObj = new JSONObject();
        	resObj.put("registerstatus", "fail");
        	resObj.put("reason", "This email is already in use");
        	sendResponse(resObj.toJSONString(), response);
		}
		else if(userCheck.userExists(ID, email, username) == 2)
		{
			System.out.println("This username is already in use.");
			resObj = new JSONObject();
        	resObj.put("registerstatus", "fail");
        	resObj.put("reason", "This username is already in use");
        	sendResponse(resObj.toJSONString(), response);
		}
		else
		{
			System.out.println("ID and email are both unique, account can be created.");
			
			
			Connection dbConnection = null;
			PreparedStatement usersInsert = null;
			PreparedStatement user_roleInsert = null;
			Statement stmt = null;
			PreparedStatement taskInsert = null;
			
			String insertTask = "INSERT INTO courseconfidential.task_list"
					+ "(task_id, task, source_url, description, name, email, username) VALUES"
					+ "(?,?,?,?,?,?,?)";
			String insertUsers = "INSERT INTO courseconfidential.users"
					+ "(idnum, fname, lname, status, username, email, password, login_attempts) VALUES"
					+ "(?,?,?,?,?,?,?,?)";
			String insertUser_role = "INSERT INTO courseconfidential.user_role"
					+ "(idnum, role_num) VALUES"
					+ "(?,?)";
			
			try {
				String passHash = authTool.generateStrongPasswordHash(password);
				Class.forName("com.mysql.jdbc.Driver");
				dbConnection = DriverManager.getConnection(Credentials.DB_URL,Credentials.USERNAME,Credentials.PASSWORD);
				usersInsert = dbConnection.prepareStatement(insertUsers);
				user_roleInsert = dbConnection.prepareStatement(insertUser_role);
				stmt = dbConnection.createStatement();
				
				if(userType.equalsIgnoreCase("Professor"))
				{
					String task_id = userCheck.generateId();
					
					taskInsert = dbConnection.prepareStatement(insertTask);
					
					taskInsert.setString(1, task_id);
					taskInsert.setString(2, "Requesting professor account creation");
					taskInsert.setString(3, "http://localhost:8080/CourseConfidential/registration.jsp");
					taskInsert.setString(4, "University: " + university + " Department: " + department);
					taskInsert.setString(5, firstName + " " + lastName);
					taskInsert.setString(6, email);
					taskInsert.setString(7, username);			
					
					System.out.println("TaskInsert table prepared statement created.");
					// execute insert SQL statements
					taskInsert.executeUpdate();
		 
					System.out.println("Records are inserted into task_list table!");
				}
				
				
				for(int i = 1; i < 4; i++)
				{
					String query = "INSERT INTO courseconfidential.password_answers  "
							+ "VALUES ('"+ID+"',"+i+",'"+answers[i-1]+"')";
					stmt.addBatch(query);
				}
				
				stmt.executeBatch();
				
				System.out.println("Batch for password_answers table executed...");
				
				usersInsert.setString(1, ID);
				usersInsert.setString(2, firstName);
				usersInsert.setString(3, lastName);
				usersInsert.setString(5, username);
				usersInsert.setString(6, email);
				usersInsert.setString(7, passHash);
				usersInsert.setString(8, "0");

				if(userType.equalsIgnoreCase("Professor"))
				{
					usersInsert.setString(4, "SUSPENDED");
				}
				else
				{
					usersInsert.setString(4, "ACTIVE");
				}
				
				System.out.println("Users table prepared statement created.");
				
				user_roleInsert.setString(1, ID);
				if(userType.equals("Registered User"))
				{
					user_roleInsert.setString(2, "2");
					System.out.println("User selected registered user account type.");
				}
				else if(userType.equals("Professor"))
				{
					user_roleInsert.setString(2, "3");
					System.out.println("User selected professor account type.");
				}
				
				System.out.println("User_roleInsert table prepared statement created.");
				// execute insert SQL statements
				usersInsert.executeUpdate();
				user_roleInsert.executeUpdate();
	 
				System.out.println("Records are inserted into users table and user_role table!");
				
				
				CCCookieMonster monstah = new CCCookieMonster(request,response);
		
				monstah.listCookies();
				Cookie c = null;
				
				if(monstah.existsCookie("user-id")){
	 				c = monstah.createCookie("user-id",ID,1);
	 				response.addCookie(c);
	 			}
	 			if(monstah.existsCookie("user-name")){
	 				c = monstah.createCookie("user-name",username,1);
	 				response.addCookie(c);
	 			}
	 			if(monstah.existsCookie("user-email")){
	 				c = monstah.createCookie("user-email",email,1);
	 				response.addCookie(c);
	 			}
	 			if(monstah.existsCookie("user-status")){
	 				c = monstah.createCookie("user-status","authenticated",1);
	 				response.addCookie(c);
	 			}
	 			if(monstah.existsCookie("user-type")){
	 				c = monstah.createCookie("user-type",userType,1);
	 				response.addCookie(c);
	 			}
					
	 			monstah.listCookies();	
				
				resObj = new JSONObject();
	        	resObj.put("registerstatus", "success");
	        	resObj.put("reason", "The account was created");
	        	sendResponse(resObj.toJSONString(), response);
	 
			} catch (SQLException | ClassNotFoundException | InvalidKeySpecException | NoSuchAlgorithmException e){
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
 
	}
	
	protected void sendResponse(String output, HttpServletResponse response)throws ServletException, IOException
	{
		byte[] b = output.getBytes();
		ServletOutputStream outstream = response.getOutputStream();
		outstream.write(b);
		outstream.flush();
		outstream.close();
	}
	
	protected void createGuest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CCCookieMonster monster = new CCCookieMonster(request, response);
		System.out.println("Entered create guest method.");
		monster.listCookies();
		
		if(!monster.existsCookie("user-status"))
		{
			System.out.println("Cookies are being created...");
			CCUser userCheck = new CCUser();
			
			String ID = userCheck.generateUserId();
			
			//add user id cookie
    		response.addCookie(monster.createCookie("user-id", ID, 1));	 
    		response.addCookie(monster.createCookie("user-name", "guest" + ID.substring(0, 5), 1));	 	        		        	
    		response.addCookie(monster.createCookie("user-status","active", 1));	 
    		response.addCookie(monster.createCookie("user-type","guest", 1));		
		}
	}
	
	protected void listQuestions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection dbConnection = null;
		Statement authQuery = null;
				
		//response object to send to the user		

		try{
			
			Class.forName("com.mysql.jdbc.Driver");
			
			String query = "SELECT p.question FROM courseconfidential.password_question p";
						   
	        dbConnection = DriverManager.getConnection(Credentials.DB_URL,Credentials.USERNAME,Credentials.PASSWORD);
	        
	        authQuery = dbConnection.createStatement();
	        
	        System.out.println(query);
	        
	        ResultSet rs = authQuery.executeQuery(query);
	        
	        JSONArray resList = new JSONArray();
	        JSONObject curr = new JSONObject();
	        int questionNumber = 1;
	        while(rs.next())
	        {
	        	curr.put("question_" + questionNumber, rs.getString("question"));	   
	        	System.out.println("Question " + questionNumber + " is: " + rs.getString("question"));
	        	questionNumber++;
	        }
	        
	        
	        resList.add(curr);
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
	
}
	
	


