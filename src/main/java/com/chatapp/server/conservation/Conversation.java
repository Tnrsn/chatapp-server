package com.chatapp.server.conservation;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.Id;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;

@Entity
@Table(name = "conversations")
public class Conversation {

    @Id
    @GeneratedValue
    private UUID id;
    private String name; // null for DMs
    private String type; // type of the conversation (direct or community).
    private UUID createdBy;
    private LocalDateTime createdAt;
    
    public UUID getId()
    {
    	return id;
    }
    
    public void setType(String type)
    {
    	this.type = type;
    }
    
    public void setCreatedBy(UUID userId)
    {
    	createdBy = userId;
    }
    
    public void setCreatedAt(LocalDateTime date)
    {
    	createdAt = date;
    }
}