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
        User user = new User();
        user.setLogin(" ");
        user.setId(1);
        user.setEmail("rek@mail.ru");
        user.setBirthday(LocalDate.of(1997, 12,9));

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size(), "Login is not empty");
    }

    @Test
    void validateIncorrectEmail() {
        User user = new User();
        user.setLogin("cat");
        user.setId(1);
        user.setEmail("это-неправильный?эмейл@");
        user.setBirthday(LocalDate.of(1997, 12,9));

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size(), "Email is correct");
    }

    @Test
    void validateIncorrectBirthday() {
        User user = new User();
        user.setLogin("cat");
        user.setId(1);
        user.setEmail("rek@mail.ru");
        user.setBirthday(LocalDate.of(2997, 12,9));

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size(), "Birthday is correct");
    }

    @Test
    void validateNullBirthday() {
        User user = new User();
        user.setLogin("cat");
        user.setId(1);
        user.setEmail("rek@mail.ru");
        user.setBirthday(null);

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size(), "Correct Birthday");
    }


}
