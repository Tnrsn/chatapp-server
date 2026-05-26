package com.chatapp.server.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
	User findByUsername(String username);
	User findByEmail(String email);
	User findById(String userId);
	
	boolean existsByEmail(String email);
	boolean existsByUsername(String username);
	
	List<User> findByUsernameContainingIgnoreCase(String username);
	
	@Query("""
			SELECT u FROM User u
			WHERE u.id IN (
			    SELECT CASE
			        WHEN f.requesterId = :userId THEN f.receiverId
			        ELSE f.requesterId
			    END
			    FROM Friendship f
			    WHERE (f.requesterId = :userId OR f.receiverId = :userId)
			    AND f.status = 'accepted'
			)
			""")
			List<User> findFriends(@Param("userId") UUID userId);
}