package com.jtweet.usermanagement.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.jtweet.usermanagement.exception.UserNotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.jtweet.usermanagement.exception.UserAlreadyExistsException;
import com.jtweet.usermanagement.model.AppUser;
import com.jtweet.usermanagement.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserServiceTest {

	private final int userId = 1;
	private final String email = "gabriel@gabrielgama.com.br";
	private final String name = "Gabriel";
	private final String username = "gabrieldagama";
	private final String password = "test123";

    @InjectMocks
    private UserServiceImpl userService;
    
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Before
    public void setUp() {
    	MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetById() throws UserNotFoundException {
        AppUser user = buildUserObject();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        AppUser returnedUser = userService.getById(userId);
        verify(userRepository, times(2)).findById(userId);
        Assert.assertEquals(user, returnedUser);
    }

    @Test(expected = UserNotFoundException.class)
    public void testGetByIdException() throws UserNotFoundException {
        Optional<AppUser> optionalUser = Optional.empty();
        when(userRepository.findById(userId)).thenReturn(optionalUser);
        userService.getById(userId);
    }

    @Test
    public void testGetByUsername() throws UserNotFoundException {
        AppUser user = buildUserObject();
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        AppUser returnedUser = userService.getByUsername(username);
        verify(userRepository, times(2)).findByUsername(username);
        Assert.assertEquals(user, returnedUser);
    }

    @Test(expected = UserNotFoundException.class)
    public void testGetByUsernameException() throws UserNotFoundException {
        Optional<AppUser> optionalUser = Optional.empty();
        when(userRepository.findByUsername(username)).thenReturn(optionalUser);
        userService.getByUsername(username);
    }

    @Test
    public void createUserSuccessTest() throws UserAlreadyExistsException {
    	AppUser user = buildUserObject();
    	when(userRepository.existsByEmail(this.email)).thenReturn(false);
        when(passwordEncoder.encode(this.password)).thenReturn("encodedpwd");
    	userService.createUser(user);
    	verify(userRepository, times(1)).save(user);
    }
   
    @Test(expected = UserAlreadyExistsException.class)
    public void createFailureTest() throws UserAlreadyExistsException {
    	AppUser user = buildUserObject();
    	when(userRepository.existsByEmail(this.email)).thenReturn(true);
    	userService.createUser(user);
    }

    @Test
    public void changePasswordTest() {
        AppUser user = buildUserObject();
        String newPassword = "newPassword";
        userService.changePassword(user, newPassword);
        verify(passwordEncoder, times(1)).encode(newPassword);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testAuthenticateSuccess() {
        AppUser user = buildUserObject();
        when(passwordEncoder.matches(password, password)).thenReturn(true);
        Boolean result = userService.authenticate(user, password);
        verify(passwordEncoder, times(1)).matches(password, password);
        Assert.assertEquals(true, result);
    }

    @Test
    public void testAuthenticateFail() {
        AppUser user = buildUserObject();
        String wrongPassword = "wrongPassword";
        when(passwordEncoder.matches(password, wrongPassword)).thenReturn(false);
        Boolean result = userService.authenticate(user, wrongPassword);
        verify(passwordEncoder, times(1)).matches(wrongPassword, password);
        Assert.assertEquals(false, result);
    }

    private AppUser buildUserObject() {
    	AppUser user = new AppUser(this.name, this.email, this.username, this.password);
    	user.setId(this.userId);
    	return user;
    }
}
