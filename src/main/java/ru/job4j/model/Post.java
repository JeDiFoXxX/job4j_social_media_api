package ru.job4j.model;

import jakarta.persistence.*;
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
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Setter
    @Column(length = 100, nullable = false)
    private String title;

    @Setter
    @Column(length = 1000, nullable = false)
    private String description;

    @Column(name = "created_at")
    private Instant createAt = Instant.now();

    public Post(User user, String title, String description) {
        this.user = user;
        this.title = title;
        this.description = description;
    }
}
