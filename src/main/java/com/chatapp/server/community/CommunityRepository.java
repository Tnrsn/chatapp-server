package com.chatapp.server.community;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityRepository extends JpaRepository<Community, UUID> {
	
	boolean existsByNameIgnoreCase(String name);
	
	@Query("""
		    SELECT c
		    FROM Community c
		    WHERE c.conversationId IN (
		        SELECT cm.conversationId
		        FROM ConversationMember cm
		        WHERE cm.userId = :userId
		    )
		""")
		List<Community> findCommunitiesByUserId(UUID userId);
}
