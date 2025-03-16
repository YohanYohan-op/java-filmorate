package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

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
        if (!StringUtils.hasText(user.getEmail()) || !user.getEmail().contains("@") || !StringUtils.hasText(user.getLogin()) || user.getLogin().contains(" ") || user.getBirthday().isBefore(LocalDate.of(1910, 1, 1)) || user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Ошибка создания сущности: {}", user);
            throw new ValidationException("invalid data");
        }
        user.setId(++current);
        user.setFriendsList(new HashSet<>());
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        log.info("Сущность успешно создана: id {}", user.getId());
        log.debug("user: {}", user);
        return user;
    }

    public User update(User newUser) {
        if (!StringUtils.hasText(newUser.getEmail()) || !newUser.getEmail().contains("@") || !StringUtils.hasText(newUser.getLogin()) || newUser.getLogin().contains(" ") || newUser.getBirthday().isBefore(LocalDate.of(1910, 1, 1)) || newUser.getBirthday().isAfter(LocalDate.now()) || newUser.getId() == 0 || newUser.getId() > current) {
            log.error("Ошибка обновления сущности {}", newUser);
            throw new ValidationException("invalid data");
        }
        if (!users.containsKey(newUser.getId())) {
            throw new UserNotFoundException("User with id " + newUser.getId() + " not found.");
        }
        User oldUser = users.get(newUser.getId());
        if (newUser.getName() == null) {
            newUser.setName(newUser.getLogin());
            oldUser.setName(newUser.getName());
        } else {
            oldUser.setName(newUser.getName());
        }
        oldUser.setEmail(newUser.getEmail());
        oldUser.setLogin(newUser.getLogin());
        oldUser.setBirthday(newUser.getBirthday());
        users.put(oldUser.getId(), oldUser);
        log.info("Сущность успешно обновлена: id {}", oldUser.getId());
        log.debug("user: {}", oldUser);
        return oldUser;
    }
}
