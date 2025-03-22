package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.services.FilmService;
import ru.yandex.practicum.filmorate.services.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class FilmorateTests {

    private InMemoryFilmStorage filmStorage;
    private InMemoryUserStorage userStorage;
    private FilmService filmService;
    private UserService userService;

    @BeforeEach
    public void setUp() {
        filmStorage = new InMemoryFilmStorage();
        userStorage = new InMemoryUserStorage();
        filmService = new FilmService(filmStorage, userStorage);
        userService = new UserService(userStorage);
    }

    @Test
    void testCreateFilm_ValidFilm_ShouldCreateFilm() {
        Film film = new Film();
        film.setName("Film Name");
        film.setDescription("Film Description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);
        film.setLikeScore(new HashSet<>());

        Film createdFilm = filmStorage.create(film);
        assertNotNull(createdFilm);
        assertEquals(1, createdFilm.getId());
        assertEquals("Film Name", createdFilm.getName());
        assertEquals(1, filmStorage.getFilms().size());
    }

    @Test
    void testCreateFilm_InvalidName_ShouldThrowValidationException() {
        Film film = new Film();
        film.setName("");
        film.setDescription("Film Description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);
        film.setLikeScore(new HashSet<>());
        assertThrows(ValidationException.class, () -> filmStorage.create(film));
    }

    @Test
    void testCreateFilm_InvalidDescription_ShouldThrowValidationException() {
        Film film = new Film();
        film.setName("Film Name");
        film.setDescription("a".repeat(201));
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);
        film.setLikeScore(new HashSet<>());
        assertThrows(ValidationException.class, () -> filmStorage.create(film));
    }

    @Test
    void testCreateFilm_InvalidReleaseDate_ShouldThrowValidationException() {
        Film film = new Film();
        film.setName("Film Name");
        film.setDescription("Film Description");
        film.setReleaseDate(LocalDate.of(1890, 1, 1));
        film.setDuration(120);
        film.setLikeScore(new HashSet<>());
        assertThrows(ValidationException.class, () -> filmStorage.create(film));
    }

    @Test
    void testCreateFilm_InvalidDuration_ShouldThrowValidationException() {
        Film film = new Film();
        film.setName("Film Name");
        film.setDescription("Film Description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(-120);
        film.setLikeScore(new HashSet<>());
        assertThrows(ValidationException.class, () -> filmStorage.create(film));
    }

    @Test
    void testUpdateFilm_ValidFilm_ShouldUpdateFilm() {
        Film film = new Film();
        film.setName("Film Name");
        film.setDescription("Film Description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);
        film.setLikeScore(new HashSet<>());
        filmStorage.create(film);

        Film updatedFilm = new Film();
        updatedFilm.setId(1);
        updatedFilm.setName("Updated Film Name");
        updatedFilm.setDescription("Updated Film Description");
        updatedFilm.setReleaseDate(LocalDate.of(2001, 1, 1));
        updatedFilm.setDuration(150);
        updatedFilm.setLikeScore(new HashSet<>());

        Film result = filmStorage.update(updatedFilm);
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Updated Film Name", result.getName());
        assertEquals("Updated Film Description", result.getDescription());
        assertEquals(LocalDate.of(2001, 1, 1), result.getReleaseDate());
        assertEquals(150, result.getDuration());
    }

    @Test
    void testUpdateFilm_InvalidId_ShouldThrowNotFoundException() {
        Film film = new Film();
        film.setName("Film Name");
        film.setDescription("Film Description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);
        filmStorage.create(film);

        Film updatedFilm = new Film();
        updatedFilm.setId(99);
        updatedFilm.setName("Updated Film Name");
        updatedFilm.setDescription("Updated Film Description");
        updatedFilm.setReleaseDate(LocalDate.of(2001, 1, 1));
        updatedFilm.setDuration(150);
        assertThrows(ValidationException.class, () -> filmStorage.update(updatedFilm));
    }

    @Test
    void testUpdateFilm_InvalidData_ShouldThrowValidationException() {
        Film film = new Film();
        film.setName("Film Name");
        film.setDescription("Film Description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);
        film.setLikeScore(new HashSet<>());
        filmStorage.create(film);

        Film updatedFilm = new Film();
        updatedFilm.setId(1);
        updatedFilm.setName("");
        updatedFilm.setDescription("Updated Film Description");
        updatedFilm.setReleaseDate(LocalDate.of(2001, 1, 1));
        updatedFilm.setDuration(150);
        updatedFilm.setLikeScore(new HashSet<>());
        assertThrows(ValidationException.class, () -> filmStorage.update(updatedFilm));
    }

    @Test
    void testGetAllFilms_ShouldReturnAllFilms() {
        Film film1 = new Film();
        film1.setName("Film 1");
        film1.setDescription("Description 1");
        film1.setReleaseDate(LocalDate.of(2000, 1, 1));
        film1.setDuration(120);
        film1.setLikeScore(new HashSet<>());

        Film film2 = new Film();
        film2.setName("Film 2");
        film2.setDescription("Description 2");
        film2.setReleaseDate(LocalDate.of(2001, 1, 1));
        film2.setDuration(130);
        film2.setLikeScore(new HashSet<>());

        filmStorage.create(film1);
        filmStorage.create(film2);
        Collection<Film> films = filmStorage.getAllFilms();
        assertEquals(2, films.size());
    }

    @Test
    void testDeleteFilm_ValidFilm_ShouldDeleteFilm() {
        Film film = new Film();
        film.setName("Film Name");
        film.setDescription("Film Description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);
        film.setLikeScore(new HashSet<>());
        filmStorage.create(film);
        film.setId(1);
        filmStorage.delete(film);
        assertEquals(0, filmStorage.getFilms().size());
    }

    @Test
    void testDeleteFilm_InvalidFilm_ShouldThrowValidationException() {
        Film film = new Film();
        film.setName("Film Name");
        film.setDescription("Film Description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);
        film.setLikeScore(new HashSet<>());
        assertThrows(ValidationException.class, () -> filmStorage.delete(film));
    }

    // UserService Tests
    @Test
    void testAddFriend_ValidUsers_ShouldAddFriend() {
        User user1 = new User();
        user1.setEmail("user1@example.com");
        user1.setLogin("user1login");
        user1.setName("User 1");
        user1.setBirthday(LocalDate.of(1990, 1, 1));
        user1.setFriendsList(new HashSet<>());

        User user2 = new User();
        user2.setEmail("user2@example.com");
        user2.setLogin("user2login");
        user2.setName("User 2");
        user2.setBirthday(LocalDate.of(1991, 1, 1));
        user2.setFriendsList(new HashSet<>());

        userStorage.create(user1);
        userStorage.create(user2);
        Set<Integer> friends = userService.addFriend(1, 2);
        assertEquals(1, friends.size());
        assertTrue(friends.contains(2));
    }

    @Test
    void testAddFriend_SameUser_ShouldThrowNotFoundException() {
        User user1 = new User();
        user1.setEmail("user1@example.com");
        user1.setLogin("user1login");
        user1.setName("User 1");
        user1.setBirthday(LocalDate.of(1990, 1, 1));
        user1.setFriendsList(new HashSet<>());
        userStorage.create(user1);
        assertThrows(NotFoundException.class, () -> userService.addFriend(1, 1));
    }

    @Test
    void testAddFriend_UserNotFound_ShouldThrowNotFoundException() {
        User user1 = new User();
        user1.setEmail("user1@example.com");
        user1.setLogin("user1login");
        user1.setName("User 1");
        user1.setBirthday(LocalDate.of(1990, 1, 1));
        user1.setFriendsList(new HashSet<>());
        userStorage.create(user1);
        assertThrows(NotFoundException.class, () -> userService.addFriend(1, 99));
    }

    @Test
    void testDeleteFromFriends_ValidUsers_ShouldDeleteFriend() {
        User user1 = new User();
        user1.setEmail("user1@example.com");
        user1.setLogin("user1login");
        user1.setName("User 1");
        user1.setBirthday(LocalDate.of(1990, 1, 1));
        user1.setFriendsList(new HashSet<>());

        User user2 = new User();
        user2.setEmail("user2@example.com");
        user2.setLogin("user2login");
        user2.setName("User 2");
        user2.setBirthday(LocalDate.of(1991, 1, 1));
        user2.setFriendsList(new HashSet<>());

        userStorage.create(user1);
        userStorage.create(user2);
        userService.addFriend(1, 2);
        Set<Integer> friends = userService.deleteFromFriends(1, 2);
        assertEquals(0, friends.size());
    }

    @Test
    void testDeleteFromFriends_SameUser_ShouldThrowNotFoundException() {
        User user1 = new User();
        user1.setEmail("user1@example.com");
        user1.setLogin("user1login");
        user1.setName("User 1");
        user1.setBirthday(LocalDate.of(1990, 1, 1));
        user1.setFriendsList(new HashSet<>());
        userStorage.create(user1);
        assertThrows(NotFoundException.class, () -> userService.deleteFromFriends(1, 1));
    }

    @Test
    void testDeleteFromFriends_UserNotFound_ShouldThrowNotFoundException() {
        User user1 = new User();
        user1.setEmail("user1@example.com");
        user1.setLogin("user1login");
        user1.setName("User 1");
        user1.setBirthday(LocalDate.of(1990, 1, 1));
        user1.setFriendsList(new HashSet<>());
        userStorage.create(user1);
        assertThrows(NotFoundException.class, () -> userService.deleteFromFriends(1, 99));
    }

    @Test
    void testMutualFriends_ValidUsers_ShouldReturnMutualFriends() {
        User user1 = new User();
        user1.setEmail("user1@example.com");
        user1.setLogin("user1login");
        user1.setName("User 1");
        user1.setBirthday(LocalDate.of(1990, 1, 1));
        user1.setFriendsList(new HashSet<>());

        User user2 = new User();
        user2.setEmail("user2@example.com");
        user2.setLogin("user2login");
        user2.setName("User 2");
        user2.setBirthday(LocalDate.of(1991, 1, 1));
        user2.setFriendsList(new HashSet<>());

        User user3 = new User();
        user3.setEmail("user3@example.com");
        user3.setLogin("user3login");
        user3.setName("User 3");
        user3.setBirthday(LocalDate.of(1992, 1, 1));
        user3.setFriendsList(new HashSet<>());

        userStorage.create(user1);
        userStorage.create(user2);
        userStorage.create(user3);
        userService.addFriend(1, 3);
        userService.addFriend(2, 3);
        Set<Integer> mutualFriends = userService.mutualFriends(1, 2);
        assertEquals(1, mutualFriends.size());
        assertTrue(mutualFriends.contains(3));
    }

    @Test
    void testMutualFriends_SameUser_ShouldThrowNotFoundException() {
        User user1 = new User();
        user1.setEmail("user1@example.com");
        user1.setLogin("user1login");
        user1.setName("User 1");
        user1.setBirthday(LocalDate.of(1990, 1, 1));
        user1.setFriendsList(new HashSet<>());
        userStorage.create(user1);
        assertThrows(NotFoundException.class, () -> userService.mutualFriends(1, 1));
    }

    @Test
    void testMutualFriends_UserNotFound_ShouldThrowNotFoundException() {
        User user1 = new User();
        user1.setEmail("user1@example.com");
        user1.setLogin("user1login");
        user1.setName("User 1");
        user1.setBirthday(LocalDate.of(1990, 1, 1));
        user1.setFriendsList(new HashSet<>());
        userStorage.create(user1);
        assertThrows(NotFoundException.class, () -> userService.mutualFriends(1, 99));
    }

    @Test
    void testGetFriends_ValidUser_ShouldReturnFriends() {
        User user1 = new User();
        user1.setEmail("user1@example.com");
        user1.setLogin("user1login");
        user1.setName("User 1");
        user1.setBirthday(LocalDate.of(1990, 1, 1));
        user1.setFriendsList(new HashSet<>());

        User user2 = new User();
        user2.setEmail("user2@example.com");
        user2.setLogin("user2login");
        user2.setName("User 2");
        user2.setBirthday(LocalDate.of(1991, 1, 1));
        user2.setFriendsList(new HashSet<>());

        userStorage.create(user1);
        userStorage.create(user2);
        userService.addFriend(1, 2);
        Set<Integer> friends = userService.getFriends(1);
        assertEquals(1, friends.size());
        assertTrue(friends.contains(2));
    }

    @Test
    void testGetTenTheMostPopularFilms_ShouldReturnSortedFilms() {
        Film film1 = new Film();
        film1.setName("Film 1");
        film1.setDescription("Description 1");
        film1.setReleaseDate(LocalDate.of(2000, 1, 1));
        film1.setDuration(120);
        Film film2 = new Film();
        film2.setName("Film 2");
        film2.setDescription("Description 2");
        film2.setReleaseDate(LocalDate.of(2001, 1, 1));
        film2.setDuration(130);
        Film film3 = new Film();
        film3.setName("Film 3");
        film3.setDescription("Description 3");
        film3.setReleaseDate(LocalDate.of(2002, 1, 1));
        film3.setDuration(140);
        User user1 = new User();
        user1.setName("User 1");
        user1.setBirthday(LocalDate.of(1990, 1, 1));
        user1.setEmail("user1@example.com");
        user1.setLogin("user1login");
        User user2 = new User();
        user2.setName("User 2");
        user2.setBirthday(LocalDate.of(1991, 1, 1));
        user2.setEmail("user2@example.com");
        user2.setLogin("user2login");
        filmStorage.create(film1);
        filmStorage.create(film2);
        filmStorage.create(film3);
        userStorage.create(user1);
        userStorage.create(user2);
        filmService.addLike(1, 1, true);
        filmService.addLike(2, 1, true);
        filmService.addLike(2, 2, true);
        List<Film> popularFilms = filmService.getTenTheMostPopularFilms(10);
        assertEquals(3, popularFilms.size());
        assertEquals("Film 2", popularFilms.get(0).getName());
        assertEquals("Film 1", popularFilms.get(1).getName());
        assertEquals("Film 3", popularFilms.get(2).getName());
    }

    @Test
    void testGetTenTheMostPopularFilms_InvalidCount_ShouldThrowValidationException() {
        assertThrows(ValidationException.class, () -> filmService.getTenTheMostPopularFilms(-1));
    }
}
