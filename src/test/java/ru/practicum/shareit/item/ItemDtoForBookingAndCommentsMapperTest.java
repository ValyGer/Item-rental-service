package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDtoForBookingAndComments;
import ru.practicum.shareit.item.dto.ItemDtoForBookingAndCommentsMapperImpl;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ItemDtoForBookingAndCommentsMapperTest {

    @InjectMocks
    ItemDtoForBookingAndCommentsMapperImpl itemDtoForBookingAndCommentsMapper;

    @Test
    void toItemDtoForBookingAndComments() {
        LocalDateTime time = LocalDateTime.now();
        List<Comment> comments = new ArrayList<>();
        comments.add(new Comment("text"));
        List<Booking> bookings = new ArrayList<>();
        User booker = new User(2L, "name", "email@mail.ru");
        Item item = new Item(1L, "telephone", "For call your friends", 1L, true, 2L);
        bookings.add(new Booking(1L, item, booker, time.plusMinutes(-20), time.plusMinutes(20)));
        item.setBookings(bookings);
        item.setComments(comments);

        ItemDtoForBookingAndComments itemDtoForBookingAndComments = itemDtoForBookingAndCommentsMapper
                .toItemDtoForBookingAndComments(item);

        assertEquals(item.getItemId(), itemDtoForBookingAndComments.getId());
        assertEquals(item.getName(), itemDtoForBookingAndComments.getName());
        assertEquals(item.getDescription(), itemDtoForBookingAndComments.getDescription());
        assertEquals(item.getIsAvailable(), itemDtoForBookingAndComments.getAvailable());
        assertEquals(item.getComments().size(), itemDtoForBookingAndComments.getComments().size());
    }
}