package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoWithItem;
import ru.practicum.shareit.booking.dto.BookingDtoWithItemMapper;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    private final BookingDtoWithItemMapper bookingDtoWithItemMapper;

    @PostMapping // Создание нового
    public ResponseEntity<BookingDtoWithItem> createBooking(@RequestHeader("X-Sharer-User-Id") long bookerId,
                                                            @Valid @RequestBody BookingDto bookingDto) {
        return ResponseEntity.ok().body(bookingDtoWithItemMapper.toBookingDtoWithItem(bookingService
                .createBooking(bookerId, bookingDto)));
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

    @GetMapping // Получение списка бронирований для данного пользователя с учетом статуса и даты
    public ResponseEntity<List<BookingDtoWithItem>> getAllBookingByUser(@RequestHeader("X-Sharer-User-Id") long userId,
                                                                        @RequestParam(value = "state", defaultValue = "ALL",
                                                                                required = false) String state,
                                                                        @RequestParam(required = false, defaultValue = "0") Integer from,
                                                                        @RequestParam(required = false, defaultValue = "20") Integer size) {
        return ResponseEntity.ok().body(bookingService.getAllBookingByUser(from, size, userId, state));
    }

    @GetMapping("/owner") // Получение списка бронирований для всех вещей текущего пользователя с учетом статуса и даты
    public ResponseEntity<List<BookingDtoWithItem>> getAllBookingByOwner(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                                                         @RequestParam(value = "state", defaultValue = "ALL",
                                                                                 required = false) String state,
                                                                         @RequestParam(required = false, defaultValue = "0") Integer from,
                                                                         @RequestParam(required = false, defaultValue = "20") Integer size) {
        return ResponseEntity.ok().body(bookingService.getAllBookingByOwner(from, size, ownerId, state).stream()
                .map(bookingDtoWithItemMapper::toBookingDtoWithItem)
                .collect(Collectors.toList()));
    }
}
