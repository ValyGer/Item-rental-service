package ru.practicum.shareit.item;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @BeforeEach
    private void addItem() {
        userRepository.save(new User(
                1L,
                "Name",
                "name@mail.ru"
        ));

        userRepository.save(new User(
                2L,
                "Secondary",
                "mail@mail.ru"
        ));

        itemRepository.save(new Item(
                1L,
                "Telephone",
                "A device for calls and correspondence with your friends",
                1L,
                true
        ));

        itemRepository.save(new Item(
                2L,
                "Camera",
                "Allows you to take great photos",
                2L,
                true
        ));

        itemRepository.save(new Item(
                3L,
                "Curtains",
                "They will hide you from the sun",
                1L,
                false
        ));
    }

    // "you" содержится в описании всех 3 вещей, метод возвращает только вещи со статусом isAvailable = true
    @Test
    void searchAvailableItems() {
        List<Item> actualItems = itemRepository.searchAvailableItems("you");

        assertThat(actualItems.size(), equalTo(2));
    }

    @AfterEach
    private void deleteItem() {
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }
}