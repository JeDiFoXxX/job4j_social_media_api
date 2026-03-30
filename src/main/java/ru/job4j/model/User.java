package ru.job4j.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = "id")
@JsonPropertyOrder({ "id", "username", "password", "email" })
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Setter
    @Schema(description = "Имя аккаунта", example = "Ivanich")
    @NotBlank(message = "Поле username не может быть пустым")
    @Size(min = 3,
            max = 50,
            message = "Username должен быть от 3 до 50 символов")
    @Pattern(
            regexp = "^[a-zA-Z0-9]+$",
            message = "Username может содержать только английские буквы и цифры"
    )
    @Column(unique = true, length = 50, nullable = false)
    private String username;

    @Setter
    @Schema(description = "Пароль от аккаунта", example = "Ivan!ch#1979")
    @NotBlank(message = "Поле password не может быть пустым")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).*$",
            message = "Пароль должен содержать цифры, буквы в разных регистрах и спецсимволы (@#$%^&+=!)"
    )
    @Size(min = 10, max = 50, message = "Пароль должен быть от 10 до 50 символов")
    @Column(length = 50, nullable = false)
    private String password;

    @Setter
    @Schema(description = "Email для аккаунта", example = "Ivan!ch#1979@inbox.ru")
    @NotBlank(message = "Поле email не может быть пустым")
    @Email(message = "Введите корректный адрес электронной почты")
    @Size(max = 100, message = "Email не должен превышать 100 символов")
    @Column(unique = true, length = 100, nullable = false)
    private String email;

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
