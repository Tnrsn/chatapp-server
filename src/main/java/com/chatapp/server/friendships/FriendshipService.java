package com.chatapp.server.friendships;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.chatapp.server.user.User;
import com.chatapp.server.user.UserRepository;

@Service
public class FriendshipService {
    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;

    public FriendshipService(
            FriendshipRepository friendshipRepository,
            UserRepository userRepository) {

        this.friendshipRepository = friendshipRepository;
        this.userRepository = userRepository;
    }

    public List<User> getFriends(UUID userId) {
        return userRepository.findFriends(userId);
    }
}
