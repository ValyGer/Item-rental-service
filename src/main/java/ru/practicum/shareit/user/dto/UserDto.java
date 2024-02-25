package ru.practicum.shareit.user.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Validated
@NoArgsConstructor
public class UserDto {
    @EqualsAndHashCode.Exclude
    private long id;
    private String name;
    @NotBlank
    @Email(message = "электронная почта не может быть пустой и должна содержать символ @")
    private String email;

    public UserDto(String userName, String email) {
        this.name = userName;
        this.email = email;
    }

    public UserDto(Long userId, String userName, String email) {
        this.id = userId;
        this.name = userName;
        this.email = email;
    }
}
