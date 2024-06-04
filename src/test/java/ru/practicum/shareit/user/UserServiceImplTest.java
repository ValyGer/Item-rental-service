package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import ru.practicum.shareit.exceptions.ConflictException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.impl.UserServiceImpl;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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
    void updateUser_whenUserUpdated_thenReturnUser() {
        Long userId = 0L;
        User oldUser = new User();
        oldUser.setUserName("Name1");
        oldUser.setEmail("user1@mail.ru");

        User newUser = new User();
        newUser.setUserName("Name2");
        newUser.setEmail("user2@mail.ru");
        when(userRepository.findById(userId)).thenReturn(Optional.of(oldUser));

        userService.updateUser(userId, newUser);

        verify(userRepository).save(userArgumentCaptor.capture());
        User savedUser = userArgumentCaptor.getValue();

        assertEquals("Name2", savedUser.getUserName());
        assertEquals("user2@mail.ru", savedUser.getEmail());
    }

    @Test
    void updateUser_whenUserNotUpdated_thenReturnThrow() {
        Long userId = 0L;
        User oldUser = new User();
        oldUser.setUserName("Name1");
        oldUser.setEmail("user1@mail.ru");

        User newUser = new User();
        newUser.setUserName("Name2");
        newUser.setEmail("user1@mail.ru");
        when(userRepository.findById(userId)).thenReturn(Optional.of(oldUser));
        when(userRepository.save(any(User.class))).thenThrow(ConflictException.class);

        assertThrows(ConflictException.class,
                () -> userService.updateUser(userId, newUser));
    }

    @Test
    void getAllUsers_thenReturnAllUsers() {
        List<User> allUsers = new ArrayList<>();
        allUsers.add(new User(1L, "Name1", "user1@mail.ru"));
        allUsers.add(new User(2L, "Name2", "user2@mail.ru"));

        when(userRepository.findAll()).thenReturn(allUsers);

        List<User> savedAllUsers = userService.getAllUsers();

        assertEquals(savedAllUsers.size(), allUsers.size());
        assertEquals(savedAllUsers.get(0).getUserName(), "Name1");
        assertEquals(savedAllUsers.get(0).getEmail(), "user1@mail.ru");
        assertEquals(savedAllUsers.get(1).getUserName(), "Name2");
        assertEquals(savedAllUsers.get(1).getEmail(), "user2@mail.ru");
    }

    @Test
    void deleteUser_whenUserExists_thenDeletedUser() {
        Long userId = 0L;
        User user = new User();
        user.setUserName("Name1");
        user.setEmail("user1@mail.ru");

        HttpStatus httpStatus = userService.deleteUser(userId);

        assertEquals(HttpStatus.OK, httpStatus);
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void deleteUser_whenUserNotExists_thenReturnThrow() {
        Long userId = 0L;
        User user = new User();
        user.setUserName("Name1");
        user.setEmail("user1@mail.ru");

        doThrow(new RuntimeException())
                .when(userRepository).deleteById(userId);

        assertThrows(RuntimeException.class,
                () -> userService.deleteUser(userId));
    }
}