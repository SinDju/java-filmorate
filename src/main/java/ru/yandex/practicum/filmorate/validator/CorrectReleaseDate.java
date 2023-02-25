package ru.yandex.practicum.filmorate.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(ElementType.FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = CorrectReleaseDateValidator.class)
@Documented
public @interface CorrectReleaseDate {

    String message() default "{Incorrect releaseDate}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}