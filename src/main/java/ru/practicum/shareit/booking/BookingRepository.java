package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    // ---Для обработки getAllBookingByUser
    // Получение списка всех бронирований текущего пользователя

    // Возвращает список всех бронирований пользователя отсортированных по времени
    List<Booking> findBookingsByBooker_IdOrderByStartDesc(long userId);

    @Query("select b " +
            "from Booking b " +
            "where b.booker = ?1 and b.start < ?2 and b.end > ?3 " +
            "order by b.start DESC")
    List<Booking> findAllBookingsForBookerWithStartAndEnd(long id, LocalDateTime now, LocalDateTime now1);

    List<Booking> findAllByBooker_IdAndEndIsBeforeOrderByStartDesc(long id, LocalDateTime now);

    List<Booking> findAllByBooker_IdAndStartIsAfterOrderByStartDesc(long id, LocalDateTime now);

    List<Booking> findAllByBooker_IdAndStatusEqualsOrderByStartDesc(long id, State state);


    // ---Для обработки getAllBookingByOwner
    // Получаем список бронирований отсортированных по времени
    List<Booking> findAllByItem_OwnerOrderByStartDesc(long userId);

    // Возвращает список бронирования вещей которые имеются на данный момент время отсортированных по времени
    List<Booking> findAllByItem_OwnerAndStartBeforeAndEndAfterOrderByStartDesc(long userId, LocalDateTime nowDateTime, LocalDateTime nowDateTime1);

    // Возвращает список завершенных бронирований до указанной даты отсортированных по времени
    @Query("select b " +
            "from Booking b " +
            "where b.item.owner = ?1 and b.end < ?2 " +
            "order by b.start desc")
    List<Booking> findAllByItem_OwnerAndEndIsBeforeOrderByStartDesc(long userId, LocalDateTime nowDateTime);

    // Получение списка бронирований после определенной даты
    List<Booking> findAllByItem_OwnerAndStartIsAfterOrderByStartDesc(long id, LocalDateTime now);

    // Возвращает список бронирований пользователя определенного статуса
    List<Booking> findAllByItem_OwnerAndStatusEqualsOrderByStartDesc(long userId, State state);

    // Возвращает список вещей пользователя
    @Query("select b " +
            "from Booking b " +
            "where b.item.owner = ?1 and b.end < ?2 " +
            "order by b.start desc")
    List<Booking> findAllByItemOrderByStartDesc(long itemId);
}
