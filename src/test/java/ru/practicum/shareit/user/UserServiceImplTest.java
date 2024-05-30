package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
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

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Test
    void createUser_whenUserCreated_thenReturnUser() {
        User user = new User(1L, "Name", "user@mail.ru");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User userSaved = userService.createUser(user);

        assertThat(userSaved.getId(), equalTo(user.getId()));
        assertThat(userSaved.getUserName(), equalTo((user.getUserName())));
        assertThat(userSaved.getEmail(), equalTo(user.getEmail()));
    }

    @Test
    void createUser_whenUserNotCreated_thenReturnThrow() {
        User user = new User();
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
    void updateUser_whenUserUpdeted_thenReturnUser() {
        Long userId = 0L;
        User oldUser = new User();
        oldUser.setUserName("Name1");
        oldUser.setEmail("user1@mail.ru");

        User newUser = new User();
        newUser.setUserName("Name2");
        newUser.setEmail("user2@mail.ru");
        when(userRepository.findById(userId)).thenReturn(Optional.of(oldUser));

        User actualUser = userService.updateUser(userId,newUser);

        verify(userRepository).save(userArgumentCaptor.capture());
        User savedUser = userArgumentCaptor.getValue();

        assertEquals("Name2", savedUser.getUserName());
        assertEquals("user2@mail.ru", savedUser.getEmail());
    }

    @Test
    void getAllUsers() {
    }

    @Test
    void deleteUser() {
    }
}