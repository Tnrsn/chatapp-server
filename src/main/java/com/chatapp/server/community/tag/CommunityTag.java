package com.chatapp.server.community.tag;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "community_tag")
public class CommunityTag {

    @Id
    @GeneratedValue
    private UUID id;
    @Column(name = "community_id", nullable = false)
    private UUID communityId;
    @Column(name = "tag_id", nullable = false)
    private UUID tagId;

	public UUID getCommunityId() {
		return communityId;
	}

	public void setCommunityId(UUID communityId) {
		this.communityId = communityId;
	}

	public UUID getTagId() {
		return tagId;
	}

	public void setTagId(UUID tagId) {
		this.tagId = tagId;
	}
}
