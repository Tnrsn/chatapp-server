package com.chatapp.server.community.tag;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class TagService {

	private final CommunityTagRepository communityTagRepository;
	private final TagRepository tagRepository;
	
	public TagService(CommunityTagRepository communityTagRepository, TagRepository tagRepository)
	{
		this.communityTagRepository = communityTagRepository;
		this.tagRepository = tagRepository;
	}
	
//	public List<Tag> getTagsByCommunityId(UUID communityId)
//	{
//	    List<CommunityTag> communityTags = communityTagRepository.findByCommunityId(communityId);
//	    List<Tag> tags = new ArrayList<>();
//
//	    for (CommunityTag ct : communityTags)
//	    {
//	        Tag tag = tagRepository.findById(ct.getTagId()).orElse(null);
//
//	        if(tag != null)
//	        {
//	            tags.add(tag);
//	        }
//	    }
//
//	    return tags;
//	}
	
	public List<String> getTagNamesByCommunityId(UUID communityId)
	{
	    List<CommunityTag> communityTags = communityTagRepository.findByCommunityId(communityId);
	    List<String> tags = new ArrayList<>();

	    for (CommunityTag ct : communityTags)
	    {
	        Tag tag = tagRepository.findById(ct.getTagId()).orElse(null);
	        if(tag != null) tags.add(tag.getName());
	    }

	    return tags;
	}
}
