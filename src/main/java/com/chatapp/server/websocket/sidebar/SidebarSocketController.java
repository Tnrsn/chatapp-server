package com.chatapp.server.websocket.sidebar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import com.chatapp.server.dto.FriendshipRequest;
import com.chatapp.server.friendships.FriendshipService;

@Controller
public class SidebarSocketController {
	
    @Autowired
    private FriendshipService friendshipService;
	
	@MessageMapping("/friend.accept")
	public void acceptFriend(FriendshipRequest request)
	{
		System.out.println("Friendship accepteddddd");
//		friendshipService.acceptRequest(request.getUserToken(), request.getRequesterId());
	}
}
