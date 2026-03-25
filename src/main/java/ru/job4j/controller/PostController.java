package ru.job4j.controller;


import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import ru.job4j.model.Photo;
import ru.job4j.model.Post;
import ru.job4j.model.dto.PostRequest;
import ru.job4j.service.PostService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/post")
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<Post> add(@RequestBody PostRequest postRequest) {
        var savePost = postService.addPost(
                postRequest.getUser(),
                postRequest.getTitle(),
                postRequest.getDescription(),
                postRequest.getPhotos()
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
    public ResponseEntity<List<Photo>> addPhoto(@PathVariable("postId") Long postId,
                                                @RequestBody List<Photo> photos) {
        var savePhotos = postService.addPhoto(postId, photos);
        var uri = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
        return ResponseEntity.created(uri).body(savePhotos);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<Post> get(@PathVariable("postId") Long postId) {
        return postService.getPost(postId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{postId}/photo")
    public ResponseEntity<List<Photo>> getPhoto(@PathVariable("postId") Long postId) {
        return ResponseEntity.ok(postService.getPhoto(postId));
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody Post post) {
        return postService.updatePost(post) > 0
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> delete(@PathVariable("postId") Long postId) {
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
