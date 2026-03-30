package ru.job4j.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import ru.job4j.model.User;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    private final List<User> users = List.of(
            new User("user1", "123456789A@a", "1@gmail.com"),
            new User("user2", "987654321A@a", "2@gmail.com")
    );

    @Test
    public void addUser() {
        var user = users.get(0);
        userRepository.save(user);
        var foundUser = userRepository.findById(user.getId());
        assertThat(foundUser).isPresent();
        assertThat(user).isEqualTo(foundUser.get());

    }
}