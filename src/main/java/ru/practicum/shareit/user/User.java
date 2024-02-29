package ru.practicum.shareit.user;

import lombok.*;

/**
 * TODO Sprint add-controllers.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class User {
    private long userId;
    private String userName;
    private String email;

    public User(String userName, String email) {
        this.userName = userName;
        this.email = email;
    }

    public User(long userId, String userName, String email) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
    }
}
