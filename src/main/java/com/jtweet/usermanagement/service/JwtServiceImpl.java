package com.jtweet.usermanagement.service;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.jtweet.usermanagement.helper.TokenGenerator;
import com.jtweet.usermanagement.helper.TokenValidator;
import com.jtweet.usermanagement.model.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class JwtServiceImpl implements JwtService {

	@Autowired
	private TokenGenerator tokenGenerator;

	@Autowired
	private TokenValidator tokenValidator;

	@Override
	public DecodedJWT validateToken(String token) throws JWTVerificationException, UnsupportedEncodingException {
		return tokenValidator.validate(token);
	}

	@Override
	public String generateToken(AppUser user) {
		return tokenGenerator.generate(user);
	}

}
