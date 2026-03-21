package ru.job4j.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import ru.job4j.model.Photo;
import ru.job4j.model.Post;
import ru.job4j.model.User;
import ru.job4j.repository.PhotoRepository;
import ru.job4j.repository.PostRepository;

@Service
@AllArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final PhotoRepository photoRepository;

    @Transactional(rollbackFor = {Exception.class})
    public void addPost(User user, String title, String description, List<Photo> photos) {
        var post = new Post(user, title, description);
        postRepository.save(post);
        addPhoto(post, photos);
    }

    @Transactional(rollbackFor = {Exception.class})
    public void addPhoto(Post post, List<Photo> photos) {
        if (photos != null && !photos.isEmpty()) {
            for (Photo photo : photos) {
                photo.setPost(post);
            }
            photoRepository.saveAll(photos);
        }
    }

    @Transactional(rollbackFor = {Exception.class})
    public int deletePhoto(Post post, List<Long> photoIds) {
        var rsl = 0;
        if (photoIds != null && !photoIds.isEmpty()) {
            rsl = postRepository.deleteByPostIdAndPhotoIds(post.getId(), photoIds);
        }
        return rsl;
    }

    @Transactional(rollbackFor = {Exception.class})
    public int updateTitleAndDescriptionPost(Post post, Post newPost) {
        var optional = postRepository.findById(post.getId());
        var rsl = optional.isPresent() ? 1 : 0;
        if (rsl > 0) {
            Post original = optional.get();
            if (!original.getTitle().equals(newPost.getTitle())) {
                original.setTitle(newPost.getTitle());
            }
            if (!original.getDescription().equals(newPost.getDescription())) {
                original.setDescription(newPost.getDescription());
            }
        }
        return rsl;
    }

    @Transactional(rollbackFor = {Exception.class})
    public int deletePost(Post post) {
        return postRepository.deleteByPostId(post.getId());
    }
}
