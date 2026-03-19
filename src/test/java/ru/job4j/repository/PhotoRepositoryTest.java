package ru.job4j.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import ru.job4j.model.Post;
import ru.job4j.model.User;
import ru.job4j.model.Photo;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class PhotoRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PhotoRepository photoRepository;

    private final List<User> users = List.of(
            new User("user1", "password", "email1"),
            new User("user2", "password", "email2")
    );

    @Test
    public void addPhotoToPost() {
        Post post = new Post(users.get(0), "title", "description");
        Photo photo = new Photo(post, "name", "url");
        userRepository.save(users.get(0));
        postRepository.save(post);
        photoRepository.save(photo);
        var foundPost = photoRepository.findById(photo.getId());
        assertThat(foundPost).isPresent();
        assertThat(photo).isEqualTo(foundPost.get());
    }

    @Test
    public void whenDeleteCascadePostThenDeleteCascadeToo() {
        Post post = new Post(users.get(0), "title", "description");
        Photo photo = new Photo(post, "name", "url");
        userRepository.save(users.get(0));
        postRepository.save(post);
        photoRepository.save(photo);
        postRepository.deleteAllInBatch();
        assertThat(0).isEqualTo(photoRepository.findAll().size());
    }
}