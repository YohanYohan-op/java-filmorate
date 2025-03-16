package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
@Service
public class UserService {
    InMemoryUserStorage userStorage;

    @Autowired
    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Set<Integer> addFriend(int userID, int friendUserID) {
        if (userID == friendUserID) {
            log.error("Ошибка, нельзя добавить самого себя:{} - userID, {} - friendUserID", userID, friendUserID);
            throw new UserNotFoundException("Одинаковые id пользователя");
        }
        Optional<User> findUser = userStorage.getAllUsers().stream().filter(user -> user.getId() == userID).findFirst();
        if (findUser.isEmpty()) {
            log.error("Ошибка, пользователь не найден:{} - userID", userID);
            throw new UserNotFoundException("Пользователь не найден");
        }

        Optional<User> findFriend = userStorage.getAllUsers().stream().filter(user -> user.getId() == friendUserID).findFirst();
        if (findFriend.isEmpty()) {
            log.error("Ошибка, пользователь не найден:{} - userID", userID);
            throw new UserNotFoundException("Пользователь не найден");
        }
        User user = findUser.get();
        Set<Integer> userFriendList = user.getFriendsList();
        userFriendList.add(friendUserID);
        user.setFriendsList(userFriendList);
        return user.getFriendsList();
    }

    public Set<Integer> deleteFromFriends(Integer userID, Integer friendUserID) {
        if (userID == friendUserID) {
            log.error("Ошибка, id не могут быть одинаковыми:{} - userID, {} - friendUserID", userID, friendUserID);
            throw new UserNotFoundException("Одинаковые id пользователя");
        }
        if (!userStorage.getUsers().containsKey(friendUserID) || !userStorage.getUsers().containsKey(userID)) {
            log.error("Ошибка, пользователь не найден:{} - friendUserID, {} - userID", friendUserID, userID);
            throw new UserNotFoundException("Пользователь не найден");
        }
        Optional<User> findUser = userStorage.getAllUsers().stream().filter(user -> user.getId() == userID).findFirst();
        if (findUser.isEmpty()) {
            log.error("Ошибка, пользователь не найден:{} - userID", userID);
            throw new UserNotFoundException("Пользователь не найден");
        }
        User user = findUser.get();
        Set<Integer> userFriendList = user.getFriendsList();
        userFriendList.remove(friendUserID);
        user.setFriendsList(userFriendList);
        return user.getFriendsList();
    }

    public Set<Integer> mutualFriends(Integer userID, Integer friendUserID) {
        if (userID == friendUserID) {
            log.error("Ошибка, id не могут быть одинаковыми:{} - userID, {} - friendUserID", userID, friendUserID);
            throw new UserNotFoundException("Одинаковые id пользователя");
        }
        if (!userStorage.getUsers().containsKey(friendUserID) || !userStorage.getUsers().containsKey(userID)) {
            log.error("Ошибка, пользователь не найден:{} - friendUserID, {} - userID", friendUserID, userID);
            throw new UserNotFoundException("Пользователь не найден");
        }
        Optional<User> firstUser = userStorage.getAllUsers().stream().filter(user -> user.getId() == userID).findFirst();
        Optional<User> secondUser = userStorage.getAllUsers().stream().filter(user -> user.getId() == friendUserID).findFirst();
        if (firstUser.isEmpty() || secondUser.isEmpty()) {
            log.error("Ошибка, пользователь не найден:{} - userID, {} - friendUserID", userID, friendUserID);
            throw new UserNotFoundException("User is not found");
        }
        return firstUser.get().getFriendsList().stream().filter(friendId -> secondUser.get().getFriendsList().contains(friendId)).collect(Collectors.toSet());
    }

    public Set<Integer> getFriends(Integer userID) {
        if (!userStorage.getUsers().containsKey(userID)) {
            log.error("Ошибка, пользователь не найден:{} - userID", userID);
            throw new UserNotFoundException("Пользователь не найден");
        }
        Optional<User> firstUser = userStorage.getAllUsers().stream().filter(user -> user.getId() == userID).findFirst();
        return firstUser.map(User::getFriendsList).orElse(null);
    }
}
