package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private HashMap<Integer, Film> filmsMap = new HashMap<>();
    private int generatoreId = 0;

    @GetMapping
    public List<Film> getAllFilms() {
        return new ArrayList<>(filmsMap.values());
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
            final int id = ++generatoreId;
            film.setId(id);
            filmsMap.put(id, film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        if (filmsMap.containsKey(film.getId())){
            filmsMap.put(film.getId(), film);
        } else {
            throw new ValidationException("Фильм еще не зарегестрирован");
        }
        return film;
    }
}
