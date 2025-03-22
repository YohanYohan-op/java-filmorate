package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.filmorate.exceptions.FriendsException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.services.UserService;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserStorage userStorage;

    @InjectMocks
    private UserService userService;

    private User user;
    private User friend;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1);
        user.setEmail("test@example.com");
        user.setLogin("testlogin");
        user.setName("Test User");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        friend = new User();
        friend.setId(2);
        friend.setEmail("friend@example.com");
        friend.setLogin("friendlogin");
        friend.setName("Friend User");
        friend.setBirthday(LocalDate.of(1992, 2, 2));
    }

    @Test
    void addFriendShouldAddFriendToUser() {
        when(userStorage.getUserById(1)).thenReturn(Optional.of(user));
        when(userStorage.getUserById(2)).thenReturn(Optional.of(friend));
        when(userStorage.update(user)).thenReturn(user);
        userService.addFriend(1, 2);

        assertTrue(user.getFriendsList().contains(2));
        verify(userStorage, times(1)).update(user);
    }

    @Test
    void addFriendShouldThrowNotFoundExceptionWhenUserNotFound() {
        when(userStorage.getUserById(1)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.addFriend(1, 2));
    }

    @Test
    void addFriendShouldThrowFriendsExceptionWhenAddingSelfAsFriend() {
        assertThrows(FriendsException.class, () -> userService.addFriend(1, 1));
    }

    @Test
    void deleteFriendShouldRemoveFriendFromUser() {
        user.addFriend(2);
        when(userStorage.getUserById(1)).thenReturn(Optional.of(user));
        when(userStorage.getUserById(2)).thenReturn(Optional.of(friend));
        when(userStorage.update(user)).thenReturn(user);

        userService.deleteFriend(1, 2);

        assertFalse(user.getFriendsList().contains(2));
        verify(userStorage, times(1)).update(user);
    }

    @Test
    void getFriendsShouldReturnSetOfFriends() {
        user.addFriend(2);
        when(userStorage.getUserById(1)).thenReturn(Optional.of(user));

        Set<Integer> friends = userService.getFriends(1);

        assertEquals(1, friends.size());
        assertTrue(friends.contains(2));
    }

    @Test
    void getAllUsersShouldReturnAllUsersFromStorage() {
        List<User> users = Arrays.asList(user, friend);
        when(userStorage.getAllUsers()).thenReturn(users);

        Collection<User> result = userService.getAllUsers();

        assertEquals(2, result.size());
        verify(userStorage, times(1)).getAllUsers();
    }

    @Test
    void createUserShouldCreateUserInStorage() {
        when(userStorage.create(user)).thenReturn(user);

        User createdUser = userService.create(user);

        assertEquals(1, createdUser.getId());
        verify(userStorage, times(1)).create(user);
    }

    @Test
    void updateUserShouldUpdateUserInStorage() {
        when(userStorage.getUserById(1)).thenReturn(Optional.of(user));
        when(userStorage.update(user)).thenReturn(user);

        User updatedUser = userService.update(user);

        assertEquals(1, updatedUser.getId());
        verify(userStorage, times(1)).update(user);
    }

    @Test
    void updateUserShouldThrowNotFoundExceptionWhenUserNotFound() {
        when(userStorage.getUserById(1)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.update(user));
    }
}
