package com.chatapp.server.messages;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chatapp.server.auth.SessionManager;

@RestController
@RequestMapping("/messages")
public class MessageManager {

    private final MessageService messageService;

    public MessageManager(MessageService messageService) 
    {
        this.messageService = messageService;
    }
    
    @PostMapping("/send")
    public Message sendMessage(@RequestParam UUID conversationId, @RequestParam String token, @RequestParam String content) 
    {
    	UUID senderId = SessionManager.getUserId(token);
    	if(senderId == null) return null;
    	
        return messageService.sendMessage(conversationId, senderId, content);
    }
    
    @GetMapping("/get")
    public List<Message> getMessages(@RequestParam UUID conversationId, @RequestParam String token) 
    {
    	UUID userId = SessionManager.getUserId(token);
    	if(userId == null) return null;
    	
        return messageService.getMessages(conversationId, userId);
    }
}