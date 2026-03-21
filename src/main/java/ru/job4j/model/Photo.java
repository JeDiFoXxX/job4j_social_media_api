package ru.job4j.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "photos")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = "id")
@ToString
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Setter
    private Post post;

    @Column(name = "photo_name", length = 512, nullable = false)
    private String name;

    @Column(name = "photo_url",  length = 512, nullable = false)
    private String url;

    public Photo(Post post, String name, String url) {
        this.post = post;
        this.name = name;
        this.url = url;
    }
}
