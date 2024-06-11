package ru.practicum.shareit.user.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserDto {
    @EqualsAndHashCode.Exclude
    @Positive
    private Long id;
    private String name;
    @NotBlank
    @Email(message = "электронная почта не может быть пустой и должна содержать символ @")
    private String email;
}
