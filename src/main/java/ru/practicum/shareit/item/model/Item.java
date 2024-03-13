package ru.practicum.shareit.item.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Entity
@Table(name = "items")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Item {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long itemId; // идентификатор в базе
    private String name; // наименование
    private String description; // описание
    @Column(name = "owner_id")
    private long owner; // номер пользователя которому принадлежит вещь
    private Boolean status; // статус вещи: доступна не доступна
    @Column(name = "request_id")
    private Long request; // если вещь создана по запросу, то тут будет ссылка на номер этого запроса
    @Transient
    private List<Comment> comments = new ArrayList<>(); // список комментариев


    public Item(String name, String description, Boolean status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }
}
