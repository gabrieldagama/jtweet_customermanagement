package com.jtweet.usermanagement.service;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.jtweet.usermanagement.model.AppUser;

import java.io.UnsupportedEncodingException;

public interface JwtService {
    public String generateToken(AppUser user);
    public DecodedJWT validateToken(String token) throws JWTVerificationException, UnsupportedEncodingException;
}
