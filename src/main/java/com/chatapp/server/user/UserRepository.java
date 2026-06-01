package com.chatapp.server.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
	User findByUsername(String username);
	User findByEmail(String email);
//	User findById(UUID userId);
	
	boolean existsByEmail(String email);
	boolean existsByUsername(String username);
	
	List<User> findByUsernameContainingIgnoreCase(String username);
	

}