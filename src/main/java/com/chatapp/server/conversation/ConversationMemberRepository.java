package com.chatapp.server.conversation;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ConversationMemberRepository extends JpaRepository<ConversationMember, UUID> {

    List<ConversationMember> findByUserId(UUID userId);
    List<ConversationMember> findByConversationId(UUID conversationId);

    boolean existsByConversationIdAndUserId(UUID conversationId, UUID userId);
    
    @Modifying
    @Query("""
    DELETE FROM ConversationMember cm
    WHERE cm.conversationId = :conversationId
    AND cm.userId = :userId
    """)
    int leaveConversation(UUID conversationId, UUID userId);
    
    @Modifying
    @Query("""
    DELETE FROM ConversationMember cm
    WHERE cm.conversationId = :conversationId
    """)
    void deleteByConversationId(UUID conversationId);
}
