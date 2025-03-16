package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.services.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;

import java.util.Collection;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final FilmStorage storage;
    private final FilmService service;

    public FilmController(FilmService service, InMemoryFilmStorage storage) {
        this.service = service;
        this.storage = storage;
    }

    @GetMapping
    public Collection<Film> getAllFilms() {
        return storage.getAllFilms();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        return storage.create(film);
    }

    @PutMapping
    public Film update(@RequestBody Film newFilm) {
        return storage.update(newFilm);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable int id, @PathVariable int userId) {
        return service.addLike(userId, id, true).get();
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film dislike(@PathVariable int id, @PathVariable int userId) {
        return service.addLike(id, userId, false).get();
    }

    @GetMapping("/popular")
    public List<Film> getTenTheMostPopularFilms(@RequestParam(defaultValue = "10") int count) {
        return service.getTenTheMostPopularFilms(count);
    }
}
