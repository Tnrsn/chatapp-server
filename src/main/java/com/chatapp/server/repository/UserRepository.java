package com.chatapp.server.repository;

import com.chatapp.server.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
	User findByUsername(String username);
	User findByEmail(String email);
	User findById(String userId);
	
	boolean existsByEmail(String email);
	boolean existsByUsername(String username);
}