package com.taashee.spring_security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.taashee.spring_security.entity.CustomUserDetails;
import com.taashee.spring_security.entity.UserEntity;
import com.taashee.spring_security.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	UserRepository userRepository;
	
	@Autowired
	public CustomUserDetailsService(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}



	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity user = userRepository.findByUsername(username)
				.orElseThrow(() ->new UsernameNotFoundException("user not found"));
		
//		return new User(user.getUsername(),user.getPassword(),mapRolesToAuthorities(user.getRoles()));
		
		return new CustomUserDetails(user);
	}
	
	
//	public Collection<GrantedAuthority> mapRolesToAuthorities(List<String> roles) {
//		return roles.stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
//	}

}
