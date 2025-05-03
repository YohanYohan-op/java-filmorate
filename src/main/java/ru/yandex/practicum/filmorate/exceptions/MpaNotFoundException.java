package ru.yandex.practicum.filmorate.exceptions;

public class MpaNotFoundException extends RuntimeException {
    public MpaNotFoundException(int id) {
        super("MPA с id " + id + " не найден");
    }
}
