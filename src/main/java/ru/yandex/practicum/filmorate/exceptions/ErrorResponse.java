package ru.yandex.practicum.filmorate.exceptions;

import lombok.Data;

@Data
public class ErrorResponse {
    private String error;
    private String description;

    public ErrorResponse(String error, String description) {
        this.error = error;
        this.description = description;
    }

}
