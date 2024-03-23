package ru.practicum.shareit.item.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingLastNextDto;
import ru.practicum.shareit.booking.dto.BookingLastNextDtoMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDtoForBookingAndComments;
import ru.practicum.shareit.item.dto.ItemDtoForBookingAndCommentsMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final ItemDtoForBookingAndCommentsMapper itemDtoForBookingAndCommentsMapper;
    private final BookingLastNextDtoMapper bookingLastNextDtoMapper;

    // Добавление вещи
    public Item createItem(Long userId, Item item) throws NotFoundException {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            log.info("Вещь успешно добавлена");
            item.setOwner(userId);
            return itemRepository.save(item);
        } else {
            log.info("Пользователь с Id = {} существует в базе", userId);
            throw new NotFoundException("Пользователь не найден");
        }
    }

    // Обновление вещи
    public Item updateItem(Long userId, Long itemId, Item item) throws NotFoundException {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            log.info("Пользователь с Id = {} существует в базе", userId);
            try {
                Item saved = itemRepository.getReferenceById(itemId);
                if (item.getName() != null) {
                    saved.setName(item.getName());
                }
                if (item.getDescription() != null) {
                    saved.setDescription(item.getDescription());
                }
                if (item.getIsAvailable() != null) {
                    saved.setIsAvailable(item.getIsAvailable());
                }
                log.info("Вещь с Id = {} обновлена", itemId);
                return itemRepository.save(saved);
            } catch (NotFoundException e) {
                log.info("Данной вещи с Id = {} нет у пользователя", itemId);
                throw new NotFoundException("Вещи с указанным Id не принадлежит данному пользователю");
            }
        } else {
            log.info("Пользователь с Id = {} не существует в базе", userId);
            throw new NotFoundException("Пользователь не найден");
        }
    }

    // Получение вещи пользователя
    @Transactional
    public List<ItemDtoForBookingAndComments> getAllItemsUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            log.info("Пользователь с Id = {} существует в базе", userId);

            List<Item> items = itemRepository.findItemsByOwner(userId);
            List<ItemDtoForBookingAndComments> allItemByUser = new ArrayList<>();
            LocalDateTime now = LocalDateTime.now();
            for (Item item : items) {
                ItemDtoForBookingAndComments itemFromBd = itemDtoForBookingAndCommentsMapper
                        .toItemDtoForBookingAndComments(item);
                List<Booking> allBookingForItem = bookingRepository.findAllByItemOrderByStartDesc(item);
                BookingLastNextDto lastBooking;
                BookingLastNextDto nextBooking;
                if (allBookingForItem != null) {
                    lastBooking = getLastBooking(allBookingForItem, now);
                    nextBooking = getNextBooking(allBookingForItem, now);
                    itemFromBd.setLastBooking(lastBooking);
                    itemFromBd.setNextBooking(nextBooking);
                }
                allItemByUser.add(itemFromBd);
            }
            log.info("Список вещей успешно получен");
            return allItemByUser;
        } else {
            log.info("Пользователь с Id = {} отсутствует в баз", userId);
            throw new NotFoundException("Пользователя с указанным Id не существует");
        }
    }

    // Получение вещи по Id
    public Item getItemsById(Long userId, Long itemId) {
        Optional<Item> item = itemRepository.findById(itemId);
        if (item.isPresent()) {
            log.info("Получена вещь с Id = {}", itemId);
            return item.get();
        } else {
            log.info("Данной вещи с Id = {} нет в базе", itemId);
            throw new NotFoundException("Вещи с указанным Id не существует");
        }
    }

    // Получение списка доступных вещей по поиску
    @Transactional
    public List<Item> searchAvailableItems(String text) {
        if (text.isBlank()) {
            log.debug("Передан пустой запрос, возвращен пустой список");
            return new ArrayList<>();
        } else {
            log.debug("Вызван метод поиска доступной вещи");
            return itemRepository.searchAvailableItems(text);
        }
    }

    // Получение пользователем информации о датах следующего предыдущего бронирований вещи
    public ItemDtoForBookingAndComments getItemWithBooker(long itemId, long ownerId) {
        Optional<Item> item = itemRepository.findById(itemId);
        if (item.isPresent()) {
            Optional<User> user = userRepository.findById(ownerId);
            if (user.isPresent()) {
                if (item.get().getOwner() == ownerId) {
                    ItemDtoForBookingAndComments itemFromBd = itemDtoForBookingAndCommentsMapper
                            .toItemDtoForBookingAndComments(item.get());
                    List<Booking> allBookingForItem = bookingRepository.findAllByItemOrderByStartDesc(item.get());
                    BookingLastNextDto lastBooking;
                    BookingLastNextDto nextBooking;
                    if (allBookingForItem != null) {
                        LocalDateTime now = LocalDateTime.now();
                        lastBooking = getLastBooking(allBookingForItem, now);
                        nextBooking = getNextBooking(allBookingForItem, now);
                        itemFromBd.setLastBooking(lastBooking);
                        itemFromBd.setNextBooking(nextBooking);
                    }
                    return itemFromBd;
                } else {
                    log.info("Пользователь с Id = {} не является владельцев вещи, информация по бронированию не нужна", ownerId);
                    return itemDtoForBookingAndCommentsMapper
                            .toItemDtoForBookingAndComments(item.get());
                }
            } else {
                log.info("Пользователь с Id = {} не существует в базе", ownerId);
                throw new NotFoundException("Пользователь не найден");
            }
        } else {
            log.info("Данной вещи с Id = {} нет в базе", itemId);
            throw new NotFoundException("Вещи с указанным Id не существует");
        }
    }

    // Поиск следующего бронирования
    private BookingLastNextDto getNextBooking(List<Booking> allBookingForItem, LocalDateTime now) {
        Booking nextBooking = null;
        if (allBookingForItem != null && !allBookingForItem.isEmpty()) {
            for (Booking booking : allBookingForItem) {
                if (booking.getStart().isAfter(now)) {
                    if (nextBooking == null && (booking.getStatus().equals(Status.APPROVED)
                            || booking.getStatus().equals(Status.WAITING))) {
                        nextBooking = booking;
                    } else if (nextBooking == null) {
                        nextBooking = booking;
                    } else if (booking.getStart().isBefore(nextBooking.getStart())) {
                        nextBooking = booking;
                    }
                }
            }
        }
        return bookingLastNextDtoMapper.toBookingLastNextDto(nextBooking);
    }

    // Поиск предыдущего бронирования
    private BookingLastNextDto getLastBooking(List<Booking> allBookingForItem, LocalDateTime now) {
        Booking lastBooking = null;
        if (allBookingForItem != null && !allBookingForItem.isEmpty()) {
            for (Booking booking : allBookingForItem) {
                if (booking.getStart().isBefore(now)) {
                    if (lastBooking == null && (booking.getStatus().equals(Status.APPROVED))) {
                        lastBooking = booking;
                    } else if (lastBooking == null) {
                        lastBooking = booking;
                    } else if (booking.getEnd().isAfter(lastBooking.getEnd())) {
                        lastBooking = booking;
                    }
                }
            }
        }
        return bookingLastNextDtoMapper.toBookingLastNextDto(lastBooking);
    }
}
