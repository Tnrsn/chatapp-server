package com.chatapp.server.auth;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chatapp.server.repository.UserRepository;

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
		
		@GetMapping("/validate")
		public boolean isValid(@RequestParam String token)
		{
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
