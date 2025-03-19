package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Map;

public interface UserStorage {
    User create(User user);

    User update(User newUser);

    Collection<User> getAllUsers();

    Map<Integer, User> getUsers();

}
