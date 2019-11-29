package com.jtweet.usermanagement.helper;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.jtweet.usermanagement.model.AppUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenGenerator {
    @Value("${jtweet.secret}")
    private String jwtSecret;

    public String generate(AppUser user) {
        long expirationTime = 864_000_000;
        return JWT.create()
                .withSubject(user.getUsername())
                .withClaim("id", user.getId())
                .withClaim("email", user.getEmail())
                .withClaim("name", user.getName())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .sign(Algorithm.HMAC256(jwtSecret.getBytes()));
    }
}
