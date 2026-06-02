package com.chatapp.server.community;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.chatapp.server.auth.SessionManager;
import com.chatapp.server.community.tag.CommunityTag;
import com.chatapp.server.community.tag.CommunityTagRepository;
import com.chatapp.server.community.tag.Tag;
import com.chatapp.server.community.tag.TagRepository;
import com.chatapp.server.conversation.CommunityCreatedEvent;
import com.chatapp.server.conversation.Conversation;
import com.chatapp.server.conversation.ConversationService;
import com.chatapp.server.dto.CommunityRequest;

@Service
public class CommunityService {

    private final CommunityRepository repo;
    private final ConversationService conversationService;
    private final CommunityTagRepository communityTagRepository;
    private final TagRepository tagRepository;
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public CommunityService(CommunityRepository repo, ConversationService conversationService, 
    		CommunityTagRepository communityTagRepository, TagRepository tagRepository)
    {
        this.repo = repo;
        this.conversationService = conversationService;
        this.communityTagRepository = communityTagRepository;
        this.tagRepository = tagRepository;
    }
	
	public Community CreateCommunity(String token, CommunityRequest request)
	{
		System.out.println("OwnerId; " + SessionManager.getUserId(token));
		System.out.println("Server name; " + request.getName()); //Delete this line later after debug
		UUID ownerId = SessionManager.getUserId(token);
		
		if(ownerId == null) return null;
	    if(request == null) return null; //Just in case
	    if(request.getName() == null || request.getName().isBlank()) return null;
	    if(request.getTags().size() > 3) return null;
	    if(repo.existsByNameIgnoreCase(request.getName().trim())) return null; //Checks if a community with that name already exists
	    
	    
	    Conversation conversation = conversationService.createCommunityConversation(ownerId);
	    Community community = new Community();
	    
	    community.setName(request.getName().trim());
	    community.setDescription(request.getDescription().trim());
	    community.setPublic(request.isPublic());
	    community.setOwnerUserId(ownerId);
	    community.setConversationId(conversation.getId());
	    community.setCreatedAt(LocalDateTime.now());
	    community.setUpdatedAt(LocalDateTime.now());
	    community = repo.save(community);
	    
	    
	    for (String rawTag : request.getTags()) {
	    	System.out.print("Tag: " + rawTag);
	        String tag = rawTag.trim().toLowerCase();
	        if (tag.isBlank()) continue;

	        Tag tagEntity = tagRepository.findByNameIgnoreCase(tag).orElseGet(() -> 
	        	{
	                Tag t = new Tag();
	                t.setName(tag);
	                return tagRepository.save(t);
	            });

	        CommunityTag ct = new CommunityTag();
	        ct.setCommunityId(community.getId());
	        ct.setTagId(tagEntity.getId());

	        communityTagRepository.save(ct);
	    }
	    
	    List<String> tags = request.getTags().stream().map(String::toLowerCase).map(String::trim).toList();
	    
	    conversationService.addConversationMember(ownerId, conversation.getId());
	    
	    CommunityCreatedEvent event = new CommunityCreatedEvent(community.getId(), community.getConversationId(), community.getName(),
	    		community.getDescription(), community.isPublic(), tags);
	    
	    messagingTemplate.convertAndSendToUser(ownerId.toString(), "/queue/updates", event);
	    
	    return community;
	}
	
}
