package ru.practicum.shareit.item.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "comments")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Comment {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; // идентификатор комментария
    private String text; // содержание комментария
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item; // вещь на которую оставлен комментарий
    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author; // номер пользователя которому принадлежит комментарий
    @Column(name = "created")
    private LocalDateTime create; // дата и время создания комментария

    public Comment(String text) {
        this.text = text;
    }

    public Comment(String text, User author, LocalDateTime create) {
        this.text = text;
        this.author = author;
        this.create = create;
    }
}
