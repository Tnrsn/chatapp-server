package com.chatapp.server.messages;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.chatapp.server.conservation.ConversationMemberRepository;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final ConversationMemberRepository memberRepository;

    public MessageService(MessageRepository messageRepository, ConversationMemberRepository memberRepository) 
    {
        this.messageRepository = messageRepository;
        this.memberRepository = memberRepository;
    }
    
    public Message sendMessage(UUID conversationId, UUID senderId, String content) 
    {
        boolean isMember = memberRepository.existsByConversationIdAndUserId(conversationId, senderId);

        if (!isMember) 
        {
            throw new RuntimeException("User is not in this conversation");
        }

        Message msg = new Message();
        msg.setConversationId(conversationId);
        msg.setSenderId(senderId);
        msg.setContent(content);
        msg.setMessageType("text");
        msg.setCreatedAt(LocalDateTime.now());

        return messageRepository.save(msg);
    }
    
    public List<Message> getMessages(UUID conversationId, UUID userId) 
    {
        boolean isMember = memberRepository.existsByConversationIdAndUserId(conversationId, userId);

        if (!isMember) 
        {
            throw new RuntimeException("Access denied");
        }

        return messageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId);
    }
}
