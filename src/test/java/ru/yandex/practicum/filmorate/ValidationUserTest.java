package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ValidationUserTest {
    private static Validator validator;
    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }


    @Test
    void validateLoginIsEmpty() {
        User user = User.builder()
                .login(" ")
                .name("O")
                .id(1)
                .email("rek@mail.ru")
                .birthday(LocalDate.of(1997, 12,9))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size(), "Login is not empty");
    }

    @Test
    void validateLoginIsWhitespace() {
        User user = User.builder()
                .login("A m i K o")
                .id(1)
                .email("rek@mail.ru")
                .birthday(LocalDate.of(1997, 12,9))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size(), "The login contains a space");
    }

    @Test
    void validateIncorrectEmail() {
        User user = User.builder()
                .login("cat")
                .id(1)
                .email("это-неправильный?эмейл@")
                .birthday(LocalDate.of(1997, 12,9))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size(), "Email is correct");
    }

    @Test
    void validateIncorrectBirthday() {
        User user = User.builder()
                .login("cat")
                .id(1)
                .email("rek@mail.ru")
                .birthday(LocalDate.of(2997, 12,9))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size(), "Birthday is correct");
    }

    @Test
    void validateNullBirthday() {
        User user = User.builder()
                .login("cat")
                .id(1)
                .email("rek@mail.ru")
                .birthday(null)
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size(), "Correct Birthday");
    }


}
