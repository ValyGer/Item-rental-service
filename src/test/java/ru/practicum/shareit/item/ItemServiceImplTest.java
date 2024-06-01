package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingLastNextDtoMapper;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDtoForBookingAndCommentsMapper;
import ru.practicum.shareit.item.impl.ItemServiceImpl;
import ru.practicum.shareit.user.UserService;

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


    @Test
    void createItem() {
    }

    @Test
    void updateItem() {
    }

    @Test
    void getAllItemsUser() {
    }

    @Test
    void getItemsById() {
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