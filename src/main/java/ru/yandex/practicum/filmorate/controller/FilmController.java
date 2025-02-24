package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final LocalDate FILM_BIRTHDAY = LocalDate.of(1985,12, 28);
    private final Map<Integer, Film> films = new HashMap<>();
    private int current = 0;

    @GetMapping
    public Collection<Film> getAllUsers() {
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        if (film.getName() == null || film.getDescription().length()>200 || film.getReleaseDate().isAfter(FILM_BIRTHDAY)
                || film.getDuration() > 0) {
            log.error("Ошибка создания сущности");
            throw new ValidationException("invalid data");
        }
        film.setId(++current);
        films.put(film.getId(), film);
        log.info("Сущность успешно создана");
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film newFilm) {
        if (newFilm.getName() == null || newFilm.getDescription().length()>200 || newFilm.getReleaseDate().isAfter(FILM_BIRTHDAY)
                || newFilm.getDuration() > 0) {
            log.error("Ошибка обновления сущности");
            throw new ValidationException("invalid data");
        }
        Film oldFilm = films.get(newFilm.getId());
        oldFilm.setName(newFilm.getName());
        oldFilm.setDescription(newFilm.getDescription());
        oldFilm.setReleaseDate(newFilm.getReleaseDate());
        oldFilm.setDuration(newFilm.getDuration());
        films.put(oldFilm.getId(), oldFilm);
        log.error("Сущность успешно обновлена");
        return oldFilm;
    }
}
