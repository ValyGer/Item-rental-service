package ru.practicum.shareit.user.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserDto {
    @EqualsAndHashCode.Exclude
    private Long id;
    private String name;
    private String email;

    public UserDto(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public UserDto(Long userId, String userName, String email) {
        this.id = userId;
        this.name = userName;
        this.email = email;
    }
}
