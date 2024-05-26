package ru.practicum.shareit.request.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemRequestServiceImpl implements ItemRequestService {

    private final UserService userService;
    private final ItemRequestRepository itemRequestRepository;

    public ItemRequest createItemRequest(ItemRequest itemRequest, Long userId) throws NotFoundException {
        User user = userService.getUserById(userId);
        log.info("Запрос успешно создан");
        itemRequest.setRequester(user);
        itemRequest.setCreated(LocalDateTime.now());
        return itemRequestRepository.save(itemRequest);
    }

    @Transactional
    public List<ItemRequest> getAllItemRequestOfUser(Long userId, Integer from, Integer size) {
        if (from < 0) {
            throw new ValidationException("Индекс первого элемента должен быть не отрицательным");
        } else if (size <= 0) {
            throw new ValidationException("Количество элементов для отображения должно быть положительным");
        }
        userService.getUserById(userId);
        List<ItemRequest> allItemRequestOfUser = itemRequestRepository.findAllItemRequestByRequester_Id(userId, PageRequest.of(from, size));
        if (allItemRequestOfUser.isEmpty()) {
            return new ArrayList<>();
        } else {
            return allItemRequestOfUser;
        }
    }

    @Transactional
    public List<ItemRequest> getAllItemRequestOfOtherUsers(Long userId, Integer from, Integer size) {
        if (from < 0) {
            throw new ValidationException("Индекс первого элемента должен быть не отрицательным");
        } else if (size <= 0) {
            throw new ValidationException("Количество элементов для отображения должно быть положительным");
        }
        userService.getUserById(userId);
        List<ItemRequest> allItemRequestOfUser = itemRequestRepository.findAllItemRequestByRequester_IdIsNot(userId, PageRequest.of(from, size));
        if (allItemRequestOfUser.isEmpty()) {
            return new ArrayList<>();
        } else {
            return allItemRequestOfUser;
        }
    }

    public ItemRequest getItemRequest(Long requestId, Long userId) {
        userService.getUserById(userId);
        Optional<ItemRequest> itemRequest = itemRequestRepository.findById(requestId);
        if (itemRequest.isPresent()) {
            return itemRequest.get();
        } else {
            log.info("Запрос Id = {} не найден", requestId);
            throw new NotFoundException("Запрос не найден");
        }
    }
}
