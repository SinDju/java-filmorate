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
        Film film = Film.builder()
                .name(" ")
                .id(1)
                .description("Какой-то очень интересный фильм:)")
                .releaseDate(LocalDate.of(1997, 12,9))
                .duration(160)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size(), "Name is not empty");
    }

    @Test
    void validateEmptyDescription() {
        Film film = Film.builder()
                .name("D")
                .id(1)
                .description(" ")
                .releaseDate(LocalDate.of(1997, 12,9))
                .duration(160)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size(), "Description is correct");
    }

    @Test
    void validateIncorrectReleaseDate() {
        Film film = Film.builder()
                .name("D")
                .id(1)
                .description("Фильм вышедший в другой реальности, так как у нас кино зародилось 28.12.1895")
                .releaseDate(LocalDate.of(1890, 12, 27))
                .duration(160)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size(), "ReleaseDate is correct");
    }

    @Test
    void validateNegativeDuration() {
        Film film = Film.builder()
                .name("Я")
                .id(1)
                .description("Какой-то очень интересный фильм:)")
                .releaseDate(LocalDate.of(1997, 12,9))
                .duration(-160)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size(), "Duration is positive");
    }

}


