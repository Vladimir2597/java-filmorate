package ru.yandex.practicum.filmorate.exceptions;

import org.springframework.http.HttpStatus;

public class ValidationException extends Exception {
    public ValidationException(String reason) {
        super(reason);
    }
}
