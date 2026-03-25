package ru.job4j.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import lombok.Setter;
import ru.job4j.model.Photo;
import ru.job4j.model.User;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostRequest {
    private User user;
    private String title;
    private String description;
    private List<Photo> photos;
}
