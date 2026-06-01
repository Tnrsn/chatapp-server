package com.chatapp.server.user;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> searchUsers(String username) {
        return userRepository.findByUsernameContainingIgnoreCase(username);
    }
    
    public Optional<User> getUserById(UUID id)
    {
    	return userRepository.findById(id);
    }
}