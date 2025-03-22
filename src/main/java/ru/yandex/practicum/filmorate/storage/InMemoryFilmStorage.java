package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private static final LocalDate FILM_BIRTHDAY = LocalDate.of(1895, 12, 28);
    @Getter
    private final Map<Integer, Film> films = new HashMap<>();
    private int current = 0;

    @Override
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @Override
    public Film create(Film film) {
        if (!StringUtils.hasText(film.getName()) || !StringUtils.hasText(film.getDescription()) || film.getDescription().length() > 200 || film.getReleaseDate() == null || film.getReleaseDate().isBefore(FILM_BIRTHDAY) || film.getDuration() <= 0) {
            log.error("Ошибка создания сущности {}", film);
            throw new ValidationException("invalid data");
        }
        film.setId(++current);
        film.setLikeScore(new HashSet<>());
        films.put(film.getId(), film);
        log.info("Сущность успешно создана: id {}", film.getId());
        log.debug("film: {}", film);
        return film;
    }

    @Override
    public Film update(Film newFilm) {
        if (!StringUtils.hasText(newFilm.getName()) || newFilm.getDescription().length() > 200 || newFilm.getReleaseDate() == null || newFilm.getReleaseDate().isBefore(FILM_BIRTHDAY) || newFilm.getDuration() <= 0 || newFilm.getId() == 0 || newFilm.getId() > current) {
            log.error("Ошибка обновления сущности:{}", newFilm);
            throw new ValidationException("invalid data");
        }
        if (!films.containsKey(newFilm.getId())) {
            throw new NotFoundException("Film with id " + newFilm.getId() + " not found.");
        }
        Film oldFilm = films.get(newFilm.getId());
        oldFilm.setName(newFilm.getName());
        oldFilm.setDescription(newFilm.getDescription());
        oldFilm.setReleaseDate(newFilm.getReleaseDate());
        oldFilm.setDuration(newFilm.getDuration());
        films.put(oldFilm.getId(), oldFilm);
        log.info("Сущность успешно обновлена: id={}", oldFilm.getId());
        log.debug("film: {}", oldFilm);
        return oldFilm;
    }
    @Override
    public Collection<Film> delete(Film film) {
        if (film == null || film.getId() == 0 || film.getId() > current) {
            log.error("Ошибка удаления сущности:{}", film);
            throw new ValidationException("invalid data");
        }
        films.remove(film.getId());
        return films.values();
    }
}

