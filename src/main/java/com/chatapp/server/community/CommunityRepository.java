package com.chatapp.server.community;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityRepository extends JpaRepository<Community, UUID> {
	
	boolean existsByNameIgnoreCase(String name);

}
