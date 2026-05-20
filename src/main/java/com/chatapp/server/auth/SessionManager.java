package com.chatapp.server.auth;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/session")
public class SessionManager {

		private static final Map<String, UUID> sessions = new HashMap<>();
		
		@PostMapping("/create")
		public String createSession(UUID userId) 
		{
			String token = UUID.randomUUID().toString();
			sessions.put(token, userId);
			return token;
		}
		
		@GetMapping("/validate")
		public boolean isValid(@RequestParam String token)
		{
			return sessions.containsKey(token);
		}
		
		public UUID getUserId(String token) 
		{
			return sessions.get(token);
		}
		
		public void removeSession(String token)
		{
			sessions.remove(token);
		}
}
