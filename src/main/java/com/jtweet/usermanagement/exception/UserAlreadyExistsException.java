package com.jtweet.usermanagement.exception;

public class UserAlreadyExistsException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6591298817199636501L;
	
	private String email;

	public UserAlreadyExistsException(String message, String email)
	{
		super(message);
		this.setEmail(email);
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}
