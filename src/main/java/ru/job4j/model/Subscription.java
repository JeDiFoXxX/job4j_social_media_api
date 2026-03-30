package ru.job4j.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(
        name = "subscriptions",
        uniqueConstraints = {
                @UniqueConstraint(name = "unique_sub", columnNames = {"follower_id", "followed_id"})
        },
        check = {
                @CheckConstraint(name = "no_self_follow", constraint = "follower_id <> followed_id")
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = "id")
@ToString
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "follower_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull(message = "Поле follower не может быть пустым")
    private User follower;

    @ManyToOne
    @JoinColumn(name = "followed_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull(message = "Поле followed не может быть пустым")
    private User followed;

    public Subscription(User follower, User followed) {
        this.follower = follower;
        this.followed = followed;
    }
}
