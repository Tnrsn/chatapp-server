package com.chatapp.server.community;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chatapp.server.dto.CommunityRequest;

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
}
