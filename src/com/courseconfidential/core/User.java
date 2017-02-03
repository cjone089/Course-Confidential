package com.courseconfidential.core;

public abstract class User {

	String acctStatus;
	String acctId;
	String username;
	String password;
	String firstName;
	String lastName;
	String role;
	
	/**
	 * @author D'Mita Levy
	 * Represents the basic details of user account for the Course Confidential site
	 * Last Update: 1 - 31 - 2015 
	 */
	
	//default constructor
	public User()
	{
		
	}

	public String getAcctStatus() {
		return acctStatus;
	}

	public void setAcctStatus(String acctStatus) {
		this.acctStatus = acctStatus;
	}

	public String getAcctId() {
		return acctId;
	}

	public void setAcctId(String acctId) {
		this.acctId = acctId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}
