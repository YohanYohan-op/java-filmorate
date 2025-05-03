package ru.yandex.practicum.filmorate.exceptions;

public class GenreNotFoundException extends RuntimeException {
    public GenreNotFoundException(int id) {
        super("Жанр с id " + id + " не найден");
    }

    public GenreNotFoundException(String id) {
        super("Жанр с id " + id + " не найден");
    }
}
