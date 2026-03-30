package ru.job4j.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import ru.job4j.model.User;
import ru.job4j.service.UserService;

@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<User> add(@Valid
                                    @RequestBody User user) {
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
    public ResponseEntity<User> get(@Min(value = 1, message = "Id не может быть меньше 1")
                                    @PathVariable("userId") Long userId) {
        return userService.getUser(userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping
    public ResponseEntity<Void> update(@Valid
                                       @RequestBody User user) {
        return userService.updateUser(user) > 0
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(@Min(value = 1, message = "Id не может быть меньше 1")
                                       @PathVariable("userId") Long userId) {
        return userService.deleteUser(userId) > 0
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }
}
