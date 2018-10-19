package com.jtweet.usermanagement.model;

public class TokenUserDetails extends AppUser{
    private String token;
    
	public TokenUserDetails(String name, String email, String username, String password, String token)
	{
		super(name, email, username,password);
        this.token = token;
	}
	
    public String getToken() {
        return token;
    }
}
