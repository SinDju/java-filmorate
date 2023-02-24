package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ValidationFilmTest {
    private static Validator validator;
    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }

    @Test
    void validateNameIsEmpty() {
        Film film = new Film();
        film.setId(1);
        film.setName(" ");
        film.setDescription("Какой-то очень интересный фильм:)");
        film.setReleaseDate(LocalDate.of(1997, 12,9));
        film.setDuration(15);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size(), "Name is not empty");
    }

    @Test
    void validateEmptyDescription() {
        Film film = new Film();
        film.setId(1);
        film.setName("D");
        film.setDescription(" ");
        film.setReleaseDate(LocalDate.of(1997, 12,9));
        film.setDuration(15);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size(), "Description is correct");
    }

    @Test
    void validateIncorrectReleaseDate() {
        Film film = new Film();
        film.setId(1);
        film.setName("D");
        film.setDescription("Фильм вышедший в другой реальности, так как у нас кино зародилось 28.12.1895");
        film.setReleaseDate(LocalDate.of(1890, 12, 27));
        film.setDuration(15);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size(), "ReleaseDate is correct");
    }

    @Test
    void validateNegativeDuration() {
        Film film = new Film();
        film.setId(1);
        film.setName("Я");
        film.setDescription("Какой-то очень интересный фильм:)");
        film.setReleaseDate(LocalDate.of(1997, 12,9));
        film.setDuration(-25);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size(), "Duration is positive");
    }

}


