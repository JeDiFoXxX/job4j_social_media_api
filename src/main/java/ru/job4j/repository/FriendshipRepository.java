package ru.job4j.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import ru.job4j.model.Friendship;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    @Query(value = """
            SELECT * FROM friendships
            WHERE (user_id = :followerId AND friend_id = :followedId) 
               OR (user_id = :followedId AND friend_id = :followerId)
            """, nativeQuery = true)
    List<Friendship> findPair(@Param("followerId") Long followerId,
                              @Param("followedId") Long followedId);

    @Transactional
    @Modifying(clearAutomatically = true)
    int deleteByUserIdAndFriendId(Long userId, Long friendId);
}
