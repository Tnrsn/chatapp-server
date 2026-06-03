package com.chatapp.server.community;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
import com.chatapp.server.community.tag.TagService;
import com.chatapp.server.conversation.CommunityCreatedEvent;
import com.chatapp.server.conversation.Conversation;
import com.chatapp.server.conversation.ConversationMember;
import com.chatapp.server.conversation.ConversationMemberRepository;
import com.chatapp.server.conversation.ConversationService;
import com.chatapp.server.dto.CommunityRequest;
import com.chatapp.server.user.User;
import com.chatapp.server.websocket.WebSocketPing;

@Service
public class CommunityService {

    private final CommunityRepository communityRepository;
    private final ConversationService conversationService;
    private final CommunityTagRepository communityTagRepository;
    private final TagRepository tagRepository;
    private final ConversationMemberRepository memberRepository;
    private final TagService tagService;
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public CommunityService(CommunityRepository communityRepository, ConversationService conversationService, 
    		CommunityTagRepository communityTagRepository, TagRepository tagRepository, ConversationMemberRepository memberRepository,
    		TagService tagService)
    {
        this.communityRepository = communityRepository;
        this.conversationService = conversationService;
        this.communityTagRepository = communityTagRepository;
        this.tagRepository = tagRepository;
        this.memberRepository = memberRepository;
        this.tagService = tagService;
    }
	
	public Community CreateCommunity(String token, CommunityRequest request)
	{
//		System.out.println("OwnerId; " + SessionManager.getUserId(token));
//		System.out.println("Server name; " + request.getName()); //Delete this line later after debug
		UUID ownerId = SessionManager.getUserId(token);
		
		if(ownerId == null) return null;
	    if(request == null) return null; //Just in case
	    if(request.getName() == null || request.getName().isBlank()) return null;
	    if(request.getTags().size() > 3) return null;
	    if(communityRepository.existsByNameIgnoreCase(request.getName().trim())) return null; //Checks if a community with that name already exists
	    
	    
	    Conversation conversation = conversationService.createCommunityConversation(ownerId);
	    Community community = new Community();
	    
	    community.setName(request.getName().trim());
	    community.setDescription(request.getDescription().trim());
	    community.setPublic(request.isPublic());
	    community.setOwnerUserId(ownerId);
	    community.setConversationId(conversation.getId());
	    community.setCreatedAt(LocalDateTime.now());
	    community.setUpdatedAt(LocalDateTime.now());
	    community = communityRepository.save(community);
	    
	    
	    for (String rawTag : request.getTags()) {
//	    	System.out.print("Tag: " + rawTag);
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
	    
//	    CommunityCreatedEvent event = new CommunityCreatedEvent(community.getId(), community.getConversationId(), community.getName(),
//	    		community.getDescription(), community.isPublic(), tags);
	    
	    WebSocketPing ping = new WebSocketPing();
	    ping.setStatus(true);
	    
//	    System.out.println("/topic/community/created/" + ownerId);
	    messagingTemplate.convertAndSend("/topic/community/created/" + ownerId, ping);
	    
	    return community;
	}

	public List<Community> getCommunityListOfUser(String token) 
	{
		UUID userId = SessionManager.getUserId(token);
	    if(userId == null) return null;
		
		return communityRepository.findCommunitiesByUserId(userId);
	}

	public List<CommunitySearchResults> searchCommunities(String search) 
	{
	    if(search == null || search.isBlank()) return List.of();
	    List<Community> communities = communityRepository.searchByNameOrTag(search.trim());
	    List<CommunitySearchResults> searchResult = new ArrayList<>();
	    for(Community community : communities)
	    {
	        List<String> tags = tagService.getTagNamesByCommunityId(community.getId());

	        searchResult.add(new CommunitySearchResults(community, tags));
	    }
	    
	    return searchResult; 
	}
	
}
