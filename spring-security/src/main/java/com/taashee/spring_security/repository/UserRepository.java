package com.taashee.spring_security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.taashee.spring_security.entity.AuthenticationSuccessResponse;
import com.taashee.spring_security.entity.UserEntity;

import jakarta.transaction.Transactional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
	Optional<UserEntity> findByUsername(String username);
	
	Boolean existsByUsername(String username);
	
	@Transactional
	@Query("SELECT id as id, username as username, role as role FROM UserEntity WHERE username=:username")
	AuthenticationSuccessResponse findAuthenticationByUsername(@Param("username") String username);
}
