package com.chatapp.server.friendships;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.chatapp.server.user.User;

public interface FriendshipRepository extends JpaRepository<Friendship, UUID> {
	

}
