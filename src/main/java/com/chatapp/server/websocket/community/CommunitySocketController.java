package com.chatapp.server.websocket.community;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.chatapp.server.auth.SessionManager;
import com.chatapp.server.community.CommunityService;
import com.chatapp.server.dto.MessageRequest;
import com.chatapp.server.messages.Message;
import com.chatapp.server.messages.MessageService;

public class CommunitySocketController {
	
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private CommunityService communityService;
	
//	@MessageMapping("/conversation.send")
//	public void handleMessage(MessageRequest msg)
//	{
//        UUID senderId = SessionManager.getUserId(msg.getToken());
//        Message saved = messageService.sendMessage(msg.getConversationId(), senderId, msg.getContent(), msg.getType());
//        System.out.println(saved.getContent());
//        System.out.println(senderId);
//        System.out.println(saved.getMessageType());
//        
//        
//        System.out.println("SEND TO = /topic/conversation/" + msg.getConversationId());
//        messagingTemplate.convertAndSend("/topic/conversation/" + msg.getConversationId(), saved);
//	}
	
    @MessageMapping("/Community.send")
    public void CreateCommunity()
    {
    	
    }
	
	
//		@GetMapping("/test")
//		public String test()
//		{
//			Message msg = new Message();
//			msg.setContent("HELOOO");
//			msg.setConversationId(null);
//			msg.setSenderId(null);
//			msg.setMessageType("TEXT");
//		    System.out.println("SENDING TEST");
//
//		    messagingTemplate.convertAndSend("/topic/conversation", msg);
//
//		    return "ok";
//		}
}
