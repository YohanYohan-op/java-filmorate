package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.services.UserService;

import java.util.Collection;
import java.util.Set;

@RestController
@Slf4j
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public Collection<User> getAllUsers() {
        return userService.getUserStorage().getAllUsers();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return userService.getUserStorage().create(user);
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        return userService.getUserStorage().update(newUser);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public Set<Integer> addFriends(@PathVariable int id, @PathVariable int friendId) {
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public Set<Integer> deleteFriends(@PathVariable int id, @PathVariable int friendId) {
        return userService.deleteFromFriends(id, friendId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Set<Integer> getMutualFriends(@PathVariable int id, @PathVariable int otherId) {
        return userService.mutualFriends(id, otherId);
    }

    @GetMapping("/{id}/friends")
    public Set<Integer> getFriends(@PathVariable int id) {
        return userService.getFriends(id);
    }
}
