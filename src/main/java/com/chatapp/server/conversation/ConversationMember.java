package com.chatapp.server.conversation;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
    
    public ConversationMember() {}
    public ConversationMember(UUID conversationId, UUID userId, LocalDateTime joinedAt)
    {
    	this.conversationId = conversationId;
    	this.userId = userId;
    	this.joinedAt = joinedAt;
    }
    
    public UUID getId()
    {
    	return id;
    }
    
    public void setId(UUID uuid)
    {
    	id = uuid;
    }
    
    public void setConversationId(UUID id)
    {
    	conversationId = id;
    }
    
    public UUID getConversationId()
    {
    	return conversationId;
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
