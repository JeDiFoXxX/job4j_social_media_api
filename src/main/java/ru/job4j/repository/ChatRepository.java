package ru.job4j.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.job4j.model.Chat;

public interface ChatRepository extends JpaRepository<Chat, Long> { }
