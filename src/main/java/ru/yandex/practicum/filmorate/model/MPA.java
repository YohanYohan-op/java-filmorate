package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MPA {
    private int id;
    @NotBlank(message = "Название рейтинга MPA не может быть пустым")
    private String name;

    public MPA(int mpaId, String mpaName) {
        this.id = mpaId;
        this.name = mpaName;
    }
}
