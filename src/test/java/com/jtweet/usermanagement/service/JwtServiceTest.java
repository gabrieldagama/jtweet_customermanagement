package com.jtweet.usermanagement.service;

import static org.mockito.Mockito.*;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.jtweet.usermanagement.helper.TokenGenerator;
import com.jtweet.usermanagement.helper.TokenValidator;
import com.jtweet.usermanagement.model.AppUser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.UnsupportedEncodingException;

public class JwtServiceTest {

    @InjectMocks
    private JwtServiceImpl jwtService;

    @Mock
    private TokenGenerator tokenGenerator;

    @Mock
    private TokenValidator tokenValidator;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGenerateToken() {
        AppUser user = Mockito.mock(AppUser.class);
        String token = "generatedToken";
        when(tokenGenerator.generate(user)).thenReturn(token);
        Assert.assertEquals(token, jwtService.generateToken(user));
        verify(tokenGenerator, times(1)).generate(user);
    }

    @Test
    public void testValidateToken() throws UnsupportedEncodingException {
        DecodedJWT decodedJwt = Mockito.mock(DecodedJWT.class);
        String token = "generatedToken";
        when(tokenValidator.validate(token)).thenReturn(decodedJwt);
        Assert.assertEquals(decodedJwt, jwtService.validateToken(token));
        verify(tokenValidator, times(1)).validate(token);
    }
}
