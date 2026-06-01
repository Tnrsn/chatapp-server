package com.chatapp.server.friendships;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import com.chatapp.server.auth.SessionManager;
import com.chatapp.server.dto.MessageRequest;
import com.chatapp.server.messages.Message;
import com.chatapp.server.user.User;
import com.chatapp.server.user.UserRepository;
import com.chatapp.server.websocket.WebSocketPing;

import jakarta.websocket.Session;

@Service
public class FriendshipService {
    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public FriendshipService(FriendshipRepository friendshipRepository, UserRepository userRepository) {

        this.friendshipRepository = friendshipRepository;
        this.userRepository = userRepository;
    }

    public List<User> getFriends(String userToken) {
    	UUID userId = SessionManager.getUserId(userToken);
        return friendshipRepository.findFriends(userId);
    }
    
    public void sendFriendRequest(String requesterToken, UUID receiverId)
    {
    	UUID requesterId = SessionManager.getUserId(requesterToken);
    	if(requesterId == receiverId )	return;

    	boolean exists = friendshipRepository.existsByRequesterIdAndReceiverId(requesterId, receiverId);

        if(!exists)
        {
        	System.out.println("Friend req sent");
            Friendship friendship = new Friendship();

            friendship.setRequesterId(requesterId);
            friendship.setReceiverId(receiverId);
            friendship.setStatus("pending");

            friendshipRepository.save(friendship);
        }
        else 
        {
        	System.out.println("Friendship req not sent");
        }
    }
    
    public List<User> getPendingRequests(UUID userId)
    {
        return friendshipRepository.findPendingRequests(userId);
    }
    
    public void acceptRequest(String userToken, String requesterId)
    {
    	UUID receiverId = SessionManager.getUserId(userToken);
    	if(receiverId == null) return;
    	
    	friendshipRepository.acceptFriendRequest(UUID.fromString(requesterId), receiverId);
    	
    	WebSocketPing ping = new WebSocketPing();
    	ping.setStatus(true);
    	
    	System.out.println("ReceiverId = " + requesterId);
    	System.out.println("requesterId = " + requesterId);
    	messagingTemplate.convertAndSend("/topic/friends/refresh/" + receiverId.toString(), ping);
        messagingTemplate.convertAndSend("/topic/friends/refresh/" + requesterId, ping);
    }

    public void declineRequest(String userToken, UUID requesterId)
    {
    	UUID receiverId = SessionManager.getUserId(userToken);
    	if(receiverId == null) return;
    	
    	friendshipRepository.deleteFriendship(requesterId, receiverId);
    	
    	WebSocketPing ping = new WebSocketPing();
    	ping.setStatus(true);
    	
    	System.out.println("ReceiverId = " + requesterId);
    	System.out.println("requesterId = " + requesterId);
    	messagingTemplate.convertAndSend("/topic/friends/refresh/" + receiverId.toString(), ping);
        messagingTemplate.convertAndSend("/topic/friends/refresh/" + requesterId, ping);
    }
}
