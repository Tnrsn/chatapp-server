package com.chatapp.server.dto;

import java.util.UUID;

public class MessageRequest {
    public UUID conversationId;
    public String token;
    public String content;
    public String type;
}
