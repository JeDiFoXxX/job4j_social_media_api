package ru.job4j.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import ru.job4j.model.Post;
import ru.job4j.model.User;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class PostRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    private final List<User> users = List.of(
            new User("user1", "password", "email1"),
            new User("user2", "password", "email2")
    );

    @Test
    public void addPostToUser() {
        Post post = new Post(users.get(0), "title", "description");
        userRepository.save(users.get(0));
        postRepository.save(post);
        var foundPost = postRepository.findById(post.getId());
        assertThat(foundPost).isPresent();
        assertThat(post).isEqualTo(foundPost.get());
    }

    @Test
    public void whenDeleteCascadeUserThenDeleteCascadeToo() {
        Post post = new Post(users.get(0), "title", "description");
        userRepository.saveAll(users);
        postRepository.save(post);
        userRepository.deleteAllInBatch();
        assertThat(0).isEqualTo(postRepository.findAll().size());
    }
}