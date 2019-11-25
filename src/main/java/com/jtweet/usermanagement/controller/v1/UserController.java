package com.jtweet.usermanagement.controller.v1;

import java.util.HashMap;

import com.jtweet.usermanagement.exception.UserNotFoundException;
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
	
	@GetMapping("/{id}")
	public ResponseEntity getUser(@PathVariable Integer id) {
		try {
			return ResponseEntity.status(HttpStatus.OK).body(userService.getById(id));
		} catch (UserNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
		}
	}
	
	@PostMapping("/create")
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
				return ResponseEntity.status(HttpStatus.OK).body(userService.generateToken(user));
			}
		} catch (UserNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login invalid.");
	}
}
