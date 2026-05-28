package com.chatapp.server.search;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chatapp.server.auth.SessionManager;
import com.chatapp.server.friendships.FriendshipService;
import com.chatapp.server.user.User;
import com.chatapp.server.user.UserService;

@RestController
@RequestMapping("/search")
public class SearchManager {

	private final UserService userService;
	private final FriendshipService friendshipService;
	
    public SearchManager(UserService userService, FriendshipService friendshipService) {
        this.userService = userService;
        this.friendshipService = friendshipService;
    }
    
	@GetMapping("/people")
	public List<User> searchPeople(@RequestParam String search)
	{
		List<User> users = userService.searchUsers(search);
		return users;
	}
	
	@GetMapping("/requests")
	public List<User> searchRequests(@RequestParam String token)
	{
	    UUID userId = SessionManager.getUserId(token);
	    return friendshipService.getPendingRequests(userId);
	}
}
