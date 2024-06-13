package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingClient bookingClient;

    @PostMapping // Создание нового
    public ResponseEntity<Object> createBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                                @Valid @RequestBody BookingDto bookingDto) {
        return bookingClient.createBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}") // Подтверждение или отклонении бронирования
    public ResponseEntity<Object> setApprovedByOwner(@RequestHeader("X-Sharer-User-Id") long userId,
                                                     @PathVariable long bookingId,
                                                     @RequestParam Boolean approved) {
        return bookingClient.setApprovedByOwner(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}") // Получение информации о бронировании
    public ResponseEntity<Object> getBookingById(@Positive @RequestHeader("X-Sharer-User-Id") long userId,
                                                 @PathVariable long bookingId) {
        return bookingClient.getBookingById(userId, bookingId);
    }

    @GetMapping // Получение списка бронирований для данного пользователя с учетом статуса и даты
    public ResponseEntity<Object> getAllBookingByUser(@Positive @RequestHeader("X-Sharer-User-Id") long userId,
                                                      @RequestParam(value = "state", defaultValue = "ALL",
                                                              required = false) String state,
                                                      @Positive @RequestParam(required = false, defaultValue = "0") Integer from,
                                                      @Positive @RequestParam(required = false, defaultValue = "20") Integer size) {
        return bookingClient.getAllBookingByUser(userId, state, from, size);
    }

    @GetMapping("/owner") // Получение списка бронирований для всех вещей текущего пользователя с учетом статуса и даты
    public ResponseEntity<Object> getAllBookingByOwner(@Positive @RequestHeader("X-Sharer-User-Id") long ownerId,
                                                       @RequestParam(value = "state", defaultValue = "ALL",
                                                               required = false) String state,
                                                       @Positive @RequestParam(required = false, defaultValue = "0") Integer from,
                                                       @Positive @RequestParam(required = false, defaultValue = "20") Integer size) {
        return bookingClient.getAllBookingByOwner(ownerId, state, from, size);
    }
}
