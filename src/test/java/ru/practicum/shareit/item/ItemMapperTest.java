package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapperImpl;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class ItemMapperTest {

    @InjectMocks
    ItemMapperImpl itemMapper;

    @Test
    void toItemDto() {
        User user = new User(1L, "User", "yandex@mail.ru");
        Item item = new Item(1L, "Name of Item", "Description of Item", user.getId(), true);

        ItemDto itemDto = itemMapper.toItemDto(item);

        assertEquals(itemDto.getId(), item.getItemId());
        assertEquals(itemDto.getName(), item.getName());
        assertEquals(itemDto.getDescription(), item.getDescription());
        assertEquals(itemDto.getAvailable(), item.getIsAvailable());
    }

    @Test
    void toItemDtoWhenNull() {
        Item item = null;
        ItemDto itemDto = itemMapper.toItemDto(item);

        assertNull(itemDto);
    }

    @Test
    void toItem() {
        ItemDto itemDto = new ItemDto("Name of Item", "Description of Item", true, null);

        Item item = itemMapper.toItem(itemDto);

        assertEquals(item.getName(), itemDto.getName());
        assertEquals(item.getDescription(), itemDto.getDescription());
        assertEquals(item.getIsAvailable(), itemDto.getAvailable());
        assertEquals(item.getRequest(), itemDto.getRequestId());
    }

    @Test
    void toItemWhenNull() {
        ItemDto itemDto = null;

        Item item = itemMapper.toItem(itemDto);

        assertNull(item);
    }
}