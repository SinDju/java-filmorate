package ru.yandex.practicum.filmorate.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class CorrectReleaseDateValidator implements ConstraintValidator<CorrectReleaseDate, LocalDate> {

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext validatorContext) {
        LocalDate birthdayMovie = LocalDate.of(1895, 12, 28);
       if (date.isEqual(birthdayMovie)) {
            return true;
        }
        return !date.isBefore(birthdayMovie);
    }
}