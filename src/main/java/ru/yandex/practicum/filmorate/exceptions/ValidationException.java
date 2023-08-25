package ru.yandex.practicum.filmorate.exceptions;

public class ValidationException extends Exception {
    public ValidationException(String reason) {
        super(reason);
    }
}
