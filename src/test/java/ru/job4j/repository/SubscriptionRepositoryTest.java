package ru.job4j.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import org.springframework.dao.DataIntegrityViolationException;

import ru.job4j.model.Subscription;
import ru.job4j.model.User;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class SubscriptionRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    private final List<User> users = List.of(
            new User("user1", "password", "email1"),
            new User("user2", "password", "email2")
    );

    @Test
    public void addSubscriptionIsValid() {
        Subscription subscription = new Subscription(users.get(0), users.get(1));
        userRepository.saveAll(users);
        subscriptionRepository.save(subscription);
        var expected = subscriptionRepository.findById(subscription.getId());
        assertThat(expected).isPresent();
        assertThat(users.get(0)).isEqualTo(expected.get().getFollower());
    }

    @Test
    public void addSubscriptionIsInvalidThenUniqueConstraint() {
        userRepository.saveAll(users);
        subscriptionRepository.save(new Subscription(users.get(0), users.get(1)));
        assertThatThrownBy(() -> {
            subscriptionRepository.save(new Subscription(users.get(0), users.get(1)));
        }).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    public void addSubscriptionIsInvalidThenCheckConstraint() {
        userRepository.saveAll(users);
        assertThatThrownBy(() -> {
            subscriptionRepository.save(new Subscription(users.get(0), users.get(0)));
        }).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    public void whenDeleteCascadeUserThenDeleteCascadeToo() {
        Subscription subscription = new Subscription(users.get(0), users.get(1));
        userRepository.saveAll(users);
        subscriptionRepository.save(subscription);
        userRepository.deleteAllInBatch();
        assertThat(0).isEqualTo(subscriptionRepository.findAll().size());
    }
}