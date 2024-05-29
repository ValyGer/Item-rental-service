package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.exceptions.ConflictException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.impl.UserServiceImpl;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Test
    void createUserSuccessfully () {
        User user = new User(1L, "Name", "user@mail.ru");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User userSaved = userService.createUser(user);

        assertThat(userSaved.getId(), equalTo(user.getId()));
        assertThat(userSaved.getUserName(), equalTo((user.getUserName())));
        assertThat(userSaved.getEmail(), equalTo(user.getEmail()));
    }

    @Test
    void errorWhenCreatingUserWithEmailAlreadyExists() {
        User user = new User(1L, "Name", "user@mail.ru");
        when(userRepository.save(any(User.class))).thenThrow(ConflictException.class);

        assertThrows(ConflictException.class,
                () -> userService.createUser(user));
    }

    @Test
    void getUserById_whenUserFound_thenReturnUser() {
        User user = new User(1L, "Name", "user@mail.ru");
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        User savedUser = userService.getUserById(user.getId());

        assertEquals(user, savedUser);
    }

    @Test
    void getUserById_whenUserNotFound_thenReturnThrow() {
        User user = new User(1L, "Name", "user@mail.ru");
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> userService.getUserById(user.getId()));
    }

    @Test
    void updateUser() {
    }

    @Test
    void getAllUsers() {
    }



    @Test
    void deleteUser() {
    }
}