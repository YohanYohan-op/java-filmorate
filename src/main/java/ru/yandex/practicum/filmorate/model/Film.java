package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    @EqualsAndHashCode.Include
    private int id;
    @EqualsAndHashCode.Exclude
    private String name;
    @EqualsAndHashCode.Exclude
    private String description;
    @EqualsAndHashCode.Exclude
    private LocalDate releaseDate;
    @EqualsAndHashCode.Exclude
    private int duration;
    @EqualsAndHashCode.Exclude
    private Set<Integer> likeScore = new HashSet<>(); //Инициализация по умолчанию

    public void addLike(int userId) {
        likeScore.add(userId);
    }

    public void removeLike(int userId) {
        likeScore.remove(userId);
    }
}

