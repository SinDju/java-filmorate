package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreDao {
    List<Genre> getGenres();

    Genre getGenreById(int id);

    void addGenreInFilm(int filmId, List<Genre> genres);
}
