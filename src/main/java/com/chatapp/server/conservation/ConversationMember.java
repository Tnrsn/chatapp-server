package com.chatapp.server.conservation;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.Id;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;

@Entity
@Table(name = "conversation_members")
public class ConversationMember {

    @Id
    @GeneratedValue
    private UUID id;
    private UUID conversationId;
    private UUID userId;
    private LocalDateTime joinedAt;
    
    public UUID getConversationId()
    {
    	return id;
    }
    
    public void setConversationId(UUID uuid)
    {
    	id = uuid;
    }
    
    public void setUserId(UUID uuid)
    {
    	userId = uuid;
    }
    
    public void setJoinedAt(LocalDateTime date)
    {
    	joinedAt = date;
    }
}
