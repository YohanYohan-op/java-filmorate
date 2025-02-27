package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserControllerValidationTests {

    private UserController userController;
    private User validUser;

    @BeforeEach
    void setUp() {
        userController = new UserController();
        validUser = new User();
        validUser.setEmail("test@example.com");
        validUser.setLogin("testLogin");
        validUser.setName("Test User");
        validUser.setBirthday(LocalDate.of(2000, 1, 1));
    }

    @Test
    void createValidUser() {
        User createdUser = userController.create(validUser);
        assertNotNull(createdUser.getId());
        assertEquals(validUser.getEmail(), createdUser.getEmail());
    }

    @Test
    void createUserInvalidEmail() {
        validUser.setEmail("invalid-email");
        assertThrows(ValidationException.class, () -> userController.create(validUser));
    }

    @Test
    void createUserBlankEmail() {
        validUser.setEmail(" ");
        assertThrows(ValidationException.class, () -> userController.create(validUser));
    }

    @Test
    void createUserNullEmail() {
        validUser.setEmail(null);
        assertThrows(ValidationException.class, () -> userController.create(validUser));
    }

    @Test
    void createUserInvalidLogin() {
        validUser.setLogin("invalid login");
        assertThrows(ValidationException.class, () -> userController.create(validUser));
    }

    @Test
    void createUserBlankLogin() {
        validUser.setLogin(" ");
        assertThrows(ValidationException.class, () -> userController.create(validUser));
    }

    @Test
    void createUserNullLogin() {
        validUser.setLogin(null);
        assertThrows(ValidationException.class, () -> userController.create(validUser));
    }

    @Test
    void createUserFutureBirthday() {
        validUser.setBirthday(LocalDate.now().plusDays(1));
        assertThrows(ValidationException.class, () -> userController.create(validUser));
    }

    @Test
    void createUserTooOldBirthday() {
        validUser.setBirthday(LocalDate.of(1900, 1, 1));
        assertThrows(ValidationException.class, () -> userController.create(validUser));
    }

    @Test
    void updateUserValidUser() {
        User createdUser = userController.create(validUser);
        createdUser.setName("Updated Name");
        User updatedUser = userController.update(createdUser);
        assertEquals("Updated Name", updatedUser.getName());
    }

    @Test
    void updateUserInvalidEmail() {
        User createdUser = userController.create(validUser);
        createdUser.setEmail("invalid-email");
        assertThrows(ValidationException.class, () -> userController.update(createdUser));
    }

    @Test
    void updateUserInvalidId() {
        validUser.setId(9999);
        assertThrows(ValidationException.class, () -> userController.update(validUser));
    }

    @Test
    void updateUserPastBirthday() {
        User createdUser = userController.create(validUser);
        createdUser.setBirthday(LocalDate.of(1900, 01, 01));
        assertThrows(ValidationException.class, () -> userController.update(createdUser));
    }

    @Test
    void updateUserFutureBirthday() {
        User createdUser = userController.create(validUser);
        createdUser.setBirthday(LocalDate.now().plusDays(1));
        assertThrows(ValidationException.class, () -> userController.update(createdUser));
    }
}
