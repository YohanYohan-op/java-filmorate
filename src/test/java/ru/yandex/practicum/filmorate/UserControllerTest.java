package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.services.UserService;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1);
        user.setEmail("test@example.com");
        user.setLogin("testlogin");
        user.setName("Test User");
        user.setBirthday(LocalDate.of(1990, 1, 1));
    }

    @Test
    void getAllUsersShouldReturnCollectionOfUsers() {
        when(userService.getAllUsers()).thenReturn(List.of(user));

        Collection<User> users = userController.getAllUsers();

        assertEquals(1, users.size());
        assertEquals("test@example.com", users.iterator().next().getEmail());
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void createUserShouldReturnCreatedUser() {
        when(userService.create(user)).thenReturn(user);

        User createdUser = userController.create(user);

        assertEquals(1, createdUser.getId());
        assertEquals("test@example.com", createdUser.getEmail());
        verify(userService, times(1)).create(user);
    }

    @Test
    void updateUserShouldReturnUpdatedUser() {
        when(userService.update(user)).thenReturn(user);

        User updatedUser = userController.update(user);

        assertEquals(1, updatedUser.getId());
        assertEquals("test@example.com", updatedUser.getEmail());
        verify(userService, times(1)).update(user);
    }

    @Test
    void addFriendShouldCallUserServiceAddFriend() {
        int userId = 1;
        int friendId = 2;

        userController.addFriend(userId, friendId);

        verify(userService, times(1)).addFriend(userId, friendId);
    }

    @Test
    void deleteFriendShouldCallUserServiceDeleteFriend() {
        int userId = 1;
        int friendId = 2;

        userController.deleteFriend(userId, friendId);

        verify(userService, times(1)).deleteFriend(userId, friendId);
    }

    @Test
    void getMutualFriendsShouldReturnSetOfMutualFriends() {
        int userId = 1;
        int otherId = 2;
        Set<Integer> mutualFriends = new HashSet<>();
        mutualFriends.add(3);

        when(userService.getMutualFriends(userId, otherId)).thenReturn(mutualFriends);

        Set<Integer> result = userController.getMutualFriends(userId, otherId);

        assertEquals(1, result.size());
        assertEquals(3, result.iterator().next());
        verify(userService, times(1)).getMutualFriends(userId, otherId);
    }

    @Test
    void getFriendsShouldReturnSetOfFriends() {
        int userId = 1;
        Set<Integer> friends = new HashSet<>();
        friends.add(2);

        when(userService.getFriends(userId)).thenReturn(friends);

        Set<Integer> result = userController.getFriends(userId);

        assertEquals(1, result.size());
        assertEquals(2, result.iterator().next());
        verify(userService, times(1)).getFriends(userId);
    }
}
