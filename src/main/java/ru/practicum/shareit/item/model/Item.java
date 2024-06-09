package ru.practicum.shareit.item.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner; // номер пользователя которому принадлежит вещь
    @Column(name = "status")
    private Boolean isAvailable; // статус вещи: доступна не доступна
    @ManyToOne
    @JoinColumn(name = "request_id")
    private ItemRequest request; // если вещь создана по запросу, то тут будет ссылка на запрос
    @OneToMany(mappedBy = "item")
    private List<Comment> comments = new ArrayList<>(); // список комментариев
    @OneToMany(mappedBy = "item")
    private List<Booking> bookings; // список бронирований данной вещи

    public Item(long itemId, String name, String description, User owner, Boolean isAvailable) {
        this.itemId = itemId;
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.isAvailable = isAvailable;
    }

    public Item(String name, String description, User owner, boolean isAvailable, ItemRequest request) {
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.isAvailable = isAvailable;
        this.request = request;
    }

    public Item(String name, String description, User owner, Boolean isAvailable) {
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.isAvailable = isAvailable;
    }

    public Item(long itemId, String name, String description, User owner, Boolean isAvailable, ItemRequest request) {
        this.itemId = itemId;
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.isAvailable = isAvailable;
        this.request = request;
    }
}
