package com.courseconfidential.core;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.courseconfidential.utils.Credentials;

public class CCUser extends User{

	/**
	 * @author Christoper Jones
	 * Adds utility methods to the Course Confidential User class
	 * Last Update: 2 - 4 - 2015
	 */
	
	public CCUser()
	{
		
	}
	
	/**
	 * Generates a  user id and then returns the id after
	 * confirming that the new created id does not already exist in the user database
	 * @return
	 */
	public String generateUserId()
	{
		String result = generateId();
		    
		while(userExists(result))
		{
			result = generateId();
		}
		    
		return result;
	}
	
	/**
	 * Generates a 10 character user id by taking 5 characters from the two md5 hashes
	 * and combining them to make a user id and returns the id
	 * @return
	 */
	public String generateId()
	{
		try 
		{
			SecureRandom prng = SecureRandom.getInstance("SHA1PRNG");

		    //generate a random number
		    String randomNum1 = new Integer(prng.nextInt()).toString();
		    String randomNum2 = new Integer(prng.nextInt()).toString();
		        
		    //get its digest
		    MessageDigest sha = MessageDigest.getInstance("MD5");
		      
		    byte[] r1 =  sha.digest(randomNum1.getBytes());
		    byte[] r2 =  sha.digest(randomNum2.getBytes());
		    
		    String result = hexEncode(r1).substring(0, 5) + hexEncode(r2).substring(0, 5);
		    
		    return result;
		}
		catch (NoSuchAlgorithmException ex) 
		{
			System.err.println(ex);
			return null;
		}    
	}
	
	/**
	 * Connects to the Course Confidential database and determines whether a user
	 * with the given user id exists
	 * @param id
	 * @return
	 */
	public boolean userExists(String id)
	{
		Connection dbConnection = null;
		Statement authQuery = null;
		boolean found = false;
		
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			
			String query = "SELECT * FROM courseconfidential.users WHERE idnum='"+id+"'";
			
	        dbConnection = DriverManager.getConnection(Credentials.DB_URL,Credentials.USERNAME,Credentials.PASSWORD);
	        
	        authQuery = dbConnection.createStatement();
	        
	        ResultSet rs = authQuery.executeQuery(query);
	        
	        if(rs.next())
	        {
	        	found = true;
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
		return found;
	}
	
	
	/**
	 * Connects to the Course Confidential database and determines whether a user
	 * with the given user id, username, or email exists
	 * @param id
	 * @param newEmail
	 * @return 0 if user not found, 1 if email is found, 2 if username is found
	 */
	public int userExists(String id, String newEmail, String newUsername)
	{
		Connection dbConnection = null;
		Statement authQuery = null;
		String email = null;
		String username = null;
		
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			
			String query = "SELECT * FROM courseconfidential.users WHERE idnum='"+id+"' OR email='"+newEmail+"' OR username='"+newUsername+"'";
			
	        dbConnection = DriverManager.getConnection(Credentials.DB_URL,Credentials.USERNAME,Credentials.PASSWORD);
	        
	        authQuery = dbConnection.createStatement();
	        
	        ResultSet rs = authQuery.executeQuery(query);
	        
	        if(rs.next())
	        {
	        	email = rs.getString("email");
	        	username = rs.getString("username");
	        	if(newEmail.equals(email))
	        		return 1;
	        	if(newUsername.equals(username))
	        		return 2;
	        }
	        else if(!newEmail.equals(email) && !newUsername.equals(username))
	        {
	        	return 0;
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
		return 0;
	}
	
	
	private String hexEncode(byte[] aInput)
    {
	    StringBuilder result = new StringBuilder();
	    char[] digits = {'0', '1', '2', '3', '4','5','6','7','8','9','a','b','c','d','e','f'};
	    for (int idx = 0; idx < aInput.length; ++idx) 
	    {
	      byte b = aInput[idx];
	      result.append(digits[ (b&0xf0) >> 4 ]);
	      result.append(digits[ b&0x0f]);
	    }
    return result.toString();
  }
}
