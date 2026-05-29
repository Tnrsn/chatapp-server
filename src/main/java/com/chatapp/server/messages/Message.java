package com.chatapp.server.messages;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.Id;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue
    private UUID id;
    private UUID conversationId;
    private UUID senderId;
    private String content;
    private String messageType; // text, image, video, file etc
    private String fileUrl;
    private String fileName;
    private LocalDateTime createdAt;
    
    public void setConversationId(UUID uuid)
    {
    	id = uuid;
    }
    
    public void setSenderId(UUID uuid)
    {
    	senderId = uuid;
    }
    
    public void setContent(String content)
    {
    	this.content = content;
    }
    
    public void setMessageType(String type)
    {
    	messageType = type;
    }
    
    public void setCreatedAt(LocalDateTime date)
    {
    	createdAt = date;
    }
}
