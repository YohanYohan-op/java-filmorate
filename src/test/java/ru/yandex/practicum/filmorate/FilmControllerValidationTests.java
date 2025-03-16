package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.services.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmControllerValidationTests {

    private FilmController filmController;
    private Film validFilm;
    private static final LocalDate FILM_BIRTHDAY = LocalDate.of(1895, 12, 28);

    @BeforeEach
    void setUp() {
        filmController = new FilmController(new FilmService(new InMemoryFilmStorage(), new InMemoryUserStorage()), new InMemoryFilmStorage());
        validFilm = new Film();
        validFilm.setName("Test Film");
        validFilm.setDescription("Test Description");
        validFilm.setReleaseDate(LocalDate.of(2000, 1, 1));
        validFilm.setDuration(120);
    }

    @Test
    void createValidFilm() {
        Film createdFilm = filmController.create(validFilm);
        assertNotNull(createdFilm.getId());
        assertEquals(validFilm.getName(), createdFilm.getName());
    }

    @Test
    void createFilmWhenNameIsBlank() {
        validFilm.setName(" ");
        assertThrows(ValidationException.class, () -> filmController.create(validFilm));
    }

    @Test
    void createFilmWhenNameIsNull() {
        validFilm.setName(null);
        assertThrows(ValidationException.class, () -> filmController.create(validFilm));
    }

    @Test
    void createFilmWhenDescriptionIsTooLong() {
        validFilm.setDescription("a".repeat(201));
        assertThrows(ValidationException.class, () -> filmController.create(validFilm));
    }

    @Test
    void createFilmWhenDescriptionIsNull() {
        validFilm.setDescription(null);
        assertThrows(ValidationException.class, () -> filmController.create(validFilm));
    }

    @Test
    void createFilmWhenDescriptionIs200Char() {
        validFilm.setDescription("a".repeat(200));
        Film createdFilm = filmController.create(validFilm);
        assertNotNull(createdFilm.getId());
        assertEquals(validFilm.getDescription(), createdFilm.getDescription());
    }

    @Test
    void createFilmWhenReleaseDateIsBeforeFilmBirthday() {
        validFilm.setReleaseDate(FILM_BIRTHDAY.minusDays(1));
        assertThrows(ValidationException.class, () -> filmController.create(validFilm));
    }

    @Test
    void createFilmWhenReleaseDateIsFilmBirthday() {
        validFilm.setReleaseDate(FILM_BIRTHDAY);
        Film createdFilm = filmController.create(validFilm);
        assertNotNull(createdFilm.getId());
        assertEquals(validFilm.getReleaseDate(), createdFilm.getReleaseDate());
    }

    @Test
    void createFilmWhenReleaseDateIsNull() {
        validFilm.setReleaseDate(null);
        assertThrows(ValidationException.class, () -> filmController.create(validFilm));
    }

    @Test
    void createFilmWhenDurationIsNegative() {
        validFilm.setDuration(-1);
        assertThrows(ValidationException.class, () -> filmController.create(validFilm));
    }

    @Test
    void createFilmWhenDurationIsZero() {
        validFilm.setDuration(0);
        assertThrows(ValidationException.class, () -> filmController.create(validFilm));
    }

    @Test
    void updateFilmWhenFilmIsValid() {
        Film createdFilm = filmController.create(validFilm);
        createdFilm.setName("Updated Film Name");
        Film updatedFilm = filmController.update(createdFilm);
        assertEquals("Updated Film Name", updatedFilm.getName());
    }

    @Test
    void updateFilmWhenIdIsInvalid() {
        validFilm.setId(9999);
        assertThrows(ValidationException.class, () -> filmController.update(validFilm));
    }

    @Test
    void updateFilmWhenNameIsBlank() {
        Film createdFilm = filmController.create(validFilm);
        createdFilm.setName(" ");
        assertThrows(ValidationException.class, () -> filmController.update(createdFilm));
    }

    @Test
    void updateFilmWhenNameIsNull() {
        Film createdFilm = filmController.create(validFilm);
        createdFilm.setName(null);
        assertThrows(ValidationException.class, () -> filmController.update(createdFilm));
    }

    @Test
    void updateFilmWhenDescriptionIsTooLong() {
        Film createdFilm = filmController.create(validFilm);
        createdFilm.setDescription("a".repeat(201));
        assertThrows(ValidationException.class, () -> filmController.update(createdFilm));
    }

    @Test
    void updateFilmWhenDescriptionIs200Char() {
        Film createdFilm = filmController.create(validFilm);
        createdFilm.setDescription("a".repeat(200));
        Film updatedFilm = filmController.update(createdFilm);
        assertEquals(createdFilm.getDescription(), updatedFilm.getDescription());
    }

    @Test
    void updateFilmWhenReleaseDateIsBeforeFilmBirthday() {
        Film createdFilm = filmController.create(validFilm);
        createdFilm.setReleaseDate(FILM_BIRTHDAY.minusDays(1));
        assertThrows(ValidationException.class, () -> filmController.update(createdFilm));
    }

    @Test
    void updateFilmWhenReleaseDateIsNull() {
        Film createdFilm = filmController.create(validFilm);
        createdFilm.setReleaseDate(null);
        assertThrows(ValidationException.class, () -> filmController.update(createdFilm));
    }

    @Test
    void updateFilmWhenDurationIsNegative() {
        Film createdFilm = filmController.create(validFilm);
        createdFilm.setDuration(-1);
        assertThrows(ValidationException.class, () -> filmController.update(createdFilm));
    }

    @Test
    void updateFilmWhenDurationIsZero() {
        Film createdFilm = filmController.create(validFilm);
        createdFilm.setDuration(0);
        assertThrows(ValidationException.class, () -> filmController.update(createdFilm));
    }

}
