package ru.job4j.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import org.springframework.dao.DataIntegrityViolationException;

import ru.job4j.model.Friendship;
import ru.job4j.model.User;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class FriendshipRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendshipRepository friendshipRepository;

    private final List<User> users = List.of(
            new User("user1", "password", "email1"),
            new User("user2", "password", "email2")
    );

    @Test
    public void addFriendship() {
        Friendship friendship = new Friendship(users.get(0), users.get(1));
        userRepository.saveAll(users);
        friendshipRepository.save(friendship);
        var foundFriendship = friendshipRepository.findById(friendship.getId());
        assertThat(foundFriendship).isPresent();
        assertThat(friendship).isEqualTo(foundFriendship.get());
    }

    @Test
    public void addFriendshipIsInvalidThenUniqueConstraint() {
        userRepository.saveAll(users);
        friendshipRepository.save(new Friendship(users.get(0), users.get(1)));
        assertThatThrownBy(() -> {
            friendshipRepository.save(new Friendship(users.get(0), users.get(1)));
        }).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    public void addFriendshipIsInvalidThenCheckConstraint() {
        userRepository.saveAll(users);
        assertThatThrownBy(() -> {
            friendshipRepository.save(new Friendship(users.get(0), users.get(0)));
        }).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    public void whenDeleteCascadeUserThenDeleteCascadeToo() {
        Friendship friendship = new Friendship(users.get(0), users.get(1));
        userRepository.saveAll(users);
        friendshipRepository.save(friendship);
        userRepository.deleteAllInBatch();
        assertThat(0).isEqualTo(friendshipRepository.findAll().size());
    }
}