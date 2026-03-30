package ru.job4j.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import ru.job4j.model.Photo;
import ru.job4j.model.Post;
import ru.job4j.model.User;
import ru.job4j.model.dto.UserPostsDto;
import ru.job4j.repository.PhotoRepository;
import ru.job4j.repository.PostRepository;

@Service
@AllArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final PhotoRepository photoRepository;

    @Transactional(rollbackFor = {Exception.class})
    public Post addPost(User user, String title, String description, List<Photo> photos) {
        var post = new Post(user, title, description);
        postRepository.save(post);
        addPhoto(post.getId(), photos);
        return post;
    }

    @Transactional(rollbackFor = {Exception.class})
    public List<Photo> addPhoto(Long postId, List<Photo> photos) {
        Post post = postRepository.getReferenceById(postId);
        if (!photos.isEmpty()) {
            for (Photo photo : photos) {
                photo.setPost(post);
            }
            photoRepository.saveAll(photos);
        }
        return photos;
    }

    @Transactional
    public List<UserPostsDto> getUserPostsDto(List<Long> userIds) {
        return postRepository.findAllByUserIdInOrderByCreateAtDesc(userIds)
                .stream()
                .collect(Collectors.groupingBy(Post::getUser))
                .entrySet()
                .stream()
                .map(entry -> new UserPostsDto(
                        entry.getKey().getId(),
                        entry.getKey().getUsername(),
                        entry.getValue()
                ))
                .sorted(Comparator.comparing(UserPostsDto::getUserId))
                .toList();
    }

    @Transactional(rollbackFor = {Exception.class})
    public Optional<Post> getPost(Long postId) {
        return postRepository.findById(postId);
    }

    @Transactional(rollbackFor = {Exception.class})
    public List<Photo> getPhoto(Long postId) {
        return postRepository.findAllPhotosByPostId(postId);
    }

    @Transactional(rollbackFor = {Exception.class})
    public int updatePost(Post post) {
        var optional = postRepository.findById(post.getId());
        var rsl = optional.isPresent() ? 1 : 0;
        if (rsl > 0) {
            Post original = optional.get();
            if (!original.getTitle().equals(post.getTitle())) {
                original.setTitle(post.getTitle());
            }
            if (!original.getDescription().equals(post.getDescription())) {
                original.setDescription(post.getDescription());
            }
        }
        return rsl;
    }

    @Transactional(rollbackFor = {Exception.class})
    public int deletePost(Long postId) {
        return postRepository.deleteByPostId(postId);
    }

    @Transactional(rollbackFor = {Exception.class})
    public int deletePhoto(Long postId, List<Long> photoIds) {
        var rsl = 0;
        if (!photoIds.isEmpty()) {
            rsl = postRepository.deleteByPostIdAndPhotoIds(postId, photoIds);
        }
        return rsl;
    }
}
