package com.taashee.spring_security.security;

import java.security.Key;
import java.util.Date;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtGenerator {

	private Key getSignKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SecurityConstants.JWT_SECRET);
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
	public String generateToken(Authentication authentication) {
		String username = authentication.getName();
		Date currentDate = new Date();
		Date expiryDate = new Date(currentDate.getTime() + SecurityConstants.JWT_EXPIRATION);
		
		return Jwts.builder()
				.setSubject(username)
				.setIssuedAt(new Date())
				.setExpiration(expiryDate)
				 .signWith(getSignKey(), SignatureAlgorithm.HS256)
				.compact();
	}
	
	public String getUserNameFromToken(String token) {

	    Claims claims = Jwts.parserBuilder()
	                        .setSigningKey(getSignKey())
	                        .build()
	                        .parseClaimsJws(token)
	                        .getBody();

	    return claims.getSubject();
	}
	

	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			throw new AuthenticationCredentialsNotFoundException("Jwt was expired or incorrect");
		}
	}

}
