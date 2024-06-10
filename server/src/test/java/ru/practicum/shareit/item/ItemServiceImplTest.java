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
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.impl.ItemServiceImpl;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
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

    @Mock
    private ItemMapper itemMapper;

    @Captor
    private ArgumentCaptor<Item> itemArgumentCaptor;


    @Test
    void createItem_whenCreate_responseStatusOk() {
        User user = new User();
        Item item = new Item("name", "description", new User(), true, new ItemRequest());
        when(itemMapper.toItem(any(ItemDto.class))).thenReturn(item);
        when(userService.getUserById(any(Long.class))).thenReturn(user);
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        Item itemSaved = itemService.createItem(user.getId(), new ItemDto());

        assertThat(itemSaved, equalTo(item));
        verify(itemRepository, times(1)).save(item);
    }

    @Test
    void createItem_whenCreate_responseThrow() {
        User user = new User();
        Item item = new Item();
        when(itemMapper.toItem(any(ItemDto.class))).thenReturn(item);
        when(userService.getUserById(any(Long.class))).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class,
                () -> itemService.createItem(user.getId(), new ItemDto()));
    }

    @Test
    void updateItem_whenUpdateIsGood_responseStatusOk() {
        Long userId = 0L;
        Long itemId = 0L;
        User user = new User();
        Item oldItem = new Item("name", "description", user, true);
        ItemDto newItemDto = new ItemDto();
        Item newItem = new Item("New Name", "New description", new User(), true);
        when(itemMapper.toItem(any(ItemDto.class))).thenReturn(newItem);
        when(itemRepository.getReferenceById(itemId)).thenReturn(oldItem);
        when(userService.getUserById(userId)).thenReturn(user);

        itemService.updateItem(user.getId(), itemId, newItemDto);

        verify(itemRepository).save(itemArgumentCaptor.capture());
        Item savedItem = itemArgumentCaptor.getValue();

        assertEquals("New Name", savedItem.getName());
        assertEquals("New description", savedItem.getDescription());
    }

    @Test
    void updateItem_whenUpdateIsGood_responseThrow() {
        Long userId = 0L;
        Long itemId = 0L;
        User user = new User();
        ItemDto itemDto = new ItemDto("name", "description", true, 1L);
        Item oldItem = new Item("name", "description", user, true);

        when(itemMapper.toItem(any(ItemDto.class))).thenReturn(oldItem);
        when(userService.getUserById(userId)).thenReturn(user);
        when(itemRepository.getReferenceById(itemId)).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> itemService.updateItem(user.getId(), itemId, itemDto));
    }

    @Test
    void getItemsById_whenItemFound_thenReturnItem() {
        Long itemId = 0L;
        Item item = new Item();
        when(itemRepository.findById(any(Long.class))).thenReturn(Optional.of(item));

        Item savedItem = itemService.getItemsById(itemId);
        assertEquals(item, savedItem);
    }

    @Test
    void getItemsById_whenItemNotFound_thenReturnThrow() {
        Long itemId = 0L;
        when(itemRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemService.getItemsById(itemId));
    }

    @Test
    void getAllItemsUser_whenAllItemsFound_thenReturnListOfItem() {
        Long userId = 0L;
        when(userService.getUserById(any(Long.class))).thenReturn(new User());
        when(itemRepository.findItemsByOwnerIdOrderByItemIdAsc(any(Long.class))).thenReturn(new ArrayList<>());

        itemService.getAllItemsUser(userId);

        verify(itemRepository, times(1)).findItemsByOwnerIdOrderByItemIdAsc(userId);
        verify(commentRepository, atMostOnce()).findAllByItemOrderByItem(any(Item.class));
    }

    @Test
    void getAllItemsUser_whenAllItemsFoundWithComments_thenReturnListOfItem() {
        Long userId = 0L;
        List<Item> items = new ArrayList<>();
        items.add(new Item("Name of Item", "Description of Item", new User(), true, null));

        List<Comment> comments = new ArrayList<>();
        comments.add(new Comment("text"));
        when(userService.getUserById(any(Long.class))).thenReturn(new User());
        when(itemRepository.findItemsByOwnerIdOrderByItemIdAsc(any(Long.class))).thenReturn(items);
        when(commentRepository.findAllByItemOrderByItem(any(Item.class))).thenReturn(comments);
        when(itemDtoForBookingAndCommentsMapper.toItemDtoForBookingAndComments(any(Item.class))).thenReturn(new ItemDtoForBookingAndComments());

        itemService.getAllItemsUser(userId);

        verify(itemRepository, times(1)).findItemsByOwnerIdOrderByItemIdAsc(userId);
        verify(commentRepository, atMostOnce()).findAllByItemOrderByItem(any(Item.class));
        verify(commentMapper, times(1)).toCommentDto(any(Comment.class));
    }

    @Test
    void getAllItemsUser_whenUserNotFound_thenReturnToThrow() {
        Long userId = 0L;
        when(userService.getUserById(any(Long.class))).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class,
                () -> itemService.getAllItemsUser(any(Long.class)));

        verify(itemRepository, never()).findItemsByOwnerIdOrderByItemIdAsc(userId);
        verify(commentRepository, never()).findAllByItemOrderByItem(any(Item.class));
    }

    @Test
    void searchAvailableItemsTestWhenTextIsEmpty() {
        String text = " ";
        List<Item> listOfItems = new ArrayList<>();

        List<Item> savedOfItems = itemService.searchAvailableItems(text);

        verify(itemRepository, never()).searchAvailableItems(text);
        assertThat(listOfItems, equalTo(savedOfItems));
    }

    @Test
    void searchAvailableItemsTestWhenTestHasAnySize() {
        String text = "abc";
        List<Item> listOfItems = new ArrayList<>();
        when(itemRepository.searchAvailableItems(any(String.class))).thenReturn(listOfItems);

        List<Item> savedOfItems = itemService.searchAvailableItems(text);

        verify(itemRepository, times(1)).searchAvailableItems(text);
        assertThat(listOfItems, equalTo(savedOfItems));
    }

    @Test
    void getItemWithBooker_whenItemsFound_thenReturnListOfItemWithBooker() {
        Long itemId = 0L;
        Long userId = 0L;
        Item item = new Item("name", "description", new User(), true, new ItemRequest());
        List<Comment> commentsAboutItem = new ArrayList<>();
        ItemDtoForBookingAndComments itemFromBd = new ItemDtoForBookingAndComments();

        when(itemRepository.findById(any(Long.class))).thenReturn(Optional.of(item));
        when(commentRepository.findAllByItemOrderByItem(any(Item.class))).thenReturn(commentsAboutItem);
        when(itemDtoForBookingAndCommentsMapper.toItemDtoForBookingAndComments(any(Item.class))).thenReturn(itemFromBd);

        itemService.getItemWithBooker(itemId, userId);

        verify(itemRepository, times(1)).findById(userId);
        verify(commentRepository, atMostOnce()).findAllByItemOrderByItem(any(Item.class));
    }

    @Test
    void getItemWithBooker_whenItemsFoundWithComments_thenReturnListOfItemWithBooker() {
        Long itemId = 0L;
        Long userId = 0L;
        Item item = new Item("name", "description", new User(), true, new ItemRequest());
        List<Comment> commentsAboutItem = new ArrayList<>();
        commentsAboutItem.add(new Comment("text"));

        ItemDtoForBookingAndComments itemFromBd = new ItemDtoForBookingAndComments();

        when(itemRepository.findById(any(Long.class))).thenReturn(Optional.of(item));
        when(commentRepository.findAllByItemOrderByItem(any(Item.class))).thenReturn(commentsAboutItem);
        when(itemDtoForBookingAndCommentsMapper.toItemDtoForBookingAndComments(any(Item.class))).thenReturn(itemFromBd);

        itemService.getItemWithBooker(itemId, userId);

        verify(itemRepository, times(1)).findById(userId);
        verify(commentRepository, atMostOnce()).findAllByItemOrderByItem(any(Item.class));
        verify(commentMapper, times(1)).toCommentDto(any(Comment.class));
    }

    @Test
    void getItemWithBooker_whenItemsNotFound_thenReturnThrow() {
        Long itemId = 0L;
        Long userId = 0L;

        assertThrows(NotFoundException.class,
                () -> itemService.getItemWithBooker(itemId, userId));

        verify(itemRepository, never()).findItemsByOwnerIdOrderByItemIdAsc(userId);
        verify(commentRepository, never()).findAllByItemOrderByItem(any(Item.class));
    }

    @Test
    void addComment_whenAddCommentSuccess_thenResponse() {
        Long userId = 0L;
        Long itemId = 0L;
        LocalDateTime time = LocalDateTime.now();
        User user = new User(1L, "Name1", "user1@mail.ru");
        User owner = new User(2L, "Name2", "user2@mail.ru");
        Item item = new Item("name", "description", owner, true, new ItemRequest());
        Comment comment = new Comment();
        Booking booking = new Booking();
        List<Booking> bookingOfItem = new ArrayList<>();
        bookingOfItem.add(booking);
        when(commentMapper.toComment(any(CommentDto.class))).thenReturn(comment);
        when(itemRepository.findById(any(Long.class))).thenReturn(Optional.of(item));
        when(userService.getUserById(any(Long.class))).thenReturn(user);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        when(bookingService.getAllBookingForItemByUser(any(Item.class), any(User.class), any(LocalDateTime.class)))
                .thenReturn(bookingOfItem);

        Comment savedComment = itemService.addComment(userId, itemId, new CommentDto());

        assertThat(savedComment, equalTo(comment));
        verify(itemRepository, times(1)).findById(userId);
        verify(commentRepository, times(1)).save(comment);
    }

    @Test
    void addComment_thenResponseThrowNotFound() {
        Long userId = 0L;
        Long itemId = 0L;
        Comment comment = new Comment();
        Item item = new Item();
        User user = new User();
        List<Booking> booking = new ArrayList<>();
        when(itemRepository.findById(any(Long.class))).thenReturn(Optional.of(item));
        when(userService.getUserById(any(Long.class))).thenReturn(user);
        when(bookingService.getAllBookingForItemByUser(any(Item.class), any(User.class), any(LocalDateTime.class)))
                .thenReturn(booking);

        assertThrows(ValidationException.class,
                () -> itemService.addComment(userId, itemId, new CommentDto()));

        verify(itemRepository, times(1)).findById(userId);
        verify(commentRepository, never()).save(comment);
    }

    @Test
    void addComment_thenResponseThrowValid() {
        Long userId = 0L;
        Long itemId = 0L;
        Comment comment = new Comment();
        when(itemRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> itemService.addComment(userId, itemId, new CommentDto()));

        verify(itemRepository, times(1)).findById(userId);
        verify(commentRepository, never()).save(comment);
    }
}