package ru.yandex.practicum.filmorate.exeption;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.model.ErrorMessage;

@RestControllerAdvice("ru.yandex.practicum.filmorate")
@Slf4j
public class ErrorHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {FilmValidationException.class, UserValidationException.class, ErrorIdException.class})
    public ErrorMessage handleException(RuntimeException exception) {
        return  new ErrorMessage(
                "Ошибка запроса: {}",
                exception.getMessage()
        );
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {NotFoundException.class})
    public ErrorMessage handleNotFoundException(RuntimeException exception) {
        return  new ErrorMessage(
                "Ошибка запроса: {}",
                exception.getMessage()
        );
    }
}
