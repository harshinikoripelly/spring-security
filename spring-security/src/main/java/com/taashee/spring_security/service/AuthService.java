package com.taashee.spring_security.service;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.taashee.spring_security.config.properties.JwtProperties;
import com.taashee.spring_security.dto.RequestDto;
import com.taashee.spring_security.entity.AuthenticationSuccessResponse;
import com.taashee.spring_security.entity.CustomUserDetails;
import com.taashee.spring_security.entity.UserEntity;
import com.taashee.spring_security.repository.UserRepository;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class AuthService {

	private static final String JWT_COOKIE_NAME = "JwtToken";
	private static final int SECONDS_PER_MINUTE = 60;

	private final UserRepository userRepository;
	private final JWTService jwtService;
	private final AuthenticationManager authenticationManager;
	private final JwtProperties jwtProperties;
	private final PasswordEncoder passwordEncoder;

	public AuthService(UserRepository userRepository, JWTService jwtService, AuthenticationManager authenticationManager, JwtProperties jwtProperties
			,PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.jwtService = jwtService;
		this.authenticationManager = authenticationManager;
        this.jwtProperties = jwtProperties;
        this.passwordEncoder = passwordEncoder;
    }

	public Authentication authenticateUser(String email, String password) {
		return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
	}
	
	public ResponseEntity<String> registerUser(RequestDto requestDto) {
		if(userRepository.existsByUsername(requestDto.getUsername())) {
			return new ResponseEntity<String>("Username is taken!", HttpStatus.BAD_REQUEST);
		}
		
		UserEntity user = new UserEntity();
		user.setUsername(requestDto.getUsername());
		user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
		user.setRole(requestDto.getRole());
		
		userRepository.save(user);
		
		return new ResponseEntity<String>("User created successfully!",HttpStatus.OK);
	}

	public String generateToken(String username, String role) {
		return jwtService.generateToken(username, role);
	}

	public void addJwtCookie(String email, Authentication authentication, HttpServletResponse response) {
		CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
		String token = generateToken(email, customUserDetails.getRole());
		Cookie cookie = new Cookie(JWT_COOKIE_NAME, token);
		cookie.setMaxAge(SECONDS_PER_MINUTE * Integer.parseInt(jwtProperties.getDurationInMinutes()));
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		response.addCookie(cookie);
	}

	public void removeJwtCookie(HttpServletResponse response) {
		Cookie cookie = new Cookie(JWT_COOKIE_NAME, "");
		cookie.setMaxAge(0);
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		
		
		response.addCookie(cookie);
	}
	
	public AuthenticationSuccessResponse getAuthenticatedResponse(String username) {
		return userRepository.findAuthenticationByUsername(username);
	}
	

}
