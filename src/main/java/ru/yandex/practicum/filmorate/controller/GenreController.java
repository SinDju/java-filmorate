package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dao.impl.GenreDbStorage;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@RestController
@RequestMapping("/genres")
@Slf4j
public class GenreController {
    private final GenreDbStorage genreDbStorage;

    @Autowired
    public GenreController(GenreDbStorage genreDbStorage) {
        this.genreDbStorage = genreDbStorage;
    }

    @GetMapping
    public List<Genre> getGenres() {
        log.info("Получен запрос GET к эндпоинту: /genres");
        return genreDbStorage.getGenres();
    }

    @GetMapping("/{id}")
    public Genre getGenreByID(@PathVariable int id) {
        log.info("Получен запрос GET к эндпоинту: /genres/{id}");
        return genreDbStorage.getGenreById(id);
    }
}
