package com.jtweet.usermanagement.exception;

public class UserAlreadyExistsException extends Exception {

	private static final long serialVersionUID = 6591298817199636501L;
	
	private String email;

	public UserAlreadyExistsException(String message) {
		super(message);
	}

	public UserAlreadyExistsException(String message, String email) {
		super(message);
		this.email = email;
	}

	public UserAlreadyExistsException(Throwable cause, String email) {
		super(cause);
		this.email = email;
	}

	public UserAlreadyExistsException(String message, Throwable cause, String email) {
		super(message, cause);
		this.email = email;
	}

	public String getEmail() {
		return email;
	}
}
