package ru.job4j.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.job4j.model.Friendship;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> { }
