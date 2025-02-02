package com.ibretail.ibretaildemo.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {
	public String createToken(String email) {
        return JWT.create()
                .withSubject(email) 
                .withIssuedAt(new Date()) 
                .withExpiresAt(new Date(System.currentTimeMillis() + 86400000)) 
                .sign(Algorithm.HMAC512(SecurityConstants.SECRET_KEY));
	}
}
