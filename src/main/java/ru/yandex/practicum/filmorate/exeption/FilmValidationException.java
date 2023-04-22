package ru.yandex.practicum.filmorate.exeption;

public class FilmValidationException extends RuntimeException {
    public FilmValidationException(String message) {
        super(message);
    }
}
