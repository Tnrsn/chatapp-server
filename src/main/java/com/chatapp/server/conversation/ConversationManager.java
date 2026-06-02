package com.chatapp.server.conversation;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chatapp.server.auth.SessionManager;

@RestController
@RequestMapping("/conversations")
public class ConversationManager {

    private final ConversationService conversationService;

    public ConversationManager(ConversationService conversationService) 
    {
        this.conversationService = conversationService;
    }
    
    @PostMapping("/dm")
    public Conversation createDM(@RequestParam String token, @RequestParam UUID user2)
    {
    	UUID user1 = SessionManager.getUserId(token);
    	if(user1 == null || user1.equals(user2)) return null;
    	
        return conversationService.createDirectConversation(user1, user2);
    }
    
    @PostMapping("/community")
    public Conversation openCommunityConversation(@RequestParam String token, @RequestParam UUID communityId)
    {
    	UUID user1 = SessionManager.getUserId(token);
    	if(user1 == null || user1.equals(communityId)) return null;
    	
        return conversationService.openCommunityConversation(communityId);
    }
    
    @GetMapping("/list") //I can use here if I ever decide to make a messages list
    public List<Conversation> getUserConversations(@RequestParam String token) 
    {
    	UUID userId = SessionManager.getUserId(token);
    	if(userId == null) return null;
    	
        return conversationService.getUserConversations(userId);
    }
}