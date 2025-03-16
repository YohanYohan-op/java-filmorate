package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    public Film create(Film film);

    public Film update(Film newFilm);

    public Collection<Film> delete(Film newFilm);

    public Collection<Film> getAllFilms();
}
