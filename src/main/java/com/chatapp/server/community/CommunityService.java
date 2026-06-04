package com.chatapp.server.community;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
import com.chatapp.server.conversation.ConversationRepository;
import com.chatapp.server.conversation.ConversationService;
import com.chatapp.server.dto.CommunityRequest;
import com.chatapp.server.user.User;
import com.chatapp.server.user.UserRepository;
import com.chatapp.server.websocket.WebSocketPing;

import jakarta.transaction.Transactional;

@Service
public class CommunityService {

    private final CommunityRepository communityRepository;
    private final ConversationService conversationService;
    private final CommunityTagRepository communityTagRepository;
    private final TagRepository tagRepository;
    private final ConversationMemberRepository memberRepository;
    private final ConversationRepository conversationRepository;
    private final TagService tagService;
    private final UserRepository userRepository;
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public CommunityService(CommunityRepository communityRepository, ConversationService conversationService, 
    		CommunityTagRepository communityTagRepository, TagRepository tagRepository, ConversationMemberRepository memberRepository,
    		TagService tagService, UserRepository userRepository, ConversationRepository conversationRepository)
    {
        this.communityRepository = communityRepository;
        this.conversationService = conversationService;
        this.communityTagRepository = communityTagRepository;
        this.tagRepository = tagRepository;
        this.memberRepository = memberRepository;
        this.tagService = tagService;
        this.userRepository = userRepository;
        this.conversationRepository = conversationRepository;
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
	    messagingTemplate.convertAndSend("/topic/community/refresh/" + ownerId, ping);
	    
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
	
	public boolean joinCommunity(UUID communityId, String token)
	{
	    UUID userId = SessionManager.getUserId(token);
	    Community community = communityRepository.findById(communityId).orElse(null);

	    if(userId == null || community == null) return false;

	    //if already joined
	    if(memberRepository.existsByConversationIdAndUserId(community.getConversationId(),userId)) return true;
	    
	    memberRepository.save(new ConversationMember(community.getConversationId(),userId, LocalDateTime.now()));
	    
	    WebSocketPing ping = new WebSocketPing();
	    ping.setStatus(true);
	    messagingTemplate.convertAndSend("/topic/community/refresh/" + userId, ping);

	    return true;
	}

	public CommunityInfo getCommunityInfo(String token, UUID communityId) {
	    if (SessionManager.getUserId(token) == null) throw new RuntimeException("Invalid token");

	    Community community = communityRepository.findById(communityId).orElseThrow(() -> new RuntimeException("Community not found"));
	    List<String> tags = tagService.getTagNamesByCommunityId(communityId);
	    CommunitySearchResults info = new CommunitySearchResults(community, tags);
	    
	    UUID conversationId = communityRepository.findById(communityId)
	            .orElseThrow(() -> new RuntimeException("Community not found"))
	            .getConversationId();

	    List<User> members = memberRepository
	            .findByConversationId(conversationId)
	            .stream()
	            .map(ConversationMember::getUserId)
	            .map(userRepository::findById)
	            .map(opt -> opt.orElseThrow(() -> new RuntimeException("User not found")))
	            .toList();
	    
	    return new CommunityInfo(info, members);
	}

	@Transactional
	public boolean quitCommunity(String token, UUID conversationId)
	{
	    UUID userId = SessionManager.getUserId(token);

	    if(userId == null) return false;

	    Optional<Conversation> conversationOpt = conversationRepository.findById(conversationId);

	    if(conversationOpt.isEmpty()) return false;

	    Conversation conversation = conversationOpt.get();

	    if(conversation.getCreatedBy().equals(userId)) //Disband community if owner leaves
	    {
	    	System.out.print("Disband the community");
	        memberRepository.deleteByConversationId(conversationId);
	        communityRepository.deleteByConversationId(conversationId);
	        conversationRepository.delete(conversation);
	        return true;
	    }
	    
	    System.out.print("Left the community");
	    int deleted = memberRepository.leaveConversation(conversationId, userId);
	    return deleted > 0;
	}
	
}
