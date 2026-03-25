package ru.job4j.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.job4j.model.User;
import ru.job4j.service.UserService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<User> add(@RequestBody User user) {
        userService.addUser(user);
        var uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{userId}")
                .buildAndExpand(user.getId())
                .toUri();
        return ResponseEntity.status(HttpStatus.CREATED)
                .location(uri)
                .body(user);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> get(@PathVariable("userId") Long userId) {
        return userService.getUser(userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody User user) {
        return userService.updateUser(user) > 0
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(@PathVariable("userId") Long userId) {
        return userService.deleteUser(userId) > 0
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }
}
