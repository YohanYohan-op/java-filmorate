package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    void reset();

    List<Film> getAllFilms();

    Optional<Film> findById(Integer id);

    Film addFilm(Film film);

    Film updateFilm(Film film);

    List<Film> getMostPopular(Integer count);
}
