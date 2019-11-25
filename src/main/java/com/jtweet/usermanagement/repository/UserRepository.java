package com.jtweet.usermanagement.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.jtweet.usermanagement.model.AppUser;

import java.util.Optional;

public interface UserRepository extends PagingAndSortingRepository<AppUser, Integer> {
	Optional<AppUser> findByUsername(String username);
	Boolean existsByEmail(String email);
}
