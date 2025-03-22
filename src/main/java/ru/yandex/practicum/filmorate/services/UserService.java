package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FriendsException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public void addFriend(int userId, int friendId) {
        validateFriendship(userId, friendId);
        User user = getUserById(userId);
        User friend = getUserById(friendId);

        user.addFriend(friendId);
        userStorage.update(user);
        log.info("Пользователь {} добавил в друзья пользователя {}", userId, friendId);
    }

    public void deleteFriend(int userId, int friendId) {
        validateFriendship(userId, friendId);
        User user = getUserById(userId);
        User friend = getUserById(friendId);

        user.removeFriend(friendId);
        userStorage.update(user);
        log.info("Пользователь {} удалил из друзей пользователя {}", userId, friendId);
    }

    public Set<Integer> getMutualFriends(int userId, int otherId) {
        validateFriendship(userId, otherId);
        User user1 = getUserById(userId);
        User user2 = getUserById(otherId);

        log.info("Получение общих друзей пользователей {} и {}", userId, otherId);
        return user1.getFriendsList().stream()
                .filter(friendId -> user2.getFriendsList().contains(friendId))
                .collect(Collectors.toSet());
    }

    public Set<Integer> getFriends(int userId) {
        User user = getUserById(userId);
        log.info("Получение списка друзей пользователя {}", userId);
        return user.getFriendsList();
    }

    public Collection<User> getAllUsers() {
        log.info("Получение всех пользователей");
        return userStorage.getAllUsers();
    }

    public User create(User user) {
        validateUser(user);
        User createdUser = userStorage.create(user);
        log.info("Создан пользователь с ID: {}", createdUser.getId());
        return createdUser;
    }

    public User update(User user) {
        validateUser(user);
        getUserById(user.getId()); // Check if user exists
        User updatedUser = userStorage.update(user);
        log.info("Обновлен пользователь с ID: {}", updatedUser.getId());
        return updatedUser;
    }

    private User getUserById(int userId) {
        return userStorage.getUserById(userId)
                .orElseThrow(() -> {
                    log.error("Пользователь с ID {} не найден", userId);
                    return new NotFoundException("Пользователь с ID " + userId + " не найден");
                });
    }


    private void validateFriendship(int userId, int friendId) {
        if (userId == friendId) {
            log.error("Попытка добавить/удалить себя из друзей: userId={}, friendId={}", userId, friendId);
            throw new FriendsException("Нельзя добавить/удалить самого себя из друзей"); // Более подходящее исключение
        }
    }

    private void validateUser(User user) {
        if (user == null) {
            log.error("Передан null user");
            throw new ValidationException("Пользователь не может быть null");
        }
        // Validation logic here (email, login, birthday)
    }
}

