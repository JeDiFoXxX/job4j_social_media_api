package ru.job4j.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import org.springframework.data.domain.PageRequest;
import ru.job4j.model.Post;
import ru.job4j.model.Subscription;
import ru.job4j.model.User;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class PostRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    private final List<User> users = List.of(
            new User("user1", "123456789A@a", "1@gmail.com"),
            new User("user2", "987654321A@a", "2@gmail.com"),
            new User("user3", "999999999A@a", "3@gmail.com")
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

    @Test
    public void findFeedByUserIdWithPagination() {
        userRepository.saveAll(users);
        subscriptionRepository.save(new Subscription(users.get(0), users.get(1)));
        subscriptionRepository.save(new Subscription(users.get(0), users.get(2)));
        postRepository.saveAndFlush(new Post(users.get(1), "title3", "description3"));
        postRepository.saveAndFlush(new Post(users.get(2), "title2", "description2"));
        postRepository.saveAndFlush(new Post(users.get(1), "title1", "description1"));
        List<String> expected = postRepository
                .findFeedByUserIdWithPagination(users.get(0).getId(),
                        PageRequest.of(0, 2))
                .stream()
                .map(Post::getTitle)
                .toList();
        System.out.println(expected);
        assertThat(List.of("title1", "title2")).containsExactlyElementsOf(expected);
    }
}