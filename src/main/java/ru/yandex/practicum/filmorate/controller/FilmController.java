package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;


import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> getAll() {
        log.info("Получен запрос GET к эндпоинту: /films");
        return filmService.getAllFilms();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Получен запрос POST. Данные тела запроса: {}", film);
        Film createFilm = filmService.createFilm(film);
        log.info("Создан объект {} с идентификатором {}", Film.class.getSimpleName(), createFilm.getId());
        return createFilm;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Получен запрос PUT. Данные тела запроса: {}", film);
        Film updateFilm = filmService.updateFilm(film);
        log.info("Обновлен объект {} с идентификатором {}", Film.class.getSimpleName(), updateFilm.getId());
        return updateFilm;
    }

    // все верхние методы переделать

    @GetMapping("/{id}") // получение фильма по id
    public Film findById(@PathVariable String id){
        log.info("Получен запрос GET к эндпоинту: /films/{}/", id);
        return filmService.findById(id);
    }

    @PutMapping("/{id}/like/{userId}") // пользователь ставит лайк фильму(надо добавить бин ЮзерСтородж?)
    public void addLike(@PathVariable String id, @PathVariable String userId) {
        log.info("Получен запрос PUT к эндпоинту: /films/{}/like/{}", id, userId);
        filmService.addLike(id, userId);
        log.info("Обновлен объект {} с идентификатором {}, добавлен лайк от пользователя {}",
                Film.class.getSimpleName(), id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}") // пользователь удаляет лайк
    public void deleteLike(@PathVariable String id, @PathVariable String userId) {
        log.info("Получен запрос DELETE к эндпоинту: films/{}/like/{}", id, userId);
        filmService.deleteLike(id, userId);
        log.info("Обновлен объект {} с идентификатором {}, удален лайк от пользователя {}",
                Film.class.getSimpleName(), id, userId);
    }

    @GetMapping("/popular") // ?count={count} - возвращает список из первых count фильмов по количеству лайков.
    // Если значение параметра count не задано, верните первые 10. Film count?
    public Collection<Film> getPopularFilm(@RequestParam(defaultValue = "10") String count) {
       log.info("Получен запрос GET к эндпоинту: /popular?count={}", count);
        return  filmService.getPopularFilm(count);
    }
}
