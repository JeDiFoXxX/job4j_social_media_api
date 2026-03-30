package ru.job4j.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import org.springframework.data.domain.Sort;

import ru.job4j.model.Chat;
import ru.job4j.model.User;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class ChatRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatRepository chatRepository;

    private final List<User> users = List.of(
            new User("user1", "123456789A@a", "1@gmail.com"),
            new User("user2", "987654321A@a", "2@gmail.com")
    );

    @Test
    public void chatIsValid() {
        userRepository.saveAll(users);
        chatRepository.save(new Chat(users.get(0), users.get(1), "1"));
        chatRepository.save(new Chat(users.get(1), users.get(0), "2"));
        chatRepository.save(new Chat(users.get(0), users.get(1), "3"));
        chatRepository.save(new Chat(users.get(1), users.get(0), "4"));
        var foundChat = chatRepository.findAll(Sort.by(Sort.Direction.ASC, "createAt"))
                .stream()
                .map(Chat::getDescription)
                .toList();
        assertThat(List.of("1", "2", "3", "4")).containsExactlyElementsOf(foundChat);
    }
}