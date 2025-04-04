package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public void addLike(int filmId, int userId) {
        getFilmById(filmId);
        userStorage.getUserById(userId).orElseThrow(() -> new NotFoundException("Пользователь с ID " + userId + " не найден"));
        Film film = getFilmById(filmId);
        film.addLike(userId);
        filmStorage.update(film);
        log.info("Пользователь {} поставил лайк фильму {}", userId, filmId);
    }

    public void removeLike(int filmId, int userId) {
        getFilmById(filmId);
        userStorage.getUserById(userId).orElseThrow(() -> new NotFoundException("Пользователь с ID " + userId + " не найден"));

        Film film = getFilmById(filmId);
        film.removeLike(userId);
        filmStorage.update(film);
        log.info("Пользователь {} убрал лайк с фильма {}", userId, filmId);
    }

    public List<Film> getPopularFilms(int count) {
        if (count <= 0) {
            log.error("Неверное значение count: {}", count);
            throw new ValidationException("Count должен быть больше 0");
        }
        log.info("Получение {} популярных фильмов", count);
        return filmStorage.getAllFilms().stream().sorted(Comparator.<Film>comparingInt(film -> film.getLikeScore().size()).reversed()).limit(count).collect(Collectors.toList());
    }

    public Collection<Film> getAllFilms() {
        log.info("Получение всех фильмов");
        return filmStorage.getAllFilms();
    }

    public Film create(Film film) {
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Неверная дата релиза фильма");
            throw new ValidationException("Дата релиза фильма не может быть раньше 28 декабря 1895 года");
        }
        Film createdFilm = filmStorage.create(film);
        log.info("Создан фильм с ID: {}", createdFilm.getId());
        return createdFilm;
    }

    public Film update(Film film) {
        getFilmById(film.getId());
        Film updatedFilm = filmStorage.update(film);
        log.info("Обновлен фильм с ID: {}", updatedFilm.getId());
        return updatedFilm;
    }

    private Film getFilmById(int filmId) {
        return filmStorage.getFilmById(filmId).orElseThrow(() -> {
            log.error("Фильм с ID {} не найден", filmId);
            return new NotFoundException("Фильм с ID " + filmId + " не найден");
        });
    }
}