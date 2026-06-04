package com.chatapp.server.community;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chatapp.server.dto.CommunityRequest;
import com.chatapp.server.user.User;

@RestController
@RequestMapping("/community")
public class CommunityManager {

	private final CommunityService service;
	
    public CommunityManager(CommunityService service) 
    {
        this.service = service;
    }
    
    @PostMapping("/create")
    public void createCommunity(@RequestParam String token, @RequestBody CommunityRequest request) 
    {
        service.CreateCommunity(token, request);
    }
    
	@GetMapping("/getlist")
	public List<Community> getCommunityListOfUser(@RequestParam String token)
	{
		return service.getCommunityListOfUser(token);
	}
	
	@PostMapping("/join")
	public boolean joinCommunity(@RequestParam String token, @RequestBody UUID communityId)
	{
		return service.joinCommunity(communityId, token);
	}
	
	@GetMapping("/getinfo")
	public CommunityInfo getInfo(@RequestParam String token, @RequestParam UUID communityId)
	{
	    return service.getCommunityInfo(token, communityId);
	}
}
