package com.taashee.spring_security.service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.taashee.spring_security.config.properties.JwtProperties;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;


@Component
public class JWTService {

	private static final String R0LE = "role";
	private static final Long MILLISECONDS_PER_MINUTE = 1000L * 60;
	private final JwtProperties jwtProperties;

	public JWTService(JwtProperties jwtProperties) {
		this.jwtProperties = jwtProperties;
	}

	public void validateToken(final String token) {
		Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
	}

	public String generateToken(String userName, String role) {
		Map<String, Object> claims = new HashMap<>();
		claims.put(R0LE, role);
		return createToken(claims, userName);
	}

	private String createToken(Map<String, Object> claims, String userName) {
		return Jwts.builder().setClaims(claims).setSubject(userName).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + MILLISECONDS_PER_MINUTE * Integer.parseInt(jwtProperties.getDurationInMinutes())))
				.signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
	}

	private Key getSignKey() {
		byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecret());
		return Keys.hmacShaKeyFor(keyBytes);
	}
	

	
}