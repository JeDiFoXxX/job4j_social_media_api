package ru.job4j.repository;

import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.QueryHints;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import ru.job4j.model.Post;

import java.time.Instant;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByUserId(Long userId);

    List<Post> findByUserIdAndCreateAtBetween(Long userId, Instant after, Instant before);

    Page<Post> findByUserIdOrderByCreateAtDesc(Long userId, Pageable pageable);
}