package com.chatapp.server.messages;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chatapp.server.auth.SessionManager;
import com.chatapp.server.dto.MessageRequest;

@RestController
@RequestMapping("/messages")
public class MessageManager {

    private final MessageService messageService;

    public MessageManager(MessageService messageService) 
    {
        this.messageService = messageService;
    }
    
    @PostMapping("/send")
    public Message sendMessage(@RequestBody MessageRequest request) 
    {
    	UUID senderId = SessionManager.getUserId(request.token);
    	if(senderId == null) return null;
    	
        return messageService.sendMessage(request.conversationId, senderId, request.content, request.type);
    }
    
    @GetMapping("/get")
    public List<Message> getMessages(@RequestParam UUID conversationId, @RequestParam String token) 
    {
    	UUID userId = SessionManager.getUserId(token);
    	if(userId == null) return null;
    	
        return messageService.getMessages(conversationId, userId);
    }
}