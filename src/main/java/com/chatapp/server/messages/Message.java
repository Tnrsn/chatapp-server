package com.chatapp.server.messages;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue
    private UUID id;

    private UUID conversationId;
    private UUID senderId;
    private String username;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String messageType; // file, image etc

//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    public UUID getId()
    {
    	return id;
    }
    
    public void setConversationId(UUID uuid)
    {
    	conversationId = uuid;
    }
    
    public UUID getConversationId()
    {
    	return conversationId;
    }
    
    public UUID getSenderId()
    {
    	return senderId;
    }
    
    public void setSenderId(UUID uuid)
    {
    	senderId = uuid;
    }
    
    public void setUsername(String username)
    {
    	this.username = username;
    }
    public String getUsername()
    {
    	return username;
    }
    
    public String getContent()
    {
    	return content;
    }
    
    public void setContent(String content)
    {
    	this.content = content;
    }
    
    public String getMessageType()
    {
    	return messageType;
    }
    
    public void setMessageType(String type)
    {
    	messageType = type;
    }
    
    public LocalDateTime getCreatedAt()
    {
    	return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime date)
    {
    	createdAt = date;
    }
}
