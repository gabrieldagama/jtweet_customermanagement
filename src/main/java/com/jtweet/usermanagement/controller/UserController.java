package com.jtweet.usermanagement.controller;

import static com.jtweet.usermanagement.security.SecurityConstants.SECRET;
import static com.jtweet.usermanagement.security.SecurityConstants.TOKEN_PREFIX;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jtweet.usermanagement.service.UserService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import static com.auth0.jwt.algorithms.Algorithm.HMAC256;
import com.jtweet.usermanagement.api.ResponseBody;
import com.jtweet.usermanagement.api.ResponseBodyConverter;
import com.jtweet.usermanagement.exception.UserAlreadyExistsException;
import com.jtweet.usermanagement.model.TokenUserDetails;
import com.jtweet.usermanagement.model.AppUser;
import com.jtweet.usermanagement.repository.UserRepository;

import static com.jtweet.usermanagement.security.SecurityConstants.HEADER_STRING;
import static com.jtweet.usermanagement.security.SecurityConstants.SECRET;
import static com.jtweet.usermanagement.security.SecurityConstants.TOKEN_PREFIX;
import static com.jtweet.usermanagement.security.SecurityConstants.EXPIRATION_TIME;

@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private  UserRepository userRepository;
	
	@GetMapping
	public List<AppUser> listUsers()
	{
		return this.userService.getList();
	}
	
	@GetMapping("/{id}")
	public AppUser getUser(@PathVariable Integer id)
	{
		return this.userRepository.findById(id).get();
	}
	
	@PostMapping("/create")
	public ResponseEntity createUser(@RequestBody AppUser user)
	{
		try {
			this.userService.createNewUser(user);
		} catch (UserAlreadyExistsException exception) {
            return ResponseEntity.badRequest().body(exception);
		}
		ResponseBody responseBody = new ResponseBody(true, "User created successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseBodyConverter.convert(responseBody));
	}
	
	@PostMapping("/login")
	public String validateToken(@RequestBody HashMap<String,String> body)
	{
		AppUser user = this.userRepository.findByUsername(body.get("username"));
		if (this.userService.authenticate(user, body.get("password"))) {
	    	return JWT.create()
	                .withSubject(user.getUsername())
	                .withClaim("id", user.getId())
	                .withClaim("email", user.getEmail())
	                .withClaim("name", user.getName())
	                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
	                .sign(HMAC256(SECRET.getBytes()));
		}
		return "Login invalid.";
	}
}
