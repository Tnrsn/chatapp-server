package com.chatapp.server.friendships;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "friendships")
public class Friendship {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "requester_id")
    private UUID requesterId;

    @Column(name = "receiver_id")
    private UUID receiverId;

    private String status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
