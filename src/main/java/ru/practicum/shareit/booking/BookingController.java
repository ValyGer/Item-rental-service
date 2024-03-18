package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoWithItem;
import ru.practicum.shareit.booking.dto.BookingDtoWithItemMapper;
import ru.practicum.shareit.booking.dto.BookingMapper;

import javax.validation.Valid;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    private final BookingMapper bookingMapper;
    private final BookingDtoWithItemMapper bookingDtoWithItemMapper;

    @PostMapping // Создание нового
    public ResponseEntity<BookingDtoWithItem> createBooking(@RequestHeader("X-Sharer-User-Id") long bookerId,
                                                            @Valid @RequestBody BookingDto bookingDto) {
        bookingDto.setBookerId(bookerId);
        Booking booking = bookingMapper.toBooking(bookingDto);
        return ResponseEntity.ok().body(bookingDtoWithItemMapper.toBookingDtoWithItem(bookingService
                .createBooking(booking)));
    }

    @PatchMapping("/{bookingId}") // Подтверждение или отклонении бронирования
    public ResponseEntity<BookingDtoWithItem> setApprovedByOwner(@RequestHeader("X-Sharer-User-Id") long userId,
                                                         @PathVariable long bookingId,
                                                         @RequestParam Boolean approved) {
        return ResponseEntity.ok().body(bookingDtoWithItemMapper.toBookingDtoWithItem(bookingService
                .setApprovedByOwner(userId, bookingId, approved)));
    }

    @GetMapping("/{bookingId}") // Получение информации о бронировании
    public ResponseEntity<BookingDtoWithItem> getBookingById(@RequestHeader("X-Sharer-User-Id") long userId,
                                                     @PathVariable long bookingId) {
        return ResponseEntity.ok().body(bookingDtoWithItemMapper.toBookingDtoWithItem(bookingService
                .getBookingById(userId, bookingId)));
    }
}
