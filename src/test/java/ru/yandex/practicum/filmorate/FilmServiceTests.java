package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.services.FilmService;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FilmServiceTests {

    @Mock
    private FilmStorage filmStorage;

    @Mock
    private UserStorage userStorage;

    @InjectMocks
    private FilmService filmService;

    private Film film;
    private User user;

    @BeforeEach
    void setUp() {
        film = new Film();
        film.setId(1);
        film.setName("Test Film");

        user = new User();
        user.setId(1);
        user.setEmail("test@example.com");
    }

    @Test
    void addLikeShouldAddLikeToFilm() {
        when(filmStorage.getFilmById(1)).thenReturn(Optional.of(film));
        when(userStorage.getUserById(1)).thenReturn(Optional.of(user));
        when(filmStorage.update(film)).thenReturn(film);
        filmService.addLike(1, 1);

        assertTrue(film.getLikeScore().contains(1));
        verify(filmStorage, times(1)).update(film);
    }

    @Test
    void addLikeShouldThrowNotFoundExceptionWhenFilmNotFound() {
        when(filmStorage.getFilmById(1)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> filmService.addLike(1, 1));
    }

    @Test
    void addLikeShouldThrowNotFoundExceptionWhenUserNotFound() {
        when(filmStorage.getFilmById(1)).thenReturn(Optional.of(film));
        when(userStorage.getUserById(1)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> filmService.addLike(1, 1));
    }

    @Test
    void addLikeShouldThrowValidationExceptionWhenIdIsInvalid() {
        assertThrows(ValidationException.class, () -> filmService.addLike(-1, 1));
        assertThrows(ValidationException.class, () -> filmService.addLike(1, -1));
    }

    @Test
    void removeLikeShouldRemoveLikeFromFilm() {
        film.addLike(1);
        when(filmStorage.getFilmById(1)).thenReturn(Optional.of(film));
        when(userStorage.getUserById(1)).thenReturn(Optional.of(user));
        when(filmStorage.update(film)).thenReturn(film);
        filmService.removeLike(1, 1);

        assertFalse(film.getLikeScore().contains(1));
        verify(filmStorage, times(1)).update(film);
    }

    @Test
    void getPopularFilmsShouldReturnSortedListOfFilms() {
        Film film2 = new Film();
        film2.setId(2);
        film2.setName("Film 2");
        film2.addLike(1);
        film2.addLike(2);

        film.addLike(1);

        when(filmStorage.getAllFilms()).thenReturn(Arrays.asList(film, film2));

        List<Film> popularFilms = filmService.getPopularFilms(2);

        assertEquals(2, popularFilms.size());
        assertEquals(2, popularFilms.get(0).getId());
        assertEquals(1, popularFilms.get(1).getId());
    }

    @Test
    void getPopularFilmsShouldThrowValidationExceptionWhenCountIsInvalid() {
        assertThrows(ValidationException.class, () -> filmService.getPopularFilms(-1));
    }

    @Test
    void getAllFilmsShouldReturnAllFilmsFromStorage() {
        List<Film> films = Arrays.asList(film, new Film());
        when(filmStorage.getAllFilms()).thenReturn(films);

        Collection<Film> result = filmService.getAllFilms();

        assertEquals(2, result.size());
        verify(filmStorage, times(1)).getAllFilms();
    }

    @Test
    void createFilmShouldCreateFilmInStorage() {
        when(filmStorage.create(film)).thenReturn(film);

        Film createdFilm = filmService.create(film);

        assertEquals(1, createdFilm.getId());
        verify(filmStorage, times(1)).create(film);
    }

    @Test
    void updateFilmShouldUpdateFilmInStorage() {
        when(filmStorage.getFilmById(1)).thenReturn(Optional.of(film));
        when(filmStorage.update(film)).thenReturn(film);

        Film updatedFilm = filmService.update(film);

        assertEquals(1, updatedFilm.getId());
        verify(filmStorage, times(1)).update(film);
    }

    @Test
    void updateFilmShouldThrowNotFoundExceptionWhenFilmNotFound() {
        when(filmStorage.getFilmById(1)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> filmService.update(film));
    }
}
