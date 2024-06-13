package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDtoForBooking;
import ru.practicum.shareit.item.dto.ItemDtoForBookingMapperImpl;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class ItemDtoForBookingMapperTest {

    @InjectMocks
    ItemDtoForBookingMapperImpl itemDtoForBookingMapper;

    @Test
    void toItemDtoForBooking() {
        LocalDateTime time = LocalDateTime.now();
        List<Booking> bookings = new ArrayList<>();
        User booker = new User(2L, "name", "email@mail.ru");
        Item item = new Item(1L, "telephone", "For call your friends", new User(), true, new ItemRequest());
        bookings.add(new Booking(1L, item, booker, time.plusMinutes(-20), time.plusMinutes(20)));

        ItemDtoForBooking itemDtoForBooking = itemDtoForBookingMapper.toItemDtoForBooking(item);

        assertEquals(item.getItemId(), itemDtoForBooking.getId());
        assertEquals(item.getName(), itemDtoForBooking.getName());
    }

    @Test
    void toItemDtoForBookingWhenNull() {
        Item item = null;

        ItemDtoForBooking itemDtoForBooking = itemDtoForBookingMapper.toItemDtoForBooking(item);

        assertNull(itemDtoForBooking);
    }
}