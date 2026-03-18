package ru.job4j.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.job4j.model.Post;

public interface PostRepository extends JpaRepository<Post, Long> { }
