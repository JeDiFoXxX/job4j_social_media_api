package ru.job4j.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Entity
@Table(name = "posts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = "id")
@ToString
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull(message = "Поле user не может быть пустым")
    @Schema(description = "Пользователь поста")
    private User user;

    @Setter
    @NotBlank(message = "Поле title не может быть пустым")
    @Size(max = 100, message = "Title не должен превышать 100 символов")
    @Column(length = 100, nullable = false)
    @Schema(description = "Заголовок поста", example = "Моя история про лето")
    private String title;

    @Setter
    @NotBlank(message = "Поле description не может быть пустым")
    @Size(max = 1000, message = "Description не должен превышать 1000 символов")
    @Column(length = 1000, nullable = false)
    @Schema(description = "Описание поста", example = "Я шёл шёл и пришёл...")
    private String description;

    @Column(name = "created_at")
    private Instant createAt = Instant.now();

    public Post(User user, String title, String description) {
        this.user = user;
        this.title = title;
        this.description = description;
    }
}
