package com.jtweet.usermanagement.service;

import com.auth0.jwt.JWT;
import com.jtweet.usermanagement.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jtweet.usermanagement.exception.UserAlreadyExistsException;
import com.jtweet.usermanagement.model.AppUser;
import com.jtweet.usermanagement.repository.UserRepository;

import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC256;
import static com.jtweet.usermanagement.security.SecurityConstants.EXPIRATION_TIME;
import static com.jtweet.usermanagement.security.SecurityConstants.SECRET;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;


	@Override
	public AppUser getById(Integer id) throws UserNotFoundException {
		if (!userRepository.findById(id).isPresent()) {
			throw new UserNotFoundException("The user with id "+id+" was not found");
		}
		return userRepository.findById(id).get();
	}

	@Override
	public AppUser getByUsername(String username) throws UserNotFoundException {
		if (!userRepository.findByUsername(username).isPresent()) {
			throw new UserNotFoundException("The user with username "+username+" was not found");
		}
		return userRepository.findByUsername(username).get();
	}

	@Override
	public String generateToken(AppUser user) {
		return JWT.create()
				.withSubject(user.getUsername())
				.withClaim("id", user.getId())
				.withClaim("email", user.getEmail())
				.withClaim("name", user.getName())
				.withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.sign(HMAC256(SECRET.getBytes()));
	}

	@Override
	public void createUser(AppUser user) throws UserAlreadyExistsException {
		if (userRepository.existsByEmail(user.getEmail())) {
			throw new UserAlreadyExistsException("User email already exists", user.getEmail());
		}
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		this.userRepository.save(user);
	}

	@Override
	public void changePassword(AppUser user, String newPassword) {
		user.setPassword(passwordEncoder.encode(newPassword));
		this.userRepository.save(user);
	}

	@Override
	public Boolean authenticate(AppUser user, String password) {
		return passwordEncoder.matches(password, user.getPassword());
	}
}
