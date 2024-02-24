package ru.practicum.shareit.item.model;

import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class Item {

    private long itemId; // идентификатор в базе
    private String itemName; // наименование
    private String description; // описание
    private long owner; // номер пользователя которому принадлежит вещь
    private Boolean status; // статус вещи: доступна не доступна
   //private List<Feedback> feedbacks; // таблица с отзывами, пользователь и описание
    private long requestId; // если вещь создана по запросу, то тут будет ссылка на номер этого запроса
}
