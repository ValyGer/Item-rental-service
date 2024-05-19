package ru.practicum.shareit.request.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.user.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemRequestServiceImpl implements ItemRequestService {

    private final UserService userService;
    private final ItemRequestRepository itemRequestRepository;

    public ItemRequest createItemRequest(ItemRequest itemRequest, Long userId) {
        return null;
    }

    public List<ItemRequest> getAllItemRequestOfUser(Long userId, Integer from, Integer size) {
        return null;
    }

    public List<ItemRequest> getAllItemRequestOfOtherUsers(Long userId, Integer from, Integer size) {
        return null;
    }

    public ItemRequest getItemRequest(Long requestId, Long userId) {
        return null;
    }
}
