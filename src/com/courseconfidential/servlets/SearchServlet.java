package com.courseconfidential.servlets;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import com.courseconfidential.utils.CCCookieMonster;
import com.courseconfidential.utils.Credentials;

/**
 * Servlet implementation class SearchServlet
 */
@WebServlet("/SearchServlet")
public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		switch((String)request.getParameter("query"))
		{
			case "list-all-universities":
				listUniversities(request, response);
			break;
			
			case "list-all-courses":
				listCourses(request, response);
			break;
			case "list-professor":
				listProfessor(request,response);
			break;
			case "list-term":
				listTerm(request,response);
			break;
			case "course-search":
				searchCourse(request,response);
			break;
			case "proff-search":
				proffSearch(request,response);
			break;
				
		}
	}
	
	protected void proffSearch(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
		Connection dbConnection = null;
		Statement listQuery = null;
		
		String professor = (String)request.getParameter("professor");
		String university = (String)request.getParameter("school");
		
		//get this profs name	
		String[] proffName = professor.split(" ", 2);
			
			String proffirst = proffName[0];
			String profflast = proffName[1];
		
		
		
		try{
			
			Class.forName("com.mysql.jdbc.Driver");
			
			String query = "SELECT prof_id "+
						   "FROM courseconfidential.professor_list pl "+
						   "WHERE pl.first_name = '"+proffirst+"' AND pl.last_name ='"+profflast+"' AND pl.university_code IN "+
						   "(SELECT code FROM courseconfidential.university_list ul WHERE ul.name = '"+university+"')";
			
			System.out.println(query);
			
	        dbConnection = DriverManager.getConnection(Credentials.DB_URL,Credentials.USERNAME,Credentials.PASSWORD);
	        
	        listQuery = dbConnection.createStatement();
	        
	        ResultSet rs = listQuery.executeQuery(query);
	        	        
	        JSONObject result = new JSONObject();
	        while(rs.next())
	        {
	        	result.put("profid", rs.getString("prof_id"));
	        }
	        
	        System.out.println(result.toString());
	        sendResponse(result.toString(),response);
	        
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
	
	protected void searchCourse(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
		
		String university = (String)request.getParameter("school");
		String course = (String)request.getParameter("course");
		String professor = (String)request.getParameter("professor");
		String term = (String)request.getParameter("terms");
		
		System.out.println("University :"+university+" course: "+course+" professor: "+professor+" term: "+term);
		
		//get this profs name	
		String[] proffName = professor.split(" ", 2);
			
			String proffirst = proffName[0];
			String profflast = proffName[1];


		Connection dbConnection = null;
		Statement listQuery = null;
		
		String query ="SELECT uuid, professor_id, course_files.course_code, course_files.term, category, description, type, pages, size, name, course_canonical, course_name, first_name, last_name, university_code"+
					  " FROM courseconfidential.course_file_list course_files, courseconfidential.master_file_list m_files, "+
					  "(SELECT m_course.course_code,course_canonical,course_name,prof_id, first_name, last_name, proflist.university_code, term"+
					  " FROM courseconfidential.master_courselist m_course, courseconfidential.professor_list proflist, courseconfidential.courselist course"+
					  " WHERE m_course.course_name='"+course+"' "+
					  	" AND proflist.last_name ='"+profflast+"' "+
					  	" AND proflist.first_name='"+proffirst+"' "+
					  	" AND course.course_code = m_course.course_code "+
					  	" AND course.term='"+term+"'"+
					  	" AND course.professor_id = proflist.prof_id) AS search"+
					  	" WHERE course_files.file_uuid = m_files.uuid AND search.course_code = course_files.course_code";
		try{
			
			Class.forName("com.mysql.jdbc.Driver");
		
			System.out.println(query);
			
	        dbConnection = DriverManager.getConnection(Credentials.DB_URL,Credentials.USERNAME,Credentials.PASSWORD);
	        
	        listQuery = dbConnection.createStatement();
	        
	        ResultSet rs = listQuery.executeQuery(query);
	        
	        JSONArray fileList = new JSONArray();
	        
	        
	        while(rs.next())
	        {
	        	JSONObject curr = new JSONObject();
	        	curr.put("uuid", rs.getString("uuid"));
	        	curr.put("professorid", rs.getString("professor_id"));
	        	curr.put("courseid", rs.getString("course_code"));
	        	curr.put("term", rs.getString("term"));
	        	curr.put("category", rs.getString("category"));
	        	curr.put("description", rs.getString("description"));
	        	curr.put("type", rs.getString("type"));
	        	curr.put("pages", rs.getInt("pages"));
	        	curr.put("size", rs.getString("size"));
	        	curr.put("name", rs.getString("name"));
	        	curr.put("coursecanonical", rs.getString("course_canonical"));
	        	curr.put("coursename", rs.getString("course_name"));
	        	curr.put("proffirst", rs.getString("first_name"));
	        	curr.put("proflast", rs.getString("last_name"));
	        	curr.put("university",rs.getString("university_code"));
	        	fileList.add(curr);

	        }
	        
	        System.out.println(fileList.toString());
	        sendResponse(fileList.toString(),response);
	        
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
	
	protected void listTerm(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
		String university = (String)request.getParameter("school");
		String course = (String)request.getParameter("course");
		String professor = (String)request.getParameter("professor");
		
		String[] result = professor.split("\\s");
		
		String first = result[0];
		String last = result[1];
		
		Connection dbConnection = null;
		Statement listQuery = null;
		
		try{
			
			Class.forName("com.mysql.jdbc.Driver");
			
			String query = "SELECT term"+
					" FROM "+
					"(SELECT *"+ 
					 " FROM courseconfidential.master_courselist mcl"+
					 " WHERE mcl.course_name='"+course+"') AS course,"+
					 " (SELECT *"+
					 " FROM courseconfidential.professor_list pl"+
					 " WHERE pl.first_name ='"+first+"' AND pl.last_name= '"+last+"') AS prof,"+
					 " courseconfidential.courselist cl"+
					 " WHERE course.course_code=cl.course_code AND prof.prof_id = cl.professor_id"+
					 " GROUP BY term";
			
			System.out.println(query);
			
	        dbConnection = DriverManager.getConnection(Credentials.DB_URL,Credentials.USERNAME,Credentials.PASSWORD);
	        
	        listQuery = dbConnection.createStatement();
	        
	        ResultSet rs = listQuery.executeQuery(query);
	        
	        JSONArray courseList = new JSONArray();
	        
	        
	        while(rs.next())
	        {
	        	JSONObject curr = new JSONObject();
	        	curr.put("term", rs.getString("term"));
	        	courseList.add(curr);
	        }
	        
	        System.out.println(courseList.toString());
	        sendResponse(courseList.toString(),response);
	        
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
	//retrieve a list of all the available professors that teach the matching university courses
	protected void listProfessor(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{

		String university = (String)request.getParameter("school");
		String course = (String)request.getParameter("course");
				
		Connection dbConnection = null;
		Statement listQuery = null;
		
		String professor = null;
		
		try{
			
			Class.forName("com.mysql.jdbc.Driver");
			
			String query = "SELECT *"+
						   " FROM"+
						   " (SELECT university_code, course_code"+ 
							 " FROM courseconfidential.university_list uni, courseconfidential.master_courselist mclist"+
							 " WHERE uni.name='"+university+"' AND mclist.course_name ='"+course+"') AS matching_courses, "+
							 "courseconfidential.professor_list plist, courseconfidential.courselist cl"+
							 " WHERE plist.prof_id = cl.professor_id AND matching_courses.course_code = cl.course_code AND plist.university_code = matching_courses.university_code";
			
			System.out.println(query);
			
	        dbConnection = DriverManager.getConnection(Credentials.DB_URL,Credentials.USERNAME,Credentials.PASSWORD);
	        
	        listQuery = dbConnection.createStatement();
	        
	        ResultSet rs = listQuery.executeQuery(query);
	        
	        JSONArray courseList = new JSONArray();
	        
	        
	        while(rs.next())
	        {
	          professor = rs.getString("first_name")+" "+rs.getString("last_name");
	          JSONObject curr = new JSONObject();
	          curr.put("professor", professor);
	          courseList.add(curr);
	        }
	        
	        System.out.println(courseList.toString());
	        sendResponse(courseList.toString(),response);
	        
	        
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	
	//retrieve a list of all the available courses for the matching university
	protected void listCourses(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
		
		String university = (String)request.getParameter("school");
		
		Connection dbConnection = null;
		Statement listQuery = null;
		
		try{
			
			Class.forName("com.mysql.jdbc.Driver");
			
			String query = "SELECT * FROM (SELECT code FROM courseconfidential.university_list WHERE name LIKE '%"+university+"%') AS uni, courseconfidential.master_courselist AS courses WHERE courses.university_code = uni.code  ORDER BY course_name";
			
			System.out.println(query);
			
	        dbConnection = DriverManager.getConnection(Credentials.DB_URL,Credentials.USERNAME,Credentials.PASSWORD);
	        
	        listQuery = dbConnection.createStatement();
	        
	        ResultSet rs = listQuery.executeQuery(query);
	        
	        JSONArray courseList = new JSONArray();
	        
	        while(rs.next())
	        {
	        	JSONObject curr = new JSONObject();
	        		curr.put("code", rs.getString("course_code"));
	        		curr.put("name",rs.getString("course_name"));
	        		curr.put("university",rs.getString("university_code"));
	        	
	        		courseList.add(curr);
	        }
	        System.out.println(courseList.toString());
	        sendResponse(courseList.toString(),response);
			
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
	
	protected void listUniversities(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
		Connection dbConnection = null;
		Statement listQuery = null;
		
		try{
			
			Class.forName("com.mysql.jdbc.Driver");
			
			String query = "SELECT * FROM courseconfidential.university_list";
			
	        dbConnection = DriverManager.getConnection(Credentials.DB_URL,Credentials.USERNAME,Credentials.PASSWORD);
	        
	        listQuery = dbConnection.createStatement();
	        
	        ResultSet rs = listQuery.executeQuery(query);
	        
	        JSONArray uniList = new JSONArray();
	        
	        while(rs.next())
	        {
	        	JSONObject curr = new JSONObject();
	        		curr.put("code", rs.getString("code"));
	        		curr.put("name",rs.getString("name"));
	        		curr.put("city", rs.getString("city"));
	        		curr.put("state", rs.getString("state"));
	        	
	        	uniList.add(curr);
	        }
	        System.out.println(uniList.toString());
	        sendResponse(uniList.toString(),response);
			
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
