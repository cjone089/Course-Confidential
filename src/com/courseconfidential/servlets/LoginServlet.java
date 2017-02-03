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
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import com.courseconfidential.utils.CCAuthenticationTool;
import com.courseconfidential.utils.CCCookieMonster;
import com.courseconfidential.utils.Credentials;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		switch((String)request.getParameter("postrequest"))
		{
			case "login":
				authenticate(request, response);
			break;
			case "logout":
				logout(request,response);
			break;
			case "forgot-password":
				forgot(request,response);
			break;
			case "reset-password":
				resetpass(request,response);
			break;
			case "send-profid":
				getProfID(request, response);
			break;
		}
	}
	
	protected void resetpass(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
		Connection dbConnection = null;
		PreparedStatement update = null;
		
		String user = request.getParameter("username");
		String pass = request.getParameter("password");

		try {
			
			
			CCAuthenticationTool authTool = new CCAuthenticationTool();

			String passnew = authTool.generateStrongPasswordHash(pass);
			
			Class.forName("com.mysql.jdbc.Driver");

			String query = "UPDATE courseconfidential.users SET password = ? WHERE idnum = ?";

			dbConnection = DriverManager.getConnection(Credentials.DB_URL,
					Credentials.USERNAME, Credentials.PASSWORD);

			update = dbConnection.prepareStatement(query);

			System.out.println(query);

			update.setString(1, passnew);
			update.setString(2, user);
			
			System.out.println(update.toString());
			
			update.executeUpdate();
		

		} catch (SQLException | ClassNotFoundException | NoSuchAlgorithmException | InvalidKeySpecException e) {
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
	
	protected void forgot(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
		Connection dbConnection = null;
		Statement authQuery = null;
		
		String email = request.getParameter("useremail");

		try {
			Class.forName("com.mysql.jdbc.Driver");

			String query = "SELECT question, question_id, user_idnum,password_answer "+
							"FROM courseconfidential.password_question pq,"+ 
							"(SELECT * FROM courseconfidential.password_answers WHERE user_idnum IN (SELECT idnum FROM courseconfidential.users WHERE email =  '"+email+"')) AS pa "+
							"WHERE pq.question_id = pa.password_question";

			dbConnection = DriverManager.getConnection(Credentials.DB_URL,
					Credentials.USERNAME, Credentials.PASSWORD);

			authQuery = dbConnection.createStatement();

			System.out.println(query);

			ResultSet rs = authQuery.executeQuery(query);

			Random rand = new Random(System.currentTimeMillis());

			//select random password question
			int randomNum = rand.nextInt((2 - 0) + 0) + 0;
			
			int x = 0;
			JSONObject[] qlist = new JSONObject[3];
			
			boolean found = false;
			
			while (rs.next()) {
				JSONObject curr = new JSONObject();
				curr.put("questionid", rs.getInt("question_id"));
				curr.put("userid", rs.getString("user_idnum"));
				curr.put("question", rs.getString("question"));
				curr.put("answer",rs.getString("password_answer"));
				curr.put("validation", "success");
				qlist[x++] = curr;
				
				found = true;
				
			}
			
			//assuming failure
			if(!found){
				JSONObject failobj = new JSONObject();
				failobj.put("validation", "fail");
				
				sendResponse(failobj.toString(), response);
				System.out.println(failobj.toString());
			
			}else{
				sendResponse(qlist[randomNum].toString(), response);
				System.out.println(qlist[randomNum].toString());
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

	
	protected void logout(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
	
		HttpSession session = request.getSession(true);
		session.invalidate();
		
		CCCookieMonster monster = new CCCookieMonster(request,response);
		monster.eatCookies();
		response.sendRedirect("index.jsp");
	}
	
	protected void authenticate(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
		
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String status = "NOT FOUND";
		String idnum = null;
		String role = null;
		String username = null;
		int logins = 0;

		
		System.out.println("Email: " + email);
		System.out.println("Pass: " + password);
		
		Connection dbConnection = null;
		Statement authQuery = null;
		
		CCAuthenticationTool authTool = new CCAuthenticationTool();
		
		//response object to send to the user
		JSONObject resObj;
		
		try{
			String passHash = authTool.generateStrongPasswordHash(password);
			
			Class.forName("com.mysql.jdbc.Driver");
			
			String query = "SELECT * FROM courseconfidential.users user, courseconfidential.user_role roles, courseconfidential.role_list role_list "
                            +"WHERE user.password='"+passHash+"' AND user.email='"+email+"' AND user.idnum = roles.idnum AND roles.role_num = role_list.role_num";
			
	        dbConnection = DriverManager.getConnection(Credentials.DB_URL,Credentials.USERNAME,Credentials.PASSWORD);
	        
	        authQuery = dbConnection.createStatement();
	        
	        System.out.println(query);
	        
	        ResultSet rs = authQuery.executeQuery(query);
	        
	        while(rs.next())
	        {
	        	status = rs.getString("status");
	        	logins = rs.getInt("login_attempts");
	        	idnum = rs.getString("idnum");
	        	role = rs.getString("role");
	        	username = rs.getString("username");
		        System.out.println("user: "+idnum+" "+username+" authenticated: "+status+" : "+" logins: "+logins+" role: "+role);  	
	        }
	        
	        
	        //verify the user is allowed to login    
	        if(status.equalsIgnoreCase("SUSPENDED")){
	        	resObj = new JSONObject();
	        	resObj.put("loginstatus", "fail");
	        	resObj.put("reason", "This user account is suspended. Please contact an Administrator for further assistance");
	        	sendResponse(resObj.toJSONString(), response);
	       
	        }
	        else if(status.equalsIgnoreCase("ACTIVE"))
	        {
	        	/**
	        	 * The following cookies will be used throughout the site
	        	 * to customize the user experience and ensure that they are authenticated
	        	 * 		
	        	 * 		user-id: "idnum"
	        	 * 		user-name: "bobthebuilder"
	        	 * 		user-email: "yayemail@gmail.com"
	        	 * 		user-status: "authenticated"
	        	 * 		user-type: "professor"
	        	 * 		set cookies to expire after sometime
	        	 */
	        	
	        		CCCookieMonster monstah = new CCCookieMonster(request,response);
	        		monstah.eatCookies();
	        		
	        		//add user id cookie
	        		response.addCookie(monstah.createCookie("user-id", idnum, 1));	 
	        		response.addCookie(monstah.createCookie("user-name", username, 1));	 	        	
	        		response.addCookie(monstah.createCookie("user-email", email, 1));	 	        	
	        		response.addCookie(monstah.createCookie("user-status","authenticated", 1));	 
	        		response.addCookie(monstah.createCookie("user-type",role, 1));
	        		
	        		//reset login attempts count
	        		String update = "UPDATE courseconfidential.users SET login_attempts= 0 WHERE email='"+email+"'";
	        		System.out.println(update);
	        		authQuery.executeUpdate(update);
	        		
	        		//successful login redirect to index page.
	                resObj = new JSONObject();
		        	resObj.put("loginstatus", "success");
		        	resObj.put("reason", "Login successful!");
		        	sendResponse(resObj.toJSONString(), response);
       	
	        }else if(status.equals("NOT FOUND")){
	        	//check if email exists: maybe user entered wrong pass and if so, increment login attempts for that user.
	        	String check = "SELECT * FROM courseconfidential.users user WHERE user.email='"+email+"'";
	        	System.out.println(check);
	        	ResultSet rs2 = authQuery.executeQuery(check);
	        	
	        	if(rs2.isBeforeFirst()){
	        		//not empty
	        		while(rs2.next()){
	        			logins = rs2.getInt("login_attempts");
	        		}
	        		logins++; //increment the login attempts and update the count for the user.
	        		String update = "UPDATE courseconfidential.users SET login_attempts="+logins+" WHERE email='"+email+"'";
	        		System.out.println(update);
	        		authQuery.executeUpdate(update);
	        		
	        		if(logins >= 5)
	    	        {	
	    	        	//Sends browser info about login attempt as follows:
	    	        	//{login-status:"fail", reason:"Too many login attempts, please reset password and try again"}
	        			//set status of user account to suspended.
	        			authQuery.executeUpdate("UPDATE courseconfidential.users SET status='SUSPENDED' WHERE email='"+email+"'");
	        			
	    	        	resObj = new JSONObject();
	    	        	resObj.put("loginstatus", "fail");
	    	        	resObj.put("reason", "Too many login attempts, account is now locked. Contact an administrator for further assistance");
	    	        	sendResponse(resObj.toJSONString(), response);
	    	        
	    	        }
	        		else{
	        			resObj = new JSONObject();
		        		resObj.put("loginstatus", "fail");
		        		resObj.put("reason", "Invalid email and/or password");
		        		sendResponse(resObj.toJSONString(), response);
	        		}		
	        	}
	        	else{
	        		//send a response that notifies that the user does not exist
		        	resObj = new JSONObject();
		        	resObj.put("loginstatus", "fail");
		        	resObj.put("reason", "User not found or does not exist.");
		        	sendResponse(resObj.toJSONString(), response);
	        	}	
	        }
	        	
			
		}catch (NoSuchAlgorithmException | InvalidKeySpecException | SQLException | ClassNotFoundException e){
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
	
	protected void getProfID(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String user_id = request.getParameter("user_id");
		Connection dbConnection = null;
		Statement profQuery = null;
		JSONObject resObj = null;
		
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			
			String query = "SELECT prof_id FROM courseconfidential.registered_professors WHERE idnum='"+user_id+"'";
			
	        dbConnection = DriverManager.getConnection(Credentials.DB_URL,Credentials.USERNAME,Credentials.PASSWORD);
	        
	        profQuery = dbConnection.createStatement();
	        
	        ResultSet rs = profQuery.executeQuery(query);
	        
	        if(rs.next())
	        {
				resObj = new JSONObject();
	        	resObj.put("professorfound", "success");
	        	resObj.put("profid", "'"+rs.getString("prof_id")+"'");
	        	sendResponse(resObj.toJSONString(), response);
	        	System.out.println("Sending professor ID...: " + rs.getString("prof_id"));
	        }

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
	
	protected void sendResponse(String output, HttpServletResponse response)throws ServletException, IOException
	{
		byte[] b = output.getBytes();
		ServletOutputStream outstream = response.getOutputStream();
		outstream.write(b);
		outstream.flush();
		outstream.close();
	}


}
