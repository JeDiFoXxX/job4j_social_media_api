package ru.job4j.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import ru.job4j.model.Photo;
import ru.job4j.model.Post;
import ru.job4j.model.User;
import ru.job4j.repository.PhotoRepository;
import ru.job4j.repository.PostRepository;
import ru.job4j.repository.UserRepository;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Import(PostService.class)
class PostServiceTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private PostService postService;

    private final List<User> users = List.of(
            new User("user1", "password", "email1"),
            new User("user2", "password", "email2")
    );

    @Test
    public void addPostSimple() {
        userRepository.save(users.get(0));
        postService.addPost(users.get(0), "title", "description", null);
        var foundPost = postRepository.findAll();
        assertThat("title").isEqualTo(foundPost.get(0).getTitle());
        assertThat("description").isEqualTo(foundPost.get(0).getDescription());
    }

    @Test
    public void addPostWithPhotos() {
        Photo photo = new Photo(null, "photo", "url");
        userRepository.save(users.get(0));
        postService.addPost(users.get(0), "title", "description", List.of(photo));
        var foundPost = postRepository.findAll();
        var foundPhoto = photoRepository.findAll();
        assertThat("title").isEqualTo(foundPost.get(0).getTitle());
        assertThat("photo").isEqualTo(foundPhoto.get(0).getName());
        assertThat(foundPost.get(0).getId())
                .isEqualTo(foundPhoto.get(0).getPost().getId());
    }

    @Test
    public void deletePhotosFromPost() {
        Photo photo = new Photo(null, "photo", "url");
        userRepository.save(users.get(0));
        postService.addPost(users.get(0), "title", "description", List.of(photo));
        var foundPost = postRepository.findAll();
        var foundPhoto = photoRepository.findAll();
        var expected = postService.deletePhoto(foundPost.get(0), List.of(foundPhoto.get(0).getId()));
        assertThat(1).isEqualTo(expected);
    }

    @Test
    public void deletePostFull() {
        userRepository.save(users.get(0));
        postService.addPost(users.get(0), "title", "description", null);
        var foundPost = postRepository.findAll();
        var expected = postService.deletePost(foundPost.get(0));
        assertThat(1).isEqualTo(expected);
    }

    @Test
    public void updatePostContent() {
        userRepository.save(users.get(0));
        postService.addPost(users.get(0), "title", "description", null);
        var foundPost = postRepository.findAll();
        postService.updateTitleAndDescriptionPost(
                foundPost.get(0),
                new Post(null, "title1", "description1"));
        assertThat("title1").isEqualTo(foundPost.get(0).getTitle());
        assertThat("description1").isEqualTo(foundPost.get(0).getDescription());
    }
}