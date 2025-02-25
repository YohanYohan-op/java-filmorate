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
    private final LocalDate filmBirthday = LocalDate.of(1895, 12, 28);
    private final Map<Integer, Film> films = new HashMap<>();
    private int current = 0;

    @GetMapping
    public Collection<Film> getAllUsers() {
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        if (film.getName() == null || film.getName().isBlank() || film.getDescription().length() > 200 || film.getReleaseDate().isBefore(filmBirthday)
                || film.getDuration() < 0) {
            log.error("Ошибка создания сущности {}", film);
            throw new ValidationException("invalid data");
        }
        film.setId(++current);
        films.put(film.getId(), film);
        log.info("Сущность успешно создана: {}", film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film newFilm) {
        if (newFilm.getName() == null || newFilm.getName().isBlank() || newFilm.getDescription().length() > 200 || newFilm.getReleaseDate().isBefore(filmBirthday)
                || newFilm.getDuration() < 0) {
            log.error("Ошибка обновления сущности{}", newFilm);
            throw new ValidationException("invalid data");
        }
        Film oldFilm = films.get(newFilm.getId());
        oldFilm.setName(newFilm.getName());
        oldFilm.setDescription(newFilm.getDescription());
        oldFilm.setReleaseDate(newFilm.getReleaseDate());
        oldFilm.setDuration(newFilm.getDuration());
        films.put(oldFilm.getId(), oldFilm);
        log.info("Сущность успешно обновлена: {}", oldFilm);
        return oldFilm;
    }
}
