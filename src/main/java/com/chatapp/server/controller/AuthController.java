package com.chatapp.server.controller;

import com.chatapp.server.User;
import com.chatapp.server.dto.RegisterRequest;
import com.chatapp.server.repository.UserRepository;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
    public String register(@RequestBody RegisterRequest req) {

        User user = new User();
        user.setUsername(req.username);
        user.setEmail(req.email);
        
        user.setPasswordHash(
            encoder.encode(req.password)
        );

        repo.save(user);

        return "OK";
    }
    
    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody RegisterRequest req)
    {
        User user = repo.findByEmail(req.email);
        
//        if (user == null) {
//            return "User not found";
//        }

        boolean ok = encoder.matches(
            req.password,
            user.getPasswordHash()
        );

        if (!ok || user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok().build();
    }
}