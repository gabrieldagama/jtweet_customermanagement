package com.jtweet.usermanagement.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jtweet.usermanagement.exception.UserAlreadyExistsException;
import com.jtweet.usermanagement.model.AppUser;
import com.jtweet.usermanagement.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public List<AppUser> getList()
	{
		List<AppUser> userList = new ArrayList<AppUser>();
		this.userRepository.findAll().forEach(userList::add);
		return userList;
	}
	
	public void createNewUser(AppUser user) throws UserAlreadyExistsException
	{
		if (this.userRepository.existsByEmail(user.getEmail())) {
			throw new UserAlreadyExistsException("The user email already exists in the database.", user.getEmail());
		}
		user.setPassword(this.passwordEncoder.encode(user.getPassword()));
		this.userRepository.save(user);
	}
	
	public void changePassword(AppUser user, String oldPassword, String newPassword) 
	{
		if (user.getPassword() == passwordEncoder.encode(oldPassword)) {
			user.setPassword(passwordEncoder.encode(newPassword));
			this.userRepository.save(user);
		}
	}
	
	public Boolean authenticate(AppUser user, String password)
	{
		return passwordEncoder.matches(password, user.getPassword());
	}
	
}
