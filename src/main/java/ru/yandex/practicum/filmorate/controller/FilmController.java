package ru.yandex.practicum.filmorate.controller;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.services.FilmService;

import java.time.LocalDate;
import java.util.List;

@RestController
@Slf4j
@Validated
@RequestMapping("/films")
public class FilmController {

    private final FilmService service;

    // @Autowired
    public FilmController(FilmService service) {
        this.service = service;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return service.getAllFilms();
    }

    @GetMapping("/{filmId}")
    public Film getFilmById(@Valid @PathVariable Integer filmId) {
        return service.getFilmById(filmId);
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        isValid(film);
        return service.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        isValid(film);
        return service.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@Valid @PathVariable Integer id, @Valid @PathVariable Integer userId) {
        service.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void remoteLike(@PathVariable Integer id, @PathVariable Integer userId) {
        service.remoteLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getMostPopular(@Positive @RequestParam(defaultValue = "10") Integer count) {
        return service.getMostPopular(count);
    }

    private void isValid(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Передан запрос POST с некорректным данными фильима {}.", film);
            throw new ValidationException("Некорректные данные фильма.");
        }
    }
}
