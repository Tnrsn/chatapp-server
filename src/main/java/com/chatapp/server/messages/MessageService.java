package com.chatapp.server.messages;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.chatapp.server.conversation.ConversationMemberRepository;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final ConversationMemberRepository memberRepository;

    public MessageService(MessageRepository messageRepository, ConversationMemberRepository memberRepository) 
    {
        this.messageRepository = messageRepository;
        this.memberRepository = memberRepository;
    }
    
    public Message sendMessage(UUID conversationId, UUID senderId, String content, String type) 
    {
        boolean isMember = memberRepository.existsByConversationIdAndUserId(conversationId, senderId);
        if (!isMember) return null;
        
        Message msg = new Message();
        msg.setConversationId(conversationId);
        msg.setSenderId(senderId);
        msg.setContent(content);
        msg.setMessageType(type);
        msg.setCreatedAt(LocalDateTime.now());

        return messageRepository.save(msg);
    }
    
    public List<Message> getMessages(UUID conversationId, UUID userId) 
    {
        boolean isMember = memberRepository.existsByConversationIdAndUserId(conversationId, userId);
        if (!isMember) return null;

        return messageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId);
    }
}
