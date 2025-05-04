package com.foodbuddy.food_buddy_api.UnitTest.application;

import com.foodbuddy.food_buddy_api.application.event.ItemEventPublisher;
import com.foodbuddy.food_buddy_api.application.service.ItemService;
import com.foodbuddy.food_buddy_api.domain.event.ItemCreatedEvent;
import com.foodbuddy.food_buddy_api.domain.model.*;
import com.foodbuddy.food_buddy_api.domain.repository.ItemRepository;
import com.foodbuddy.food_buddy_api.domain.repository.MyUserRepository;
import com.foodbuddy.food_buddy_api.domain.repository.StorageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ItemServiceTest {

    @Mock private ItemRepository itemRepository;
    @Mock private StorageRepository storageRepository;
    @Mock private MyUserRepository userRepository;
    @Mock private ItemEventPublisher eventPublisher;

    @InjectMocks
    private ItemService itemService;

    private MyUser testUser;
    private Community testCommunity;
    private Storage testStorage;

    @BeforeEach
    void setupMocks() {
        testUser = new MyUser();
        testUser.setUsername("testuser");
        testUser.setId(1L);

        testCommunity = new Community();
        testCommunity.setId(1L);
        testCommunity.addMember(testUser);

        testStorage = new Storage();
        testStorage.setId(1L);
        testStorage.setName("Test Storage");
        testStorage.setCommunity(testCommunity);

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(storageRepository.findById(1L)).thenReturn(Optional.of(testStorage));
    }

    @Test
    void createItem_shouldSucceed() {
        Item item = new Item();
        item.setName("Test Item");

        Item savedItem = new Item();
        savedItem.setId(100L);
        savedItem.setName("Test Item");
        savedItem.setStorage(testStorage);

        when(itemRepository.save(item)).thenReturn(savedItem);

        Item result = itemService.createItem(1L, item, "testuser");

        assertNotNull(result);
        assertEquals("Test Item", result.getName());
        assertEquals(100L, result.getId());
        verify(eventPublisher).publish(any(ItemCreatedEvent.class));
    }

@Test
    void createItem_shouldThrow_whenUserNotMember() {
        Long storageId = 1L;
        String username = "unauthorizedUser";

        MyUser user = new MyUser();
        user.setId(200L);
        user.setUsername(username);

        Community community = new Community();
        community.setId(1L);

        Storage storage = new Storage();
        storage.setId(storageId);
        storage.setCommunity(community);

        Item item = new Item();
        item.setName("Apfel");

        when(storageRepository.findById(storageId)).thenReturn(Optional.of(storage));
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        assertThrows(RuntimeException.class, () -> {
            itemService.createItem(storageId, item, username);
        });
    }

    @Test
    void updateItem_shouldUpdateFieldsCorrectly() {
        Long itemId = 1L;
        Long newStorageId = 2L;
        String username = "testuser";

        MyUser user = new MyUser();
        user.setId(1L);
        user.setUsername(username);

        Community oldCommunity = new Community();
        oldCommunity.setId(1L);
        oldCommunity.setLeader(user);
        oldCommunity.addMember(user);

        Storage oldStorage = new Storage();
        oldStorage.setId(1L);
        oldStorage.setCommunity(oldCommunity);

        Community newCommunity = new Community();
        newCommunity.setId(2L);
        newCommunity.setLeader(user);
        newCommunity.addMember(user);

        Storage newStorage = new Storage();
        newStorage.setId(newStorageId);
        newStorage.setCommunity(newCommunity);

        Item existingItem = new Item();
        existingItem.setId(itemId);
        existingItem.setName("Altes Produkt");
        existingItem.setStorage(oldStorage);

        Item updatedItem = new Item();
        updatedItem.setName("Neues Produkt");
        updatedItem.setBrand("Neue Marke");
        updatedItem.setBarcode("123456789");

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(existingItem));
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(storageRepository.findById(newStorageId)).thenReturn(Optional.of(newStorage));
        when(itemRepository.save(any(Item.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Item result = itemService.updateItem(itemId, updatedItem, newStorageId, username);

        assertNotNull(result);
        assertEquals("Neues Produkt", result.getName());
        assertEquals("Neue Marke", result.getBrand());
        assertEquals("123456789", result.getBarcode());
        assertEquals(newStorage, result.getStorage());
    }
}