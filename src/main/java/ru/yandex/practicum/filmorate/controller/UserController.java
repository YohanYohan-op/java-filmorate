package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.services.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.Collection;
import java.util.Set;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final UserStorage storage;

    public UserController(UserService userService, InMemoryUserStorage storage) {
        this.userService = userService;
        this.storage = storage;
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        return storage.getAllUsers();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return storage.create(user);
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        return storage.update(newUser);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public Set<Integer> addFriends(@PathVariable int id, @PathVariable int friendId){
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public Set<Integer> deleteFriends(@PathVariable int id, @PathVariable int friendId){
        return userService.deleteFromFriends(id, friendId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Set<Integer> getMutualFriends(@PathVariable int id, @PathVariable int otherId){
        return userService.mutualFriends(id, otherId);
    }
    @GetMapping("/{id}/friends")
    public Set<Integer> getFriends(@PathVariable int id){
        return userService.getFriends(id);
    }


}
