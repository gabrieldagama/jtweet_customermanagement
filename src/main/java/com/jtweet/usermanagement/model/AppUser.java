package com.jtweet.usermanagement.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import javax.validation.constraints.Email;

@Entity
@Table(uniqueConstraints={@UniqueConstraint(columnNames = {"email","username"})})
public class AppUser {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	@NotNull(message = "Name must be present")
	private String name;
	@NotNull(message = "Email must be present")
	@Email
	private String email;
	@NotNull(message = "Username must be present")
	private String username;
	@NotNull(message = "Password must be present")
	private String password;
    public static final String role = "USER";

	public AppUser() {
	}
	
	public AppUser(String name, String email, String username, String password)
	{
		super();
		this.name = name;
		this.email = email;
		this.username = username;
		this.password = password;
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
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

	public String getRole() {
		return AppUser.role;
	}
	
}
