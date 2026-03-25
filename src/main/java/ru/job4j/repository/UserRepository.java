package ru.job4j.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import ru.job4j.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query(
            value = """
            SELECT * FROM users
            WHERE username = :username
            AND password = :password
            """, nativeQuery = true)
    List<User> findByUsernameAndPassword(@Param("username") String username,
                                             @Param("password") String password);

    @Query(
            value = """
                    SELECT u.* FROM users u
                    JOIN subscriptions s ON s.follower_id = u.id
                    WHERE s.followed_id = :userId
                    """, nativeQuery = true)
    List<User> findFollowerByUserId(@Param("userId") Long userId);

    @Query(
            value = """
                    SELECT u.* FROM users u
                    JOIN friendships f ON f.friend_id = u.id
                    WHERE f.user_id = :userId
                    AND accepted IS TRUE
                    """, nativeQuery = true)
    List<User> findFriendByUserId(@Param("userId") Long userId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(
            value = """
                    DELETE FROM users
                    WHERE id = :id
                    """, nativeQuery = true)
    int deleteByUserId(@Param("id") Long userId);
}
