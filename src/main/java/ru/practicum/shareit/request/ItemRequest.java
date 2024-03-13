package ru.practicum.shareit.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    @Column(name = "requester_id")
    private Long requesterId; // пользователь, который создал запроса
    private LocalDateTime create; // дата и время создания запроса
}
