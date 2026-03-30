package ru.job4j.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "chats")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = "id")
@ToString
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    @NotNull(message = "Поле sender не может быть пустым")
    @Schema(description = "Кто отправляет сообщение")
    private User sender;

    @ManyToOne
    @JoinColumn(nullable = false)
    @NotNull(message = "Поле recipient не может быть пустым")
    @Schema(description = "Кто получает сообщение")
    private User recipient;

    @Size(max = 1000, message = "Description не должен превышать 1000 символов")
    @Column(length = 1000)
    @Schema(description = "Описание сообщения", example = "КалякаМаляка...")
    private String description;

    @Column(name = "created_at")
    private Instant createAt = Instant.now();

    public Chat(User sender, User recipient, String description) {
        this.sender = sender;
        this.recipient = recipient;
        this.description = description;
    }
}
