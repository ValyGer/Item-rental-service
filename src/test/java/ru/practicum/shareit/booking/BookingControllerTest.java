package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoWithItem;
import ru.practicum.shareit.booking.dto.BookingDtoWithItemMapper;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.ItemDtoForBooking;
import ru.practicum.shareit.user.dto.UserDtoForBooking;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @MockBean
    private BookingMapper bookingMapper;

    @MockBean
    private BookingDtoWithItemMapper bookingDtoWithItemMapper;

    private final LocalDateTime start = LocalDateTime.now().plusMinutes(1);
    private final LocalDateTime end = LocalDateTime.now().plusMinutes(60);

    private final UserDtoForBooking booker = new UserDtoForBooking(1L);
    private final ItemDtoForBooking item = new ItemDtoForBooking(1L, "NameOfItem");
    private final ItemDtoForBooking itemSecond = new ItemDtoForBooking(2L, "SecondItem");

    @SneakyThrows
    @Test
    void createBooking_whenBookingIsCreate_thenResponseStatusOk() {
        BookingDto bookingDto = new BookingDto(1L, 1L, start, end);
        BookingDtoWithItem bookingDtoWithItem = new BookingDtoWithItem(1L, item, booker, start, end, Status.WAITING);

        when(bookingDtoWithItemMapper.toBookingDtoWithItem(bookingService
                .createBooking(any())))
                .thenReturn(bookingDtoWithItem);

        String result = mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", booker.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookingDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(bookingDtoWithItem), result);
    }

    @SneakyThrows
    @Test
    void createBooking_whenUserIsNotValid_thenResponseBadRequest() {
        BookingDto bookingDto = new BookingDto(null, 1L, start, end);

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", booker.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookingDto)))
                .andExpect(status().is(400));

        verify(bookingService, never()).createBooking(bookingMapper.toBooking(bookingDto));
    }

    @SneakyThrows
    @Test
    void getBookingById_whenResponseStatusOk() {
        Long bookingId = 1L;
        mockMvc.perform(get("/bookings/{bookingId}", bookingId)
                        .header("X-Sharer-User-Id", booker.getId()))
                .andExpect(status().isOk());

        verify(bookingService).getBookingById(booker.getId(), bookingId);
    }

    @SneakyThrows
    @Test
    void getBookingById_whenNotFound_thenResponseStatusBadRequest() {
        Long bookingId = 999L;

        when(bookingService.getBookingById(booker.getId(), bookingId))
                .thenThrow(NotFoundException.class);

        mockMvc.perform(get("/bookings/{bookingId}", bookingId)
                        .header("X-Sharer-User-Id", booker.getId())
                        .content(objectMapper.writeValueAsString(bookingId))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @SneakyThrows
    @Test
    void setApprovedByOwner_whenApprovedSuccess_thenResponseStatusOk() {
        Long bookingId = 1L;
        Boolean approved = true;

        BookingDtoWithItem bookingDtoWithItem = new BookingDtoWithItem(1L, item, booker, start, end, Status.WAITING);

        when(bookingDtoWithItemMapper.toBookingDtoWithItem(bookingService
                .setApprovedByOwner(booker.getId(), bookingId, approved))).thenReturn(bookingDtoWithItem);

        String result = mockMvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .header("X-Sharer-User-Id", booker.getId())
                        .param("approved", "true")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookingDtoWithItem)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(bookingDtoWithItem), result);
    }

    @SneakyThrows
    @Test
    void getAllBookingByUser_ListOfBookingSendSuccess_thenResponseStatusOk() {
        String state = State.ALL.toString();
        Integer from = 0;
        Integer size = 10;

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", booker.getId())
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "10")
                        .contentType("application/json"))
                .andExpect(status().isOk());

        verify(bookingService).getAllBookingByUser(from, size, booker.getId(), state);
    }

    @SneakyThrows
    @Test
    void getAllBookingByUser_ListOfBookingSendSuccess_thenResponseList() {
        Long bookingId = 1L;
        String state = State.ALL.toString();
        Integer from = 0;
        Integer size = 10;

        BookingDtoWithItem bookingDtoWithItem1 = new BookingDtoWithItem(1L, item, booker, start, end, Status.WAITING);
        BookingDtoWithItem bookingDtoWithItem2 = new BookingDtoWithItem(2L, itemSecond, booker, start, end, Status.APPROVED);
        List<BookingDtoWithItem> allBookingOfUser = Arrays.asList(bookingDtoWithItem1, bookingDtoWithItem2);

        when(bookingService.getAllBookingByUser(from, size, booker.getId(), state)).thenReturn(allBookingOfUser);

        String result = mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", booker.getId())
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "10")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(allBookingOfUser), result);
    }

    @SneakyThrows
    @Test
    void getAllBookingByUser_thenBadRequest() {
        Long bookingId = 1L;
        String state = State.ALL.toString();
        Integer from = -1;
        Integer size = 10;

        when(bookingService.getAllBookingByUser(from, size, bookingId, state)).thenThrow(ValidationException.class);

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", booker.getId())
                        .param("state", "ALL")
                        .param("from", "-1")
                        .param("size", "10")
                        .contentType("application/json"))
                .andExpect(status().is(400));

        verify(bookingService).getAllBookingByUser(from, size, booker.getId(), state);
    }

    @SneakyThrows
    @Test
    void getAllBookingByOwner_whenSandedResponseОк() {
        Long bookingId = 1L;
        String state = State.ALL.toString();
        Integer from = 0;
        Integer size = 10;

        UserDtoForBooking owner = new UserDtoForBooking(1L);

        BookingDtoWithItem bookingDtoWithItem1 = new BookingDtoWithItem(1L, item, booker, start, end, Status.WAITING);
        BookingDtoWithItem bookingDtoWithItem2 = new BookingDtoWithItem(2L, itemSecond, booker, start, end, Status.APPROVED);
        List<BookingDtoWithItem> allBookingOfUser = Arrays.asList(bookingDtoWithItem1, bookingDtoWithItem2);

        when(bookingService.getAllBookingByUser(from, size, bookingId, state)).thenReturn(allBookingOfUser);

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", owner.getId())
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "10")
                        .contentType("application/json"))
                .andExpect(status().isOk());

        verify(bookingService).getAllBookingByOwner(from, size, owner.getId(), state);
    }

    @SneakyThrows
    @Test
    void getAllBookingByOwner_thenBadRequest() {
        Long bookingId = 1L;
        String state = State.ALL.toString();
        Integer from = -1;
        Integer size = 10;
        UserDtoForBooking owner = new UserDtoForBooking(1L);

        when(bookingService.getAllBookingByOwner(from, size, bookingId, state)).thenThrow(ValidationException.class);

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", owner.getId())
                        .param("state", "ALL")
                        .param("from", "-1")
                        .param("size", "10")
                        .contentType("application/json"))
                .andExpect(status().is(400));

        verify(bookingService).getAllBookingByOwner(from, size, owner.getId(), state);
    }
}