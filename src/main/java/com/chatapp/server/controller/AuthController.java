package com.chatapp.server.controller;

import com.chatapp.server.auth.LoginResponse;
import com.chatapp.server.auth.SessionManager;
import com.chatapp.server.dto.RegisterRequest;
import com.chatapp.server.user.User;
import com.chatapp.server.user.UserRepository;

import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository repo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AuthController(UserRepository repo) {
        this.repo = repo;
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(@RequestBody RegisterRequest req) {

    	System.out.println("Register");
    	
        User user = new User();
        user.setUsername(req.username);
        user.setEmail(req.email);
        
        user.setPasswordHash(
            encoder.encode(req.password)
        );

        repo.save(user);

        String usertoken = SessionManager.createSession(user.getId());
        return ResponseEntity.ok(new LoginResponse(usertoken, user.getUsername()));
    }
    
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody RegisterRequest req)
    {
        User user = repo.findByEmail(req.email);
//    	System.out.println("Login");
        
        if (user == null) //Return UNAUTHORIZED if user not found
        {
        	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        boolean ok = encoder.matches(
            req.password,
            user.getPasswordHash()
        );

        if (!ok) { //Return UNAUTHORIZED if wrong password
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        String usertoken = SessionManager.createSession(user.getId());
        return ResponseEntity.ok(new LoginResponse(usertoken, user.getUsername()));
    }
    
    @GetMapping("/username")
    public ResponseEntity<String> getUsername(@RequestParam String token)
    {
    	UUID userId = SessionManager.getUserId(token);
    	
    	if(userId == null)
    		return ResponseEntity.status(401).body("Invalid Token");
    	
    	User user = repo.findById(userId).orElse(null);
    	
    	if(user == null)
    		return ResponseEntity.status(404).body("User not found");
    	
    	return ResponseEntity.ok(user.getUsername());
    }
}