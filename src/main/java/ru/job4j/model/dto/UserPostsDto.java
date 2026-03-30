package ru.job4j.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import ru.job4j.model.Post;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserPostsDto {
    private Long userId;
    private String username;
    private List<Post> posts;
}
