package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    @Getter
    private final Map<Integer, User> users = new HashMap<>();
    private int current = 0;

    public Collection<User> getAllUsers() {
        return users.values();
    }

    public User create(User user) {
        user.setId(++current);
        //Устанавливаем имя пользователя, если оно не задано
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        users.put(user.getId(), user);
        log.info("Создан пользователь с ID: {}", user.getId());
        log.debug("user: {}", user);
        return user;
    }

    public User update(User user) {
        int userId = user.getId();
        if (!users.containsKey(userId)) {
            log.error("Пользователь с ID {} не найден", userId);
            throw new NotFoundException("Пользователь с ID " + userId + " не найден");
        }

        //Устанавливаем имя пользователя, если оно не задано
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        users.put(userId, user);
        log.info("Обновлен пользователь с ID: {}", user.getId());
        log.debug("user: {}", user);
        return user;
    }

    @Override
    public Optional<User> getUserById(int id) {
        return Optional.ofNullable(users.get(id));
    }
}

