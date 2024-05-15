package ru.practicum.shareit.item.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingLastNextDto;
import ru.practicum.shareit.booking.dto.BookingLastNextDtoMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.CommentRepository;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDtoForBookingAndComments;
import ru.practicum.shareit.item.dto.ItemDtoForBookingAndCommentsMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository; // оставляем использование репозитория Comment, так как для сущности не создавался сервис
    private final UserService userService;
    private final BookingService bookingService;
    private final ItemDtoForBookingAndCommentsMapper itemDtoForBookingAndCommentsMapper;
    private final BookingLastNextDtoMapper bookingLastNextDtoMapper;
    private final CommentMapper commentMapper;

    // Добавление вещи
    public Item createItem(Long userId, Item item) throws NotFoundException {
        userService.getUserById(userId);
        log.info("Вещь успешно добавлена");
        item.setOwner(userId);
        return itemRepository.save(item);
    }

    // Обновление вещи
    public Item updateItem(Long userId, Long itemId, Item item) throws NotFoundException {
        userService.getUserById(userId);
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
    }

    // Получение вещей пользователя
    @Transactional
    public List<ItemDtoForBookingAndComments> getAllItemsUser(Long userId) {
        userService.getUserById(userId);
        log.info("Пользователь с Id = {} существует в базе", userId);

        List<Item> items = itemRepository.findItemsByOwnerOrderByItemIdAsc(userId);
        List<ItemDtoForBookingAndComments> allItemByUser = new ArrayList<>();
        for (Item item : items) {
            ItemDtoForBookingAndComments itemFromBd = itemDtoForBookingAndCommentsMapper
                    .toItemDtoForBookingAndComments(item);
            LocalDateTime timeNow = LocalDateTime.now();

            BookingLastNextDto lastBooking = getLastBooking(item, timeNow);
            BookingLastNextDto nextBooking = getNextBooking(item, timeNow);
            itemFromBd.setLastBooking(lastBooking);
            itemFromBd.setNextBooking(nextBooking);

            List<Comment> commentsAboutItem = commentRepository.findAllByItemOrderByItem(item);
            if (commentsAboutItem.isEmpty()) {
                List<CommentDto> comments = new ArrayList<>();
                itemFromBd.setComments(comments);
            } else {
                itemFromBd.setComments(commentsAboutItem.stream()
                        .map(commentMapper::toCommentDto).collect(Collectors.toList()));
                log.info("Список комментариев получен");
            }
            allItemByUser.add(itemFromBd);
        }
        log.info("Список вещей успешно получен");
        return allItemByUser;
    }

    // Получение вещи по Id
    public Item getItemsById(Long itemId) {
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
    @Transactional
    public ItemDtoForBookingAndComments getItemWithBooker(long itemId, long ownerId) {
        Optional<Item> item = itemRepository.findById(itemId);
        if (item.isPresent()) {
            userService.getUserById(ownerId);
            ItemDtoForBookingAndComments itemFromBd = itemDtoForBookingAndCommentsMapper
                    .toItemDtoForBookingAndComments(item.get());
            if (item.get().getOwner() == ownerId) {
                LocalDateTime timeNow = LocalDateTime.now();
                BookingLastNextDto lastBooking = getLastBooking(item.get(), timeNow);
                BookingLastNextDto nextBooking = getNextBooking(item.get(), timeNow);
                itemFromBd.setLastBooking(lastBooking);
                itemFromBd.setNextBooking(nextBooking);
            } else {
                log.info("Пользователь с Id = {} не является владельцев вещи, информация по бронированию не нужна", ownerId);
            }
            //Добавление комментариев
            List<Comment> commentsAboutItem = commentRepository.findAllByItemOrderByItem(item.get());
            if (commentsAboutItem.isEmpty()) {
                List<CommentDto> comments = new ArrayList<>();
                itemFromBd.setComments(comments);
            } else {
                itemFromBd.setComments(commentsAboutItem.stream()
                        .map(commentMapper::toCommentDto).collect(Collectors.toList()));
            }
            log.info("Список комментариев получен");

            return itemFromBd;
        } else {
            log.info("Пользователь с Id = {} не существует в базе", ownerId);
            throw new NotFoundException("Пользователь не найден");
        }
    }

    // Поиск следующего бронирования
    private BookingLastNextDto getNextBooking(Item item, LocalDateTime timeNow) {
        List<Booking> allBookingForItem = bookingService.getAllBookingByUser(item);
        Booking nextBooking = null;
        if (allBookingForItem != null && !allBookingForItem.isEmpty()) {
            for (Booking b : allBookingForItem) {
                if (b.getStart().isAfter(timeNow) && (b.getStatus().equals(Status.APPROVED))
                        || (b.getStatus().equals(Status.WAITING))) {
                    if (nextBooking == null) {
                        nextBooking = b;
                    } else if (b.getStart().isBefore(nextBooking.getStart())) {
                        nextBooking = b;
                    }
                }
            }
            return bookingLastNextDtoMapper.toBookingLastNextDto(nextBooking);
        }
        return null;
    }

    // Поиск предыдущего бронирования
    private BookingLastNextDto getLastBooking(Item item, LocalDateTime timeNow) {
        List<Booking> allBookingForItem = bookingService.getAllBookingByUser(item);
        Booking lastBooking = null;
        if (allBookingForItem != null && !allBookingForItem.isEmpty()) {
            for (Booking b : allBookingForItem) {
                if (b.getStart().isBefore(timeNow)) {
                    if (lastBooking == null && (b.getStatus().equals(Status.APPROVED))) {
                        lastBooking = b;
                    } else if (lastBooking == null) {
                        lastBooking = b;
                    } else if (b.getEnd().isAfter(lastBooking.getEnd())) {
                        lastBooking = b;
                    }
                }
            }
            return bookingLastNextDtoMapper.toBookingLastNextDto(lastBooking);
        }
        return null;
    }

    // Добавление комментариев
    public Comment addComment(long userId, long itemId, Comment comment) {
        Optional<Item> item = itemRepository.findById(itemId);
        if (item.isPresent()) {
            User user = userService.getUserById(userId);
            List<Booking> booking = bookingService.getAllBookingForItemByUser(item.get(), user,
                    LocalDateTime.now());
            if (booking.isEmpty()) {
                log.error("Пользователь c id = {} не может оставить отзыв на вещь c id = {}," +
                        " так как не брал ее в аренду.", userId, itemId);
                throw new ValidationException("Пользователь не может оставить отзыв на вещь если не брал ее в аренду.");
            }
            comment.setAuthor(user);
            comment.setItem(item.get());
            comment.setCreate(LocalDateTime.now());
            log.info("Добавлен новый комментарий к вещи id = {}.", itemId);
            return commentRepository.save(comment);
        } else {
            log.info("Пользователь с Id = {} не существует в базе", userId);
            throw new NotFoundException("Пользователь не найден");
        }
    }
}