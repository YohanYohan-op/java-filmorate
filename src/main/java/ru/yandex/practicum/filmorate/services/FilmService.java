package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

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
        validateId(filmId, userId);
        Film film = getFilmById(filmId);
        userStorage.getUserById(userId); //Проверяем, существует ли пользователь. Если нет, будет исключение
        film.addLike(userId);
        filmStorage.update(film);
        log.info("Пользователь {} поставил лайк фильму {}", userId, filmId);
    }

    public void removeLike(int filmId, int userId) {
        validateId(filmId, userId);
        Film film = getFilmById(filmId);
        userStorage.getUserById(userId); //Проверяем, существует ли пользователь. Если нет, будет исключение
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
        return filmStorage.getAllFilms().stream()
                .sorted(Comparator.<Film>comparingInt(film -> film.getLikeScore().size()).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    public Collection<Film> getAllFilms() {
        log.info("Получение всех фильмов");
        return filmStorage.getAllFilms();
    }

    public Film create(Film film) {
        validateFilm(film);
        Film createdFilm = filmStorage.create(film);
        log.info("Создан фильм с ID: {}", createdFilm.getId());
        return createdFilm;
    }

    public Film update(Film film) {
        validateFilm(film);
        getFilmById(film.getId()); // Check if film exists
        Film updatedFilm = filmStorage.update(film);
        log.info("Обновлен фильм с ID: {}", updatedFilm.getId());
        return updatedFilm;
    }

    private Film getFilmById(int filmId) {
        return filmStorage.getFilmById(filmId)
                .orElseThrow(() -> {
                    log.error("Фильм с ID {} не найден", filmId);
                    return new NotFoundException("Фильм с ID " + filmId + " не найден");
                });
    }


    private void validateId(int filmId, int userId) {
        if (filmId <= 0 || userId <= 0) {
            log.error("Невалидные ID: filmId={}, userId={}", filmId, userId);
            throw new ValidationException("ID фильма и пользователя должны быть больше 0");
        }
    }

    private void validateFilm(Film film) {
        if (film == null) {
            log.error("Передан null film");
            throw new ValidationException("Фильм не может быть null");
        }
        // Validation logic here (name, description, date, duration)
    }
}

