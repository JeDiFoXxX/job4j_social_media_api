package ru.job4j.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.job4j.model.Subscription;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    @Transactional
    @Modifying(clearAutomatically = true)
    int deleteByFollowerIdAndFollowedId(Long followerId, Long followedId);
}
