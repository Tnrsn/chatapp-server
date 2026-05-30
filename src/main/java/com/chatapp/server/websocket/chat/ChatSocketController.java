package com.chatapp.server.websocket.chat;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

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
	
	@MessageMapping("/conversation/")
	@SendTo("/topic/conversation/")
	public void handleMessage(MessageRequest msg)
	{
        UUID senderId = SessionManager.getUserId(msg.token);
        Message saved = messageService.sendMessage(msg.conversationId, senderId, msg.content, msg.type);

        System.out.println("SEND TO = /topic/conversation/" + msg.conversationId);
        messagingTemplate.convertAndSend("/topic/conversation/" + msg.conversationId, "TEEEEEST"); //This line is not working as intended...
	}
}
