package com.courseconfidential.servlets;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.RequestContext;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import com.courseconfidential.core.CCFile;
import com.courseconfidential.core.CCUser;
import com.courseconfidential.utils.CCCookieMonster;
import com.courseconfidential.utils.Credentials;

/**
 * Servlet implementation class FileServlet
 */
@WebServlet("/FileServlet")
@MultipartConfig
public class FileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public FileServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		switch ((String) request.getParameter("request")) {
		case "file":
			getFile(request, response);
			break;
		case "getRelated":
			getRelated(request, response);
			break;
		case "image":
			System.out.println("Image download requested...");
			getFileImage(request, response);
			break;
		case "list-requests":
			System.out.println("Request list requested...");
			listAllRequests(request, response);
			break;
		case "download":
			downloadFile(request, response);
			break;

		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		switch ((String) request.getParameter("postrequest")) {
		case "addFile":
			System.out.println("postrequest");
			ProcessTemplate(request, response);
			break;
		case "file_request":
			System.out.println("File request processing...");
			storeRequest(request, response);
			break;
		
		}
	}

	protected void getFile(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String fileid = (String) request.getParameter("theuuid");
		System.out.println(fileid);
		String coursename = null;
		String filename = null;
		String uniname = null;
		int size = 0;
		int pages = 0;
		String description = null;
		String proffirst = null;
		String proflast = null;
		String term = null;
		int upvotes = 0;
		int downvotes = 0;
		Blob fileimg = null;

		Connection dbConnection = null;
		Statement authQuery = null;
		JSONObject resObj;

		try {

			Class.forName("com.mysql.jdbc.Driver");

			String query = "SELECT * FROM courseconfidential.master_file_list mfl WHERE mfl.uuid = '"
					+ fileid + "'";

			System.out.println(query);

			String query2 = "SELECT * FROM courseconfidential.course_file_list cfl, courseconfidential.master_courselist cl, courseconfidential.professor_list pl, courseconfidential.university_list u "
					+ "WHERE cfl.course_code = cl.course_code AND cfl.professor_id = pl.prof_id AND cfl.file_uuid = '"
					+ fileid + "' " + "AND u.code = pl.university_code";

			System.out.println(query2);

			String query3 = "SELECT * FROM courseconfidential.votes v WHERE v.id = '"
					+ fileid + "'";

			System.out.println(query3);

			dbConnection = DriverManager.getConnection(Credentials.DB_URL,
					Credentials.USERNAME, Credentials.PASSWORD);

			authQuery = dbConnection.createStatement();

			ResultSet rs = authQuery.executeQuery(query);

			while (rs.next()) {
				filename = rs.getString("name");
				size = rs.getInt("size");
				pages = rs.getInt("pages");
				description = rs.getString("description");
				fileimg = rs.getBlob("file_img");
			}

			ResultSet rs2 = authQuery.executeQuery(query2);

			while (rs2.next()) {
				term = rs2.getString("term");
				coursename = rs2.getString("course_name");
				uniname = rs2.getString("name");
				proffirst = rs2.getString("first_name");
				proflast = rs2.getString("last_name");
			}

			ResultSet rs3 = authQuery.executeQuery(query3);

			while (rs3.next()) {
				upvotes = rs3.getInt("vote_up");
				downvotes = rs3.getInt("vote_down");
			}

			resObj = new JSONObject();
			resObj.put("coursename", coursename);
			resObj.put("filename", filename);
			resObj.put("uniname", uniname);
			resObj.put("filesize", size / 1000);
			resObj.put("pages", pages);
			resObj.put("descrip", description);
			resObj.put("proffirst", proffirst);
			resObj.put("proflast", proflast);
			resObj.put("term", term);
			resObj.put("upvotes", upvotes);
			resObj.put("downvotes", downvotes);
			// resObj.put("fileimg", fileimg);

			sendResponse(resObj.toJSONString(), response);
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

	protected void getFileImage(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String fileid = (String) request.getParameter("uuid");
		String filename = null;
		String uniname = null;
		int size = 0;
		int pages = 0;
		String description = null;
		Blob fileimg = null;

		Connection dbConnection = null;
		Statement authQuery = null;
		JSONObject resObj;

		try {

			Class.forName("com.mysql.jdbc.Driver");

			String query = "SELECT * FROM courseconfidential.master_file_list mfl WHERE mfl.uuid = "
					+ fileid;

			System.out.println(query);

			dbConnection = DriverManager.getConnection(Credentials.DB_URL,
					Credentials.USERNAME, Credentials.PASSWORD);

			authQuery = dbConnection.createStatement();

			ResultSet rs = authQuery.executeQuery(query);

			while (rs.next()) {
				filename = rs.getString("name");
				size = rs.getInt("size");
				pages = rs.getInt("pages");
				description = rs.getString("description");
				fileimg = rs.getBlob("file_img");
			}

			sendImage(fileimg.getBytes(1, (int) fileimg.length()), filename,
					response);

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

	protected void getRelated(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String fileid = (String) request.getParameter("uuid");
		int pages = 0;
		String name = null;
		int up = 0;
		int down = 0;
		int size = 0;

		Connection dbConnection = null;
		Statement authQuery = null;
		JSONObject resObj;
		JSONArray fileList = new JSONArray();

		try {

			Class.forName("com.mysql.jdbc.Driver");

			String query = "SELECT * FROM courseconfidential.master_file_list mfl, courseconfidential.course_file_list c, courseconfidential.votes v "
					+ "WHERE mfl.uuid = c.file_uuid AND mfl.uuid = v.id AND mfl.uuid != '"+fileid+"' "
							+ "AND c.professor_id IN (SELECT cl.professor_id FROM courseconfidential.course_file_list cl "
							+ "WHERE cl.file_uuid = '"+fileid+"')";

			System.out.println(query);

			dbConnection = DriverManager.getConnection(Credentials.DB_URL,
					Credentials.USERNAME, Credentials.PASSWORD);

			authQuery = dbConnection.createStatement();

			ResultSet rs = authQuery.executeQuery(query);

			while (rs.next()) {
				name = rs.getString("name");
				size = rs.getInt("size");
				pages = rs.getInt("pages");
				up = rs.getInt("vote_up");
				down = rs.getInt("vote_down");
				
				JSONObject temp = new JSONObject();
				temp.put("name", name);
				temp.put("id", rs.getString("uuid"));
				temp.put("size", size/1000 + "KB");
				temp.put("pages", pages);
				temp.put("upvotes", up);
				temp.put("downvotes", down);
				fileList.add(temp);
			}
			System.out.println(fileList.toString());
			sendResponse(fileList.toString(), response);
			
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
	
	protected void ProcessTemplate(HttpServletRequest request,
			HttpServletResponse response) {
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (!isMultipart) {
			System.out.println("File is not multipartite!");
		}

		// create a factory for disk-based file items
		DiskFileItemFactory factory = new DiskFileItemFactory();

		// configure a repository to ensure a secure temp location is used
		ServletContext servcontext = this.getServletConfig()
				.getServletContext();
		File repository = (File) servcontext
				.getAttribute("javax.servlet.context.tempdir");
		factory.setRepository(repository);

		FileItem fileitem = null;

		// create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);

		try {
			// parse request
			/*
			 * List<FileItem> items = upload.parseRequest((RequestContext)
			 * request); Iterator<FileItem> iter = items.iterator();
			 * 
			 * while(iter.hasNext()){ fileitem = (FileItem) iter.next();
			 * if(!fileitem.isFormField()){ String name =
			 * fileitem.getFieldName(); String value = fileitem.getString();
			 * 
			 * InputStream uploadstream = fileitem.getInputStream(); //write to
			 * db? writeFile(uploadstream, request, response); }
			 * 
			 * }
			 */

			Part filePart = request.getPart("file"); // Retrieves <input
														// type="file"
														// name="file">
			Part fileImg = request.getPart("filescreenshot");
			writeFile(filePart, fileImg, request, response);

			System.out.println("File: " + filePart.getContentType() + " : "
					+ filePart.getSize());
			System.out.println(request.getParameter("postrequest"));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void writeFile(Part filepart, Part fileImg,
			HttpServletRequest request, HttpServletResponse response) {
		Connection dbConnection = null;
		// Statement authQuery = null;

		String category = request.getParameter("category");
		String description = request.getParameter("description");
		String type = request.getParameter("type");
		String pages = request.getParameter("pages");
		String name = request.getParameter("name");
		String university = request.getParameter("university");
		String course = request.getParameter("course");
		String professor = request.getParameter("professor");
		String term = request.getParameter("term");
		int size = (int) filepart.getSize();
		String profid = null;

		System.out.println(category + " : " + description + " : " + type
				+ " : " + pages + " : " + name + " : " + size + " : "
				+ professor + " : " + course + " : " + university);

		try {

			InputStream filecontent = filepart.getInputStream();
			InputStream fileimage = fileImg.getInputStream();

			Class.forName("com.mysql.jdbc.Driver");

			String query = "INSERT INTO courseconfidential.master_file_list (uuid, category, description, type, pages, name, size, file_data, file_img) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			// and into votes table

			dbConnection = DriverManager.getConnection(Credentials.DB_URL,
					Credentials.USERNAME, Credentials.PASSWORD);

			PreparedStatement statement = dbConnection.prepareStatement(query);
			String uuid = UUID.randomUUID().toString();
			statement.setString(1, uuid);
			statement.setString(2, request.getParameter("category"));
			statement.setString(3, request.getParameter("description"));
			statement.setString(4, request.getParameter("type"));
			statement.setString(5, request.getParameter("pages"));
			statement.setString(6, request.getParameter("name"));
			statement.setInt(7, size);
			statement.setBlob(8, filecontent);
			statement.setBlob(9, fileimage);

			statement.executeUpdate();

			// insert into course file list as well!
			// get the course code associated with file
			String tempq = "SELECT cl.course_code FROM courseconfidential.master_courselist cl WHERE cl.course_name = '"
					+ course + "'";
			Statement authQuery = dbConnection.createStatement();
			ResultSet rs = authQuery.executeQuery(tempq);
			while (rs.next()) {
				course = rs.getString("course_code");
			}
			// get the profid of the associated prof
			String[] pname = professor.split(" ");
			String tempq2 = "SELECT pl.prof_id FROM courseconfidential.professor_list pl WHERE pl.first_name = '"
					+ pname[0] + "' " + "AND pl.last_name = '" + pname[1] + "'";
			ResultSet rs2 = authQuery.executeQuery(tempq2);
			while (rs2.next()) {
				profid = rs2.getString("prof_id");
			}

			// insert into the course file list table
			String query2 = "INSERT INTO courseconfidential.course_file_list (file_uuid, professor_id, course_code, term) values (?, ?, ?, ?)";
			PreparedStatement statement2 = dbConnection
					.prepareStatement(query2);
			statement2.setString(1, uuid);
			statement2.setString(2, profid);
			statement2.setString(3, course);
			statement2.setString(4, term);
			statement2.executeUpdate();

			// insert row into votes table that will be initialized with 0
			String query3 = "INSERT INTO courseconfidential.votes (id, vote_up, vote_down) values (?, ?, ?)";
			PreparedStatement statement3 = dbConnection
					.prepareStatement(query3);
			statement3.setString(1, uuid);
			statement3.setInt(2, 0);
			statement3.setInt(3, 0);
			statement3.executeUpdate();

		} catch (SQLException | ClassNotFoundException | IOException e) {
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

	protected CCFile retrieveFile(String ID) {
		CCFile file = null;

		Connection dbConnection;

		Statement statement = null;

		String query = "SELECT * FROM courseconfidential.master_file_list WHERE uuid='"
				+ ID + "'";

		try {
			// Register JDBC Driver
			Class.forName("com.mysql.jdbc.Driver");

			// Open Connections
			dbConnection = DriverManager.getConnection(Credentials.DB_URL,
					Credentials.USERNAME, Credentials.PASSWORD);

			// create statement
			statement = dbConnection.createStatement();

			// System.out.println("\tConnected to Database. Accessing Table"+db.toString());
			// System.out.println("\tSending Query:\n"+query);
			// execute

			ResultSet rs = statement.executeQuery(query);

			// build a list of server tasks

			while (rs.next()) {
				Blob blob = rs.getBlob("file_data");
				file = new CCFile(rs.getString("uuid"), rs.getString("name"),
						rs.getString("type"), blob.getBytes(1,
								(int) blob.length()));
			}

			rs.close();
			statement.close();
			dbConnection.close();
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
		}

		return file;
	}

	protected void sendImage(byte[] image, String imageName,
			HttpServletResponse response) throws ServletException, IOException {

		response.setContentType(getServletContext().getMimeType(imageName));
		response.setContentLength(image.length);
		response.getOutputStream().write(image);

	}

	protected void sendFile(CCFile file, HttpServletResponse response)
			throws ServletException, IOException {
		String name = file.getFilename();
		String type = file.getFiletype();
		String contentType = file.getContentHeader();

		// clear the response headers

		response.reset();
		response.setContentType(contentType);

		// add header information to response object

		System.out.println("Sending file...: " + name + type);

		response.addHeader("Content-Disposition", "attachment; filename="+ name + "." + type);
		OutputStream output = response.getOutputStream();
		output.write(file.getData());
		output.close();
	}

	protected void downloadFile(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Initiating file download");
		String ID = request.getParameter("ID");
			ID = ID.replace("'", "");
		System.out.println("File ID parameter is: " + ID);
		sendFile(retrieveFile(ID), response);
		JSONObject success = new JSONObject();
			success.put("status", "success");
		 sendResponse(success.toString(), response);
	}

	protected void storeRequest(HttpServletRequest request,
			HttpServletResponse response) {
		String username = request.getParameter("username");
		String description = request.getParameter("description");
		String type = request.getParameter("type");
		String university = request.getParameter("university");
		String professor = request.getParameter("professor");
		String course = request.getParameter("courses");
		String term = request.getParameter("term");
		String professor_id = null;
		String course_code = null;

		String retval[] = professor.split(" ", 2);
		String first_name = retval[0];
		String last_name = retval[1];

		CCUser userCheck = new CCUser();

		String request_id = userCheck.generateId();

		System.out.println("New generated request ID is: " + request_id);

		Connection dbConnection = null;
		PreparedStatement requestInsert = null;
		PreparedStatement request_listInsert = null;
		Statement profQuery = null;

		String insert_file_request = "INSERT INTO courseconfidential.file_requests"
				+ "(request_id, username, description, file_type) VALUES"
				+ "(?,?,?,?)";

		String insert_request_list = "INSERT INTO courseconfidential.file_request_list"
				+ "(request_id, professor_id, course_code, term) VALUES"
				+ "(?,?,?,?)";

		try {
			Class.forName("com.mysql.jdbc.Driver");
			dbConnection = DriverManager.getConnection(Credentials.DB_URL,
					Credentials.USERNAME, Credentials.PASSWORD);

			String query = "SELECT *"
					+ " FROM"
					+ " (SELECT university_code, course_code"
					+ " FROM courseconfidential.university_list uni, courseconfidential.master_courselist mclist"
					+ " WHERE uni.name='"
					+ university
					+ "' AND mclist.course_name ='"
					+ course
					+ "') AS matching_courses, "
					+ "courseconfidential.professor_list plist, courseconfidential.courselist cl"
					+ " WHERE plist.prof_id = cl.professor_id AND matching_courses.course_code = cl.course_code AND "
					+ "plist.university_code = matching_courses.university_code AND plist.first_name='"
					+ first_name + "' AND plist.last_name='" + last_name + "'";

			profQuery = dbConnection.createStatement();
			ResultSet rs = profQuery.executeQuery(query);
			while (rs.next()) {
				professor_id = rs.getString("prof_id");
				System.out.println("Current Professor ID: " + professor_id);
				course_code = rs.getString("course_code");
				System.out.println("Current course code: " + course_code);
			}

			requestInsert = dbConnection.prepareStatement(insert_file_request);

			requestInsert.setString(1, request_id);
			requestInsert.setString(2, username);
			requestInsert.setString(3, description);
			requestInsert.setString(4, type);

			System.out
					.println("requestInsert table prepared statement created.");

			request_listInsert = dbConnection
					.prepareStatement(insert_request_list);

			request_listInsert.setString(1, request_id);
			request_listInsert.setString(2, professor_id);
			request_listInsert.setString(3, course_code);
			request_listInsert.setString(4, term);

			System.out
					.println("request_listInsert table prepared statement created.");
			// execute insert SQL statements
			requestInsert.executeUpdate();
			request_listInsert.executeUpdate();

			System.out
					.println("Records are inserted into file_requests table!");
			System.out
					.println("Records are inserted into file_request_list table!");

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

	protected void listAllRequests(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Connection dbConnection = null;
		Statement authQuery = null;

		// response object to send to the user

		try {

			Class.forName("com.mysql.jdbc.Driver");

			String query = "SELECT l.username, l.description, l.file_type, u.name, c.course_name, p.first_name, p.last_name, r.term"
					+ " FROM courseconfidential.file_request_list r, courseconfidential.file_requests l, courseconfidential.professor_list p, courseconfidential.master_courselist c, "
					+ "courseconfidential.university_list u"
					+ " WHERE r.request_id = l.request_id AND r.professor_id = p.prof_id AND r.course_code = c.course_code AND u.code = c.university_code";

			dbConnection = DriverManager.getConnection(Credentials.DB_URL,
					Credentials.USERNAME, Credentials.PASSWORD);

			authQuery = dbConnection.createStatement();

			System.out.println(query);

			ResultSet rs = authQuery.executeQuery(query);

			JSONArray resList = new JSONArray();

			while (rs.next()) {
				JSONObject curr = new JSONObject();
				curr.put("request_username", rs.getString("username"));
				curr.put("request_desc", rs.getString("description"));
				curr.put("request_type", rs.getString("file_type"));
				curr.put("request_name", rs.getString("name"));
				curr.put("request_course_name", rs.getString("course_name"));
				curr.put("request_first_name", rs.getString("first_name"));
				curr.put("request_last_name", rs.getString("last_name"));
				curr.put("request_term", rs.getString("term"));
				resList.add(curr);
			}

			System.out.println(resList.toString());
			sendResponse(resList.toString(), response);

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

}
