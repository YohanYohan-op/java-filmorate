package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    @EqualsAndHashCode.Include
    private int id;
    @EqualsAndHashCode.Exclude
    private String email;
    @EqualsAndHashCode.Exclude
    private String login;
    @EqualsAndHashCode.Exclude
    private String name;
    @EqualsAndHashCode.Exclude
    private LocalDate birthday;
    @EqualsAndHashCode.Exclude
    private Set<Integer> friendsList = new HashSet<>();  //Инициализация по умолчанию

    public void addFriend(int friendId) {
        friendsList.add(friendId);
    }

    public void removeFriend(int friendId) {
        friendsList.remove(friendId);
    }
}
