package ru.practicum.shareit.request.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@Entity
@Table(name = "requests")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; //идентификатор запроса
    private String description; // описание запроса
    @ManyToOne
    @JoinColumn(name = "requester_id")
    private User requester; // пользователь, который создал запроса
    private LocalDateTime created; // дата и время создания запроса
    @OneToMany(mappedBy = "request", cascade = CascadeType.ALL)
    private List<Item> items;  // добавление идентификатора вещи созданной в ответ на запрос

    public ItemRequest(long id, String description, User requester, LocalDateTime created) {
        this.id = id;
        this.description = description;
        this.requester = requester;
        this.created = created;
    }
}