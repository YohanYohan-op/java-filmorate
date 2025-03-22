package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    private Set<Integer> likes;

    private Integer id;
    @NotBlank
    private String name;
    @NotBlank
    @Size(min = 0, max = 200)
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @Min(1)
    @Positive
    private int duration;

    @Builder
    public Film(String name, String description, LocalDate releaseDate, int duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.likes = new HashSet<>();
    }

    public void addLike(Integer filmId) {
        likes.add(filmId);
    }

    public void remoteLike(Integer userId) {
        likes.remove(userId);
    }

}
