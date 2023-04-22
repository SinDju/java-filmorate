package ru.yandex.practicum.filmorate.exeption;

public class UserValidationException extends RuntimeException {
    public UserValidationException(String message) {
        super(message);
    }
}
