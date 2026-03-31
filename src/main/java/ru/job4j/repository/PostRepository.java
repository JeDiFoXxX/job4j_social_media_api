package ru.job4j.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.time.Instant;
import java.util.List;

import ru.job4j.model.Photo;
import ru.job4j.model.Post;

@Transactional(readOnly = true)
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByUserId(Long userId);

    List<Post> findByUserIdAndCreateAtBetween(Long userId, Instant after, Instant before);

    Page<Post> findByUserIdOrderByCreateAtDesc(Long userId, Pageable pageable);

    List<Post> findAllByUserIdInOrderByCreateAtDesc(List<Long> userIds);

    @Query(
            value = """
                    SELECT * FROM photos
                    WHERE post_id = :postId
                    """, nativeQuery = true)
    List<Photo> findAllPhotosByPostId(@Param("postId") Long postId);

    @Query(
            value = """
                    SELECT p.* FROM posts p
                    JOIN subscriptions s ON s.followed_id = p.user_id
                    WHERE s.follower_id = :userId
                    ORDER BY p.created_at DESC
                    """, nativeQuery = true)
    Page<Post> findFeedByUserIdWithPagination(@Param("userId") Long userId, Pageable pageable);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(
            value = """
                    UPDATE posts
                    SET title = :title, description = :description
                    WHERE id = :id
                    """, nativeQuery = true)
    int updateTitleAndDescriptionByPostId(@Param("id") Long postId,
                                          @Param("title") String title,
                                          @Param("description") String description);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(
            value = """
                    DELETE FROM posts
                    WHERE id = :id
                    """, nativeQuery = true)
    int deleteByPostId(@Param("id") Long postId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(
            value = """
                    DELETE FROM photos
                    WHERE post_id = :postId
                    """, nativeQuery = true)
    int deletePhotoByPostId(@Param("postId") Long postId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(
            value = """
                    DELETE FROM photos
                    WHERE post_id = :postId
                    AND id IN (:photoIds)
                    """, nativeQuery = true)
    int deleteByPostIdAndPhotoIds(@Param("postId") Long postId,
                                  @Param("photoIds") List<Long> photoIds);
}