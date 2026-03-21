package ru.job4j.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import ru.job4j.model.User;
import ru.job4j.repository.*;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Import(SubscribeService.class)
class SubscribeServiceTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private SubscribeService subscribeService;

    private final List<User> users = List.of(
            new User("user1", "password", "email1"),
            new User("user2", "password", "email2")
    );

    @Test
    public void subscribe() {
        userRepository.saveAll(users);
        subscribeService.subscribe(users.get(0), users.get(1));
        subscribeService.subscribe(users.get(1), users.get(0));
        var expected = friendshipRepository.findPair(users.get(0).getId(), users.get(1).getId());
        assertThat(true).isEqualTo(expected.get(0).isAccepted());
        assertThat(true).isEqualTo(expected.get(1).isAccepted());
    }

    @Test
    public void unsubscribe() {
        userRepository.saveAll(users);
        subscribeService.subscribe(users.get(0), users.get(1));
        subscribeService.subscribe(users.get(1), users.get(0));
        subscribeService.unsubscribe(users.get(1), users.get(0));
        var expected = friendshipRepository.findPair(users.get(0).getId(), users.get(1).getId());
        assertThat(1).isEqualTo(expected.size());
        assertThat(false).isEqualTo(expected.get(0).isAccepted());
    }

}