package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j

public class UserService {
    private final UserStorage storage;
    private Integer id = 0;

    @Autowired
    public UserService(UserStorage storage) {
        this.storage = storage;
    }

    public List<User> getAllUsers() {
        log.debug("Получен запрос GET /users");
        return storage.getAllUsers();
    }


    public User getUserById(Integer id) {
        return storage.getUserById(id).orElseThrow(() -> new FilmNotFoundException("Пользователь с id " + id + " не найден."));
    }


    public User createUser(User user) {
        createUserId(user);
        log.debug("Получен запрос POST. Передан обьект {}", user);
        return storage.addUser(user);
    }


    public User updateUser(User user) {
        getUserById(user.getId());
        log.debug("Пользователь {} обновлен", user);
        return storage.updateUser(user);

    }


    public void addFriend(Integer id, Integer friendId) {
        getUserById(friendId).addFriend(id);
        getUserById(id).addFriend(friendId);
        log.debug("Пользователь {} добавлен в друзья к {}", friendId, id);
    }


    public void removeFriend(Integer id, Integer userId) {
        getUserById(id).getFriends().remove(userId);
        getUserById(userId).getFriends().remove(id);
    }


    public List<User> getFriends(Integer id) {
        getUserById(id);
        return storage.getUserFriends(id);

    }


    public List<User> getCrossFriends(Integer id, Integer userId) {
        User firstUser = getUserById(id);
        User secondUser = getUserById(userId);
        log.debug("Получен список общих друзей {} с {}", id, userId);
        return firstUser.getFriends().stream().filter(secondUser.getFriends()::contains).map(this::getUserById).collect(Collectors.toList());
    }


    public void reset() {
        log.debug("Хранилище пользователей очищено");
        storage.reset();
    }

    public void createUserId(User user) {
        id++;
        user.setId(id);
    }


}
