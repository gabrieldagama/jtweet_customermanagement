package com.jtweet.usermanagement.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.jtweet.usermanagement.exception.UserAlreadyExistsException;
import com.jtweet.usermanagement.model.AppUser;
import com.jtweet.usermanagement.repository.UserRepository;

public class UserServiceTest {
	
	private final int userId = 1;
	private final String email = "gabriel@gabrielgama.com.br";
	private final String name = "Gabriel";
	private final String username = "gabrieldagama";
	private final String password = "test123";
	
    @InjectMocks
    private UserService userService;
    
    @Mock
    private UserRepository userRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void getListTest() {
    	List<AppUser> userList = new ArrayList<AppUser>();
    	AppUser user = this.buildUserObject();
    	userList.add(user);
    	when(this.userService.getList()).thenReturn(userList);
    	List<AppUser> returnedUserList = this.userService.getList();
    	assertEquals(userList, returnedUserList);
    }
    
    @Test
    public void createSuccessTest() throws UserAlreadyExistsException {
    	AppUser user = this.buildUserObject();
    	when(this.userRepository.existsByEmail(this.email)).thenReturn(false);
    	this.userService.createNewUser(user);
    }
   
    @Test(expected = UserAlreadyExistsException.class)
    public void createFailureTest() throws UserAlreadyExistsException {
    	AppUser user = this.buildUserObject();
    	when(this.userRepository.existsByEmail(this.email)).thenReturn(true);
    	this.userService.createNewUser(user);
    }

    private AppUser buildUserObject() {
    	AppUser user = new AppUser(this.name, this.email, this.username, this.password);
    	user.setId(this.userId);
    	return user;
    }
}
