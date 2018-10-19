package com.jtweet.usermanagement.repository;

import org.springframework.data.repository.CrudRepository;

import com.jtweet.usermanagement.model.AppUser;

public interface UserRepository extends CrudRepository<AppUser, Integer> {
	AppUser findByEmail(String email);
	AppUser findByUsername(String username);
	Boolean existsByEmail(String email);
}
