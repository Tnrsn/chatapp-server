package com.chatapp.server.community.tag;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, UUID> {

	Optional<Tag> findByNameIgnoreCase(String name);
}