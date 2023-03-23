package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {
    public List<Film> getAllFilms();
    public Film createFilm(Film film);
    public Film updateFilm(Film film);

    void deleteFilm(Film film);

    Film getFilm(int id);

    void addLike(int id, int userId) // делать проверку что пользователь может поставить лайк только 1 раз
    ;

    void deleteLike(int id, int userId);

    // ?count={count} - возвращает список из первых count фильмов по количеству лайков.
    // Если значение параметра count не задано, верните первые 10. Film count?
    Collection<Film> getTop10Film(int count);
}
