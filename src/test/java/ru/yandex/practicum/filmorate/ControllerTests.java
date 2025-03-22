package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.services.FilmService;
import ru.yandex.practicum.filmorate.services.UserService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ControllerTests {

    @Mock
    private FilmService filmService;

    @Mock
    private UserService userService;

    @InjectMocks
    private FilmController filmController;

    @InjectMocks
    private UserController userController;

    private Film validFilm;
    private User validUser;

    @BeforeEach
    void setUp() {
        validFilm = new Film("Test Film", "Test Description", LocalDate.of(2000, 1, 1), 120);
        validUser = new User();
        validUser.setName("Test Name");
        validUser.setEmail("test@example.com");
        validUser.setLogin("testLogin");
        validUser.setBirthday(LocalDate.of(1990, 1, 1));
    }

    // FilmController Tests

    @Test
    void getAllFilms_ReturnsListOfFilms() {
        List<Film> films = new ArrayList<>();
        films.add(validFilm);
        when(filmService.getAllFilms()).thenReturn(films);

        List<Film> result = filmController.getAllFilms();

        assertEquals(1, result.size());
        assertEquals(validFilm, result.get(0));
    }

    @Test
    void getFilmById_ValidId_ReturnsFilm() {
        when(filmService.getFilmById(1)).thenReturn(validFilm);

        Film result = filmController.getFilmById(1);

        assertEquals(validFilm, result);
    }

    @Test
    void createFilm_ValidFilm_ReturnsCreatedFilm() {
        when(filmService.addFilm(any(Film.class))).thenReturn(validFilm);

        Film result = filmController.createFilm(validFilm);

        assertEquals(validFilm, result);
    }

    @Test
    void createFilm_InvalidReleaseDate_ThrowsValidationException() {
        Film invalidFilm = new Film("Test Film", "Test Description", LocalDate.of(1895, 12, 27), 120);

        assertThrows(ValidationException.class, () -> filmController.createFilm(invalidFilm));
    }

    @Test
    void updateFilm_ValidFilm_ReturnsUpdatedFilm() {
        when(filmService.updateFilm(any(Film.class))).thenReturn(validFilm);

        Film result = filmController.updateFilm(validFilm);

        assertEquals(validFilm, result);
    }

    @Test
    void updateFilm_InvalidReleaseDate_ThrowsValidationException() {
        Film invalidFilm = new Film("Test Film", "Test Description", LocalDate.of(1895, 12, 27), 120);
        invalidFilm.setId(1);
        assertThrows(ValidationException.class, () -> filmController.updateFilm(invalidFilm));
    }

    @Test
    void addLike_ValidData_CallsService() {
        filmController.addLike(1, 1);
        verify(filmService, times(1)).addLike(1, 1);
    }

    @Test
    void remoteLike_ValidData_CallsService() {
        filmController.remoteLike(1, 1);
        verify(filmService, times(1)).remoteLike(1, 1);
    }

    @Test
    void getMostPopular_ValidCount_ReturnsListOfFilms() {
        List<Film> films = new ArrayList<>();
        films.add(validFilm);
        when(filmService.getMostPopular(10)).thenReturn(films);

        List<Film> result = filmController.getMostPopular(10);

        assertEquals(1, result.size());
        assertEquals(validFilm, result.get(0));
    }

    // UserController Tests

    @Test
    void gettingAllUsers_ReturnsListOfUsers() {
        List<User> users = new ArrayList<>();
        users.add(validUser);
        when(userService.getAllUsers()).thenReturn(users);

        List<User> result = userController.gettingAllUsers();

        assertEquals(1, result.size());
        assertEquals(validUser, result.get(0));
    }

    @Test
    void getUserById_ValidId_ReturnsUser() {
        when(userService.getUserById(1)).thenReturn(validUser);

        User result = userController.getUserById(1);

        assertEquals(validUser, result);
    }

    @Test
    void createUser_ValidUser_ReturnsCreatedUser() {
        when(userService.createUser(any(User.class))).thenReturn(validUser);

        User result = userController.createUser(validUser);

        assertEquals(validUser, result);
    }

    @Test
    void createUser_InvalidLogin_ThrowsValidationException() {
        User invalidUser = new User();
        invalidUser.setName("Test User");
        invalidUser.setEmail("test@example.com");
        invalidUser.setLogin("test Login");
        invalidUser.setBirthday(LocalDate.of(1990, 1, 1));

        assertThrows(ValidationException.class, () -> userController.createUser(invalidUser));
    }

    @Test
    void createUser_NullName_NameEqualsLogin() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("testLogin");
        user.setName(null);
        user.setBirthday(LocalDate.of(1990, 1, 1));

        when(userService.createUser(any(User.class))).thenReturn(user);
        User result = userController.createUser(user);
        assertEquals(user.getLogin(), result.getName());
    }

    @Test
    void updateUser_ValidUser_ReturnsUpdatedUser() {
        when(userService.updateUser(any(User.class))).thenReturn(validUser);

        User result = userController.updateUser(validUser);

        assertEquals(validUser, result);
    }

    @Test
    void updateUser_InvalidLogin_ThrowsValidationException() {
        User invalidUser = new User();
        invalidUser.setId(1);
        invalidUser.setName("Test User");
        invalidUser.setEmail("test@example.com");
        invalidUser.setLogin("test Login");
        invalidUser.setBirthday(LocalDate.of(1990, 1, 1));
        assertThrows(ValidationException.class, () -> userController.updateUser(invalidUser));
    }

    @Test
    void addFriend_ValidData_CallsService() {
        userController.addFriend(1, 2);
        verify(userService, times(1)).addFriend(1, 2);
    }

    @Test
    void removeFriend_ValidData_CallsService() {
        userController.removeFriend(1, 2);
        verify(userService, times(1)).removeFriend(1, 2);
    }

    @Test
    void getFriends_ValidId_ReturnsListOfFriends() {
        List<User> friends = new ArrayList<>();
        friends.add(validUser);
        when(userService.getFriends(1)).thenReturn(friends);

        List<User> result = userController.getFriends(1);

        assertEquals(1, result.size());
        assertEquals(validUser, result.get(0));
    }

    @Test
    void getCrossFriends_ValidIds_ReturnsListOfCommonFriends() {
        List<User> commonFriends = new ArrayList<>();
        commonFriends.add(validUser);
        when(userService.getCrossFriends(1, 2)).thenReturn(commonFriends);

        List<User> result = userController.getCrossFriends(1, 2);

        assertEquals(1, result.size());
        assertEquals(validUser, result.get(0));
    }
}
