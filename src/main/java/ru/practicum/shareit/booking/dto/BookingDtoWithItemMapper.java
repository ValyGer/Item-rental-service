package ru.practicum.shareit.booking.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDtoForBooking;
import ru.practicum.shareit.item.model.Item;

@Mapper(componentModel = "spring")
public interface BookingDtoWithItemMapper {

    @Mapping(source = "bookingId", target = "id")
    @Mapping(source = "item", target = "item", qualifiedBy = ItemToItemDto.class)
    @Mapping(source = "booker.id", target = "booker.id")
    BookingDtoWithItem toBookingDtoWithItem(Booking booking);


    @Mapping(source = "bookingId", target = "id")
    @Mapping(source = "item", target = "item", qualifiedBy = ItemToItemDto.class)
    @Mapping(source = "booker.id", target = "booker.id")
    BookingDtoWithItem toBookingDtoWithItemNotTime(Booking booking);

    @ItemToItemDto
    static ItemDtoForBooking itemToItemDto(Item item) {
        ItemDtoForBooking itemDtoForBooking = new ItemDtoForBooking();
        itemDtoForBooking.setId(item.getItemId());
        itemDtoForBooking.setName(item.getName());
        return itemDtoForBooking;
    }
}