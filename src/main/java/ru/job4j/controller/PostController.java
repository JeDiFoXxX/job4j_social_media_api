package ru.job4j.controller;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import ru.job4j.model.Photo;
import ru.job4j.model.Post;
import ru.job4j.model.dto.PostPhotosDto;
import ru.job4j.model.dto.UserPostsDto;
import ru.job4j.service.PostService;

import java.util.List;

@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/api/post")
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<Post> add(@Valid @RequestBody PostPhotosDto postPhotosDto) {
        var savePost = postService.addPost(
                postPhotosDto.getUser(),
                postPhotosDto.getTitle(),
                postPhotosDto.getDescription(),
                postPhotosDto.getPhotos()
        );
        var uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{postId}")
                .buildAndExpand(savePost.getId())
                .toUri();
        return ResponseEntity.status(HttpStatus.CREATED)
                .location(uri)
                .body(savePost);
    }

    @PostMapping("/{postId}/photo")
    public ResponseEntity<List<Photo>> addPhoto(@Min(value = 1, message = "Id не может быть меньше 1")
                                                @PathVariable("postId") Long postId,
                                                @Valid
                                                @NotNull(message = "Поле photos не может быть null")
                                                @RequestBody List<Photo> photos) {
        var savePhotos = postService.addPhoto(postId, photos);
        var uri = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
        return ResponseEntity.created(uri).body(savePhotos);
    }

    @GetMapping("/{userIds}/posts")
    public ResponseEntity<List<UserPostsDto>> getUserPosts(
            @Min(value = 1, message = "Id не может быть меньше 1")
            @PathVariable("userIds") List<Long> userIds) {
        var posts = postService.getUserPostsDto(userIds);
        return !posts.isEmpty() ? ResponseEntity.ok(posts) : ResponseEntity.notFound().build();
    }

    @GetMapping("/{postId}")
    public ResponseEntity<Post> get(@Min(value = 1, message = "Id не может быть меньше 1")
                                    @PathVariable("postId") Long postId) {
        return postService.getPost(postId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{postId}/photo")
    public ResponseEntity<List<Photo>> getPhoto(@Min(value = 1, message = "Id не может быть меньше 1")
                                                @PathVariable("postId") Long postId) {
        return ResponseEntity.ok(postService.getPhoto(postId));
    }

    @PutMapping
    public ResponseEntity<Void> update(@Valid
                                       @RequestBody Post post) {
        return postService.updatePost(post) > 0
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> delete(@Min(value = 1, message = "Id не может быть меньше 1")
                                       @PathVariable("postId") Long postId) {
        return postService.deletePost(postId) > 0
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{postId}/photo")
    public ResponseEntity<Void> deletePhoto(@PathVariable("postId") Long postId,
                                            @RequestParam("ids") List<Long> photosIds) {
        return postService.deletePhoto(postId, photosIds) > 0
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }
}
