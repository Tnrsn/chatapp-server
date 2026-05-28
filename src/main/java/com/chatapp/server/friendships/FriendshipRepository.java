package com.chatapp.server.friendships;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.chatapp.server.user.User;

public interface FriendshipRepository extends JpaRepository<Friendship, UUID> {
	boolean existsByRequesterIdAndReceiverId(UUID requesterId, UUID receiverId);

	@Query("""
			SELECT u FROM User u
			WHERE u.id IN (
				SELECT f.requesterId
				FROM Friendship f
				WHERE f.receiverId = :userId
				AND f.status = 'pending'
			)
			""")
	List<User> findPendingRequests(@Param("userId") UUID userId);
	
	@Modifying
	@Transactional
	@Query("""
	    UPDATE Friendship f
	    SET f.status = 'accepted'
	    WHERE f.requesterId = :requesterId
	    AND f.receiverId = :receiverId
	    """)
	void acceptFriendRequest(@Param("requesterId") UUID requesterId, @Param("receiverId") UUID receiverId);
	
	@Modifying
	@Transactional
	@Query("""
		    DELETE FROM Friendship f
		    WHERE f.requesterId = :requesterId
		    AND f.receiverId = :receiverId
		    """)
	void declineFriendRequest(@Param("requesterId") UUID requesterId, @Param("receiverId") UUID receiverId);
	
	@Query("""
			SELECT u FROM User u
			WHERE u.id IN (
			    SELECT CASE
			        WHEN f.requesterId = :userId THEN f.receiverId
			        ELSE f.requesterId
			    END
			    FROM Friendship f
			    WHERE (f.requesterId = :userId OR f.receiverId = :userId)
			    AND f.status = 'accepted'
			)
			""")
			List<User> findFriends(@Param("userId") UUID userId);
}
