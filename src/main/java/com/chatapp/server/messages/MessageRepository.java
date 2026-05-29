package com.chatapp.server.messages;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.chatapp.server.user.User;

public interface MessageRepository extends JpaRepository<Message, UUID> {

	List<Message> findByConversationIdOrderByCreatedAtAsc(UUID conversationId);
	
	@Query("""
		    SELECT u.username
		    FROM User u
		    WHERE u.id = :userId
			""")
	String findUsernameById(@Param("userId") UUID userId);
}