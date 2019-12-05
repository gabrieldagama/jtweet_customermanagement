package com.jtweet.usermanagement.controller.v1;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.jtweet.usermanagement.exception.UserNotFoundException;
import com.jtweet.usermanagement.service.JwtServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jtweet.usermanagement.service.UserServiceImpl;
import com.jtweet.usermanagement.exception.UserAlreadyExistsException;
import com.jtweet.usermanagement.model.AppUser;

@RestController
@RequestMapping("/v1/users")
public class UserController {
	
	@Autowired
	private UserServiceImpl userService;

	@Autowired
	private JwtServiceImpl jwtService;
	
	@GetMapping("/{id}")
	public ResponseEntity getUser(@PathVariable Integer id) {
		try {
			AppUser user = userService.getById(id);
			return ResponseEntity.status(HttpStatus.OK).body(user.toApiFormat());
		} catch (UserNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
		}
	}
	
	@PostMapping("/")
	public ResponseEntity createUser(@RequestBody AppUser user) {
		try {
			this.userService.createUser(user);
			return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
		} catch (UserAlreadyExistsException e) {
            return ResponseEntity.badRequest().body("User already exists");
		}
	}
	
	@PostMapping("/login")
	public ResponseEntity login(@RequestBody HashMap<String,String> body) {
		try {
			AppUser user = this.userService.getByUsername(body.get("username"));
			if (this.userService.authenticate(user, body.get("password"))) {
				return ResponseEntity.status(HttpStatus.OK).body(jwtService.generateToken(user));
			}
		} catch (UserNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid login.");
	}

	@PostMapping("/validateJwt")
	public ResponseEntity validate(@RequestBody String jwt) {
		try {
			DecodedJWT decodedJWT = jwtService.validateToken(jwt);
			AppUser user = userService.getByUsername(decodedJWT.getSubject());
			return ResponseEntity.status(HttpStatus.OK).body(user);
		} catch (JWTVerificationException | UnsupportedEncodingException exception) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Token.");
		} catch (UserNotFoundException exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
		}
	}
}
