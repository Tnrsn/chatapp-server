package com.chatapp.server.websocket.chat;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.chatapp.server.auth.SessionManager;
import com.chatapp.server.dto.MessageRequest;
import com.chatapp.server.messages.Message;
import com.chatapp.server.messages.MessageService;

@Controller
public class ChatSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private MessageService messageService;
	
	@MessageMapping("/conversation.send")
	public void handleMessage(MessageRequest msg)
	{
        UUID senderId = SessionManager.getUserId(msg.getToken());
        Message saved = messageService.sendMessage(msg.getConversationId(), senderId, msg.getContent(), msg.getType());
        
        if(saved == null) return;
//        System.out.println(saved.getContent());
//        System.out.println(senderId);
//        System.out.println(saved.getMessageType());
        
        System.out.println("SEND TO = /topic/conversation/" + msg.getConversationId());
        messagingTemplate.convertAndSend("/topic/conversation/" + msg.getConversationId(), saved);
	}
	
//	@GetMapping("/test")
//	public String test()
//	{
//		System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAA");
//
//	    return "ok";
//	}
}
