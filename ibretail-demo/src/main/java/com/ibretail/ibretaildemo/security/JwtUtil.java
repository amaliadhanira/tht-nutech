package com.ibretail.ibretaildemo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;

public class JwtUtil {
	 private static final String SECRET_KEY = SecurityConstants.SECRET_KEY;

	    public static String extractEmail(String token) {
	        return Jwts.parserBuilder()
	                .setSigningKey(SECRET_KEY.getBytes())
	                .build()
	                .parseClaimsJws(token)
	                .getBody()
	                .getSubject(); 
	    }
}
