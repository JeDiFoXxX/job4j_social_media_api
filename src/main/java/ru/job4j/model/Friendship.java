package ru.job4j.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(
        name = "friendships",
        uniqueConstraints = {
                @UniqueConstraint(name = "unique_friendship", columnNames = {"user_id", "friend_id"})
        },
        check = {
                @CheckConstraint(name = "no_self_friend", constraint = "user_id <> friend_id")
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = "id")
@ToString
public class Friendship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull(message = "Поле user не может быть пустым")
    @Schema(description = "Кто подписался")
    private User user;

    @OneToOne
    @JoinColumn(name = "friend_id", nullable = false)
    @NotNull(message = "Поле friend не может быть пустым")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Schema(description = "На кого подписался")
    private User friend;

    @Setter
    private boolean accepted = false;

    public Friendship(User user, User friend) {
        this.user = user;
        this.friend = friend;
    }
}
