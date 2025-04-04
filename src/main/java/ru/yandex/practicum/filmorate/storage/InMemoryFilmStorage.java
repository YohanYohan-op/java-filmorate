package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    @Getter
    private final Map<Integer, Film> films = new HashMap<>();
    private int current = 0;

    @Override
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @Override
    public Film create(Film film) {
        film.setId(++current);
        films.put(film.getId(), film);
        log.info("Создан фильм с ID: {}", film.getId());
        log.debug("film: {}", film);
        return film;
    }

    @Override
    public Film update(Film film) {
        int filmId = film.getId();

        if (!films.containsKey(filmId)) {
            log.error("Фильм с ID {} не найден", filmId);
            throw new NotFoundException("Фильм с ID " + filmId + " не найден");
        }

        films.put(filmId, film); // Просто обновляем фильм
        log.info("Обновлен фильм с ID: {}", film.getId());
        log.debug("film: {}", film);
        return film;
    }

    @Override
    public void delete(int filmId) {
        if (!films.containsKey(filmId)) {
            log.error("Фильм с ID {} не найден для удаления", filmId);
            throw new NotFoundException("Фильм с ID " + filmId + " не найден");
        }
        films.remove(filmId);
        log.info("Фильм с ID {} удален", filmId);
    }

    @Override
    public Optional<Film> getFilmById(int id) {
        return Optional.ofNullable(films.get(id));
    }

}

