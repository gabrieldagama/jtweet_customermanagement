package com.jtweet.usermanagement.service;

import com.jtweet.usermanagement.exception.UserAlreadyExistsException;
import com.jtweet.usermanagement.exception.UserNotFoundException;
import com.jtweet.usermanagement.model.AppUser;

public interface UserService {
    public void createUser(AppUser user) throws UserAlreadyExistsException;
    public void changePassword(AppUser user, String newPassword);
    public AppUser getById(Integer id) throws UserNotFoundException;
    public AppUser getByUsername(String username) throws UserNotFoundException;
    public Boolean authenticate(AppUser user, String password);
    public String generateToken(AppUser user);
}
