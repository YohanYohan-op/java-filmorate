package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.services.FilmService;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FilmControllerTest {

    @Mock
    private FilmService filmService;

    @InjectMocks
    private FilmController filmController;

    private Film film;

    @BeforeEach
    void setUp() {
        film = new Film();
        film.setId(1);
        film.setName("Test Film");
    }

    @Test
    void getAllFilmsShouldReturnCollectionOfFilms() {
        when(filmService.getAllFilms()).thenReturn(List.of(film));

        Collection<Film> films = filmController.getAllFilms();

        assertEquals(1, films.size());
        assertEquals("Test Film", films.iterator().next().getName());
        verify(filmService, times(1)).getAllFilms();
    }

    @Test
    void createFilmShouldReturnCreatedFilm() {
        when(filmService.create(film)).thenReturn(film);

        Film createdFilm = filmController.create(film);

        assertEquals(1, createdFilm.getId());
        assertEquals("Test Film", createdFilm.getName());
        verify(filmService, times(1)).create(film);
    }

    @Test
    void updateFilmShouldReturnUpdatedFilm() {
        when(filmService.update(film)).thenReturn(film);

        Film updatedFilm = filmController.update(film);

        assertEquals(1, updatedFilm.getId());
        assertEquals("Test Film", updatedFilm.getName());
        verify(filmService, times(1)).update(film);
    }

    @Test
    void addLikeShouldCallFilmServiceAddLike() {
        int filmId = 1;
        int userId = 2;

        filmController.addLike(filmId, userId);

        verify(filmService, times(1)).addLike(filmId, userId);
    }

    @Test
    void removeLikeShouldCallFilmServiceRemoveLike() {
        int filmId = 1;
        int userId = 2;

        filmController.removeLike(filmId, userId);

        verify(filmService, times(1)).removeLike(filmId, userId);
    }

    @Test
    void getPopularFilmsShouldReturnListOfPopularFilms() {
        int count = 10;
        when(filmService.getPopularFilms(count)).thenReturn(List.of(film));

        List<Film> popularFilms = filmController.getPopularFilms(count);

        assertEquals(1, popularFilms.size());
        assertEquals("Test Film", popularFilms.get(0).getName());
        verify(filmService, times(1)).getPopularFilms(count);
    }
}
