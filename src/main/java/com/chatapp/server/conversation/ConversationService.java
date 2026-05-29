package com.chatapp.server.conversation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConversationService {

	private final ConversationRepository conversationRepository;
	private final ConversationMemberRepository memberRepository;
	
	public ConversationService(ConversationRepository conversationRepository, ConversationMemberRepository memberRepository)
	{
		this.conversationRepository = conversationRepository;
		this.memberRepository = memberRepository;
	}
	
	@Transactional
    public Conversation createDirectConversation(UUID user1, UUID user2) 
    {
        List<ConversationMember> user1Chats = memberRepository.findByUserId(user1);

        for (ConversationMember cm : user1Chats) //Returns the existing conversation id if exists
        {
            boolean alreadyExists = memberRepository.existsByConversationIdAndUserId(cm.getConversationId(), user2);
            if (alreadyExists) return conversationRepository.findById(cm.getConversationId()).orElse(null);
        }
        //otherwise it creates a new direct conversation.
        Conversation convo = new Conversation();
        convo.setType("direct");
        convo.setCreatedBy(user1);
        convo.setCreatedAt(LocalDateTime.now());

        Conversation saved = conversationRepository.save(convo);
        ConversationMember m1 = new ConversationMember();
        
        m1.setConversationId(saved.getId());
        m1.setUserId(user1);
        m1.setJoinedAt(LocalDateTime.now());

        ConversationMember m2 = new ConversationMember();
        m2.setConversationId(saved.getId());
        m2.setUserId(user2);
        m2.setJoinedAt(LocalDateTime.now());

        memberRepository.save(m1);
        memberRepository.save(m2);

        return saved;
    }
    
    public List<Conversation> getUserConversations(UUID userId) 
    {
        List<ConversationMember> memberships = memberRepository.findByUserId(userId);
        List<UUID> conversationIds = new ArrayList<>();

        for (ConversationMember member : memberships) {
            conversationIds.add(member.getConversationId());
        }

        return conversationRepository.findAllById(conversationIds);
    }
}
