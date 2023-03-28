package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    public List<Film> getAllFilms();

    public Film createFilm(Film film);

    public Film updateFilm(Film film);

    void deleteFilm(Film film);

    Optional<Film> getFilm(int id);

    void addLike(int id, int userId);

    void deleteLike(int id, int userId);

    Collection<Film> getTop10Film(int count);
}
