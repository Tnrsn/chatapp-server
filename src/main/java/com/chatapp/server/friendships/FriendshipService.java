package com.chatapp.server.friendships;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.chatapp.server.auth.SessionManager;
import com.chatapp.server.user.User;
import com.chatapp.server.user.UserRepository;

import jakarta.websocket.Session;

@Service
public class FriendshipService {
    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;

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
    
    public void acceptRequest(String userToken, UUID requesterId)
    {
    	UUID receiverId = SessionManager.getUserId(userToken);
    	friendshipRepository.acceptFriendRequest(requesterId, receiverId);
    }
    
    public void declineRequest(String userToken, UUID requesterId)
    {
    	UUID receiverId = SessionManager.getUserId(userToken);
    	friendshipRepository.declineFriendRequest(requesterId, receiverId);
    }
}
