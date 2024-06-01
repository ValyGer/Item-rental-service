package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingLastNextDtoMapper;
import ru.practicum.shareit.exceptions.ConflictException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDtoForBookingAndCommentsMapper;
import ru.practicum.shareit.item.impl.ItemServiceImpl;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @InjectMocks
    private ItemServiceImpl itemService;

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private UserService userService;
    @Mock
    private BookingService bookingService;
    @Mock
    private ItemDtoForBookingAndCommentsMapper itemDtoForBookingAndCommentsMapper;
    @Mock
    private BookingLastNextDtoMapper bookingLastNextDtoMapper;
    @Mock
    private CommentMapper commentMapper;

    @Captor
    private ArgumentCaptor<Item> itemArgumentCaptor;


    @Test
    void createItem_whenCreate_responseStatusOk() {
        User user = new User();
        Item item = new Item();
        when(userService.getUserById(any(Long.class))).thenReturn(user);
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        Item itemSaved = itemService.createItem(user.getId(), item);

        assertThat(itemSaved, equalTo(item));
        verify(itemRepository, times(1)).save(item);
    }

    @Test
    void createItem_whenCreate_responseThrow() {
        User user = new User();
        Item item = new Item();

        when(userService.getUserById(any(Long.class))).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class,
                () -> itemService.createItem(user.getId(), item));
    }

    @Test
    void updateItem_whenUpdateIsGood_responseStatusOk() {
        Long userId = 0L;
        Long itemId = 0L;
        User user = new User();
        Item oldItem = new Item("name", "description", true);
        Item newItem = new Item ("New Name", "New description", true);

        when(itemRepository.getReferenceById(userId)).thenReturn(oldItem);
        when(userService.getUserById(itemId)).thenReturn(user);

        itemService.updateItem(user.getId(), itemId, newItem);

        verify(itemRepository).save(itemArgumentCaptor.capture());
        Item savedItem = itemArgumentCaptor.getValue();

        assertEquals("New Name", savedItem.getName());
        assertEquals("New description", savedItem.getDescription());
    }

    @Test
    void getItemsById() {
    }

    @Test
    void getAllItemsUser() {
    }

    @Test
    void searchAvailableItems() {
    }

    @Test
    void getItemWithBooker() {
    }

    @Test
    void addComment() {
    }
}