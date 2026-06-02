package com.chatapp.server.friends;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chatapp.server.friendships.FriendshipService;
import com.chatapp.server.user.User;

@RestController
@RequestMapping("/friends")
public class FriendshipManager {

    private final FriendshipService friendshipService;

    public FriendshipManager(FriendshipService friendshipService) 
    {
        this.friendshipService = friendshipService;
    }
	
	@PostMapping("/send")
	public void sendFriendRequest(@RequestParam String token, @RequestParam UUID receiverId)
	{
	    friendshipService.sendFriendRequest(token, receiverId);
	}
	
	@PostMapping("/accept")
	public void acceptRequest(@RequestParam String token, @RequestParam String requesterId)
	{
		friendshipService.acceptRequest(token, requesterId);
	}
	
	@PostMapping("/decline")
	public void declineRequest(@RequestParam String token, @RequestParam UUID requesterId)
	{
		friendshipService.declineRequest(token, requesterId);
	}
	
	@GetMapping("/getlist")
	public List<User> getFriends(@RequestParam String token)
	{
		return friendshipService.getFriends(token);
	}
	
//	@GetMapping("/test")
//	public String test()
//	{
//		System.out.println("f754fab2-3043-43e6-9223-051d8f203f51");
//		String id = "f754fab2-3043-43e6-9223-051d8f203f51";
//		
//		friendshipService.acceptRequest(id, id);
//	    return "ok :)";
//	}
}
