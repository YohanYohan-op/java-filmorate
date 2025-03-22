package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.services.FilmService;

import java.util.Collection;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService service;

    @GetMapping
    public Collection<Film> getAllFilms() {
        return service.getFilmStorage().getAllFilms();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        return service.getFilmStorage().create(film);
    }

    @PutMapping
    public Film update(@RequestBody Film newFilm) {
        return service.getFilmStorage().update(newFilm);
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

