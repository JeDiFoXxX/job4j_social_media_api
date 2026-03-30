package ru.job4j.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @NotNull(message = "Поле post не может быть пустым")
    @Setter
    private Post post;

    @Setter
    @NotBlank(message = "Поле name не может быть пустым")
    @Size(max = 512, message = "Name не должен превышать 512 символов")
    @Column(name = "photo_name", length = 512, nullable = false)
    private String name;

    @Setter
    @NotBlank(message = "Поле url не может быть пустым")
    @Size(max = 512, message = "URL не должен превышать 512 символов")
    @Column(name = "photo_url",  length = 512, nullable = false)
    private String url;

    public Photo(Post post, String name, String url) {
        this.post = post;
        this.name = name;
        this.url = url;
    }
}
