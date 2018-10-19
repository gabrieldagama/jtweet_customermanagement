package com.jtweet.usermanagement.security.filters;

import com.auth0.jwt.JWT;
import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jtweet.usermanagement.model.AppUser;
import com.jtweet.usermanagement.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Date;

import static com.jtweet.usermanagement.security.SecurityConstants.HEADER_STRING;
import static com.jtweet.usermanagement.security.SecurityConstants.SECRET;
import static com.jtweet.usermanagement.security.SecurityConstants.TOKEN_PREFIX;
import static com.jtweet.usermanagement.security.SecurityConstants.EXPIRATION_TIME;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	
	private UserRepository userRepository;
	
	private AuthenticationManager authenticationManager;
	
    public JwtAuthenticationFilter(AuthenticationManager authManager, UserRepository userRepository) {
		this.userRepository = userRepository;
        this.authenticationManager = authManager;
    }
	
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            AppUser applicationUser = new ObjectMapper().readValue(request.getInputStream(), AppUser.class);
        	
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
            			applicationUser.getUsername(),
            			applicationUser.getPassword()
            		));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
    	AppUser applicationUser = userRepository.findByUsername(((User) authResult.getPrincipal()).getUsername());
    	String token = JWT.create()
                .withSubject(applicationUser.getUsername())
                .withClaim("id", applicationUser.getId())
                .withClaim("email", applicationUser.getEmail())
                .withClaim("name", applicationUser.getName())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(HMAC512(SECRET.getBytes()));
    	response.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
    }
}