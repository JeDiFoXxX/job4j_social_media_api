package ru.job4j.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import ru.job4j.model.User;
import ru.job4j.repository.UserRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional(rollbackFor = {Exception.class})
    public void addUser(User user) {
        userRepository.save(user);
    }

    @Transactional(rollbackFor = {Exception.class})
    public Optional<User> getUser(Long userId) {
        return userRepository.findById(userId);
    }

    @Transactional(rollbackFor = {Exception.class})
    public int updateUser(User user) {
        var optional = userRepository.findById(user.getId());
        var rsl = optional.isPresent() ? 1 : 0;
        if (rsl > 0) {
            User original = optional.get();
            if (!original.getUsername().equals(user.getUsername())) {
                original.setUsername(user.getUsername());
            }
            if (!original.getPassword().equals(user.getPassword())) {
                original.setPassword(user.getPassword());
            }
            if (!original.getEmail().equals(user.getEmail())) {
                original.setEmail(user.getEmail());
            }
        }
        return rsl;
    }

    @Transactional(rollbackFor = {Exception.class})
    public int deleteUser(Long userId) {
        return userRepository.deleteByUserId(userId);
    }
}
