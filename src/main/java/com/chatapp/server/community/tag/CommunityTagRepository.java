package com.chatapp.server.community.tag;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityTagRepository extends JpaRepository<CommunityTag, UUID> {


}