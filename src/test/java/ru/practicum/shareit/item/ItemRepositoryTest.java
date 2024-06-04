package ru.practicum.shareit.item;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import javax.transaction.Transactional;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DataJpaTest
@Transactional
class ItemRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @BeforeEach
    private void addItem() {
        User user1 = userRepository.save(new User(
                "Name",
                "name@mail.ru"
        ));

        User user2 = userRepository.save(new User(
                "Secondary",
                "mail@mail.ru"
        ));

        Item item1 = itemRepository.save(new Item(
                "Telephone",
                "A device for calls and correspondence with your friends",
                user1.getId(),
                true,
                null
        ));

        Item item2 = itemRepository.save(new Item(
                "Camera",
                "Allows you to take great photos",
                user2.getId(),
                true,
                null
        ));

        Item item3 = itemRepository.save(new Item(
                "Curtains",
                "They will hide you from the sun",
                user1.getId(),
                false,
                null
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