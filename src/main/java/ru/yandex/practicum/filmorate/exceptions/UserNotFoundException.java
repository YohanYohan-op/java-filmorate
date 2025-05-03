package ru.yandex.practicum.filmorate.exceptions;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(int id) {
        super("Пользователь с id: " + id + " не найден");
    }

    public UserNotFoundException(String m) {
        super(m);
    }
}
