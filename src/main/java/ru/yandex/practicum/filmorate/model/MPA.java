package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MPA {
    private int id;
    @NotBlank(message = "Название рейтинга MPA не может быть пустым")
    private String name;
}
