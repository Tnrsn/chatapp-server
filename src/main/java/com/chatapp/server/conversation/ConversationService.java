package com.chatapp.server.conversation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chatapp.server.community.Community;
import com.chatapp.server.community.CommunityRepository;

@Service
public class ConversationService {

	private final ConversationRepository conversationRepository;
	private final ConversationMemberRepository memberRepository;
	private final CommunityRepository communityRepository;
	
	public ConversationService(ConversationRepository conversationRepository, ConversationMemberRepository memberRepository,
			CommunityRepository communityRepository)
	{
		this.conversationRepository = conversationRepository;
		this.memberRepository = memberRepository;
		this.communityRepository = communityRepository;
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
        convo.setType("direct"); //I'll make convo types ENUM later
        convo.setCreatedBy(user1);
        convo.setCreatedAt(LocalDateTime.now());

        Conversation saved = conversationRepository.save(convo);
        
        addConversationMember(user1, saved.getId());
        addConversationMember(user2, saved.getId());

        return saved;
    }
	
	@Transactional
	public Conversation createCommunityConversation(UUID ownerId)
	{
		Conversation convo = new Conversation();
		
		convo.setType("community");
		convo.setCreatedBy(ownerId);
		convo.setCreatedAt(LocalDateTime.now());
		
		Conversation saved = conversationRepository.save(convo);
		return saved;
	}
	
	@Transactional
	public Conversation openCommunityConversation(UUID communityId)
	{
	    Community community = communityRepository.findById(communityId).orElse(null);

	    if (community == null) return null;

	    return conversationRepository.findById(community.getConversationId()).orElse(null);
	}
	
	@Transactional
	public void addConversationMember(UUID member, UUID convoId)
	{
		if(memberRepository.existsByConversationIdAndUserId(convoId, member)) return;
		
        ConversationMember m = new ConversationMember();
        m.setConversationId(convoId);
        m.setUserId(member);
        m.setJoinedAt(LocalDateTime.now());

        memberRepository.save(m);
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
