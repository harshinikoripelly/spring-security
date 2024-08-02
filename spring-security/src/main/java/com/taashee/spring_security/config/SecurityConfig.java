package com.taashee.spring_security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.taashee.spring_security.repository.UserRepository;
import com.taashee.spring_security.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	UserRepository userRepository;
	
	@Autowired
	public SecurityConfig(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    http.csrf(csrf -> csrf.disable())
//	        .exceptionHandling(exceptionHandling -> 
//	            exceptionHandling.authenticationEntryPoint(authEntryPoint))
//	        .sessionManagement(sessionManagement -> 
//	            sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	        .authorizeHttpRequests(authorize -> 
	            authorize.requestMatchers("/api/auth/**").permitAll());

//	    http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
	    return http.build();
	}
	
    @Bean
	UserDetailsService userDetailsService() {
		return new CustomUserDetailsService(userRepository);
	}
	
	@Bean
	AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService());
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
//	@Bean
//	public JwtAuthenticationFilter jwtAuthenticationFilter() {
//		return new JwtAuthenticationFilter();
//	}
}
