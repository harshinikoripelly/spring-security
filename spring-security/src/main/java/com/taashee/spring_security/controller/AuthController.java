package com.taashee.spring_security.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taashee.spring_security.dto.LoginDto;
import com.taashee.spring_security.dto.RequestDto;
import com.taashee.spring_security.entity.AuthenticationSuccessResponse;
import com.taashee.spring_security.service.AuthService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthService authService;	
	
	@Autowired
	public AuthController(AuthService authService) {
		super();
		this.authService = authService;
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginDto loginDto,
			HttpServletResponse httpServletResponse) {
		
		HashMap<String, String> response = new HashMap<>();
		try {
			Authentication authentication = authService.authenticateUser(loginDto.getUsername(), loginDto.getPassword());

			if (authentication.isAuthenticated()) {
				authService.addJwtCookie(loginDto.getUsername(), authentication, httpServletResponse);
				response.put("message", "Logged in successfully");
				AuthenticationSuccessResponse authenticationSuccessResponse = authService.getAuthenticatedResponse(loginDto.getUsername());
				return ResponseEntity.status(HttpStatus.OK).body(authenticationSuccessResponse);
			} else {
				response.put("message", "Authentication failed !");
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
			}

		} catch (Exception e) {
			response.put("message", e.getMessage());
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
		}
		
	}
	
	

	@GetMapping("/logout")
	public ResponseEntity<?> logout(HttpServletResponse response) {
		Map<String, String> res = new HashMap<>();
		try {
			authService.removeJwtCookie(response);
			res.put("message", "Logged out successfully");
			return ResponseEntity.status(HttpStatus.OK).body(res);
		} catch (Exception e) {
		res.put("message", e.getMessage());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
		}
	}
	
	
	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody RequestDto requestDto) {
		return authService.registerUser(requestDto);
	}
}
