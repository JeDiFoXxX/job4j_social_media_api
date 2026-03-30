package ru.job4j.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class PostPhotosDto {
    @Valid
    @Schema(description = "Пользователь", example = "Ivanich")
    private User user;

    @NotBlank(message = "Поле title не может быть пустым")
    @Size(max = 100, message = "Title не должен превышать 100 символов")
    @Schema(description = "Заголовок поста", example = "Моя история про лето")
    private String title;

    @NotBlank(message = "Поле description не может быть пустым")
    @Size(max = 1000, message = "Description не должен превышать 1000 символов")
    @Schema(description = "Описание поста", example = "Я шёл шёл и пришёл...")
    private String description;

    @Valid
    @NotNull(message = "Поле photos не может быть null")
    @Schema(description = "Список фото к посту")
    private List<Photo> photos;
}
