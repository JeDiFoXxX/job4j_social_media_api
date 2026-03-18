package ru.job4j.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.job4j.model.Photo;

public interface PhotoRepository extends JpaRepository<Photo, Long> { }
