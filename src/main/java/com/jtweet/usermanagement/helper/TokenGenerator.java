package com.jtweet.usermanagement.helper;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.jtweet.usermanagement.model.AppUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TokenGenerator {

    @Value("${jtweet.jwt.secret}")
    private String jwtSecret;

    @Value("${jtweet.jwt.expiration}")
    private String jwtExpiration;

    public String generate(AppUser user) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withClaim("id", user.getId())
                .withClaim("email", user.getEmail())
                .withClaim("name", user.getName())
                .withClaim("role", user.getRole())
                .withExpiresAt(new Date(System.currentTimeMillis() + Long.parseLong(jwtExpiration)))
                .sign(Algorithm.HMAC256(jwtSecret.getBytes()));
    }
}
