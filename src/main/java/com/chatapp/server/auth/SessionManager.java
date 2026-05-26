package com.chatapp.server.auth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chatapp.server.user.User;
import com.chatapp.server.user.UserRepository;
import com.chatapp.server.user.UserService;

@RestController
@RequestMapping("/session")
public class SessionManager {

		private static final Map<String, UUID> sessions = new HashMap<>();
		@Autowired
		private UserRepository repo;
		
		public static String createSession(UUID userId) 
		{
			System.out.println("id  " + userId);
			String token = UUID.randomUUID().toString();
			sessions.put(token, userId);
			return token;
		}
		
		private final UserService userService;
		
	    public SessionManager(UserService userService) {
	        this.userService = userService;
	    }
		
		@GetMapping("/validate")
		public boolean isValid(@RequestParam String token)
		{
			List<User> users = userService.searchUsers("tan");

			for (User user : users) {
			    System.out.println(user.getUsername());
			}
			return sessions.containsKey(token);
		}
		
		public static UUID getUserId(String token) 
		{
			System.out.println("This is the token" + token);
			return sessions.get(token);
		}
		
		public void removeSession(String token)
		{
			sessions.remove(token);
		}
}
