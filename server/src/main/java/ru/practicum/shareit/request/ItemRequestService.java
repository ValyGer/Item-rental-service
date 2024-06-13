package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestService {
    ItemRequest createItemRequest(ItemRequestDto itemRequestDto, Long userId);

    List<ItemRequest> getAllItemRequestOfUser(Long userId, Integer from, Integer size);

    List<ItemRequest> getAllItemRequestOfOtherUsers(Long userId, Integer from, Integer size);

    ItemRequest getItemRequest(Long requestId, Long userId);
}
