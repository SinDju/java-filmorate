package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.ErrorIdException;
import ru.yandex.practicum.filmorate.exeption.FilmValidationException;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.List;

@Service
public class FilmService {
    private final UserService userServicee;
    private final FilmStorage filmStorage;
    private int generatoreId = 0;

    @Autowired
    FilmService(UserService userServicee, FilmStorage filmStorage){
        this.userServicee = userServicee;
        this.filmStorage = filmStorage;
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film createFilm(Film film) {
        if (filmStorage.getAllFilms().contains(film)) {
            throw new FilmValidationException("Фильм уже зарегестрирован");
        }
        final int id = ++generatoreId;
        film.setId(id);
        filmStorage.createFilm(film);
        return film;
    }

    public Film updateFilm(Film film) {
        if (!filmStorage.getAllFilms().contains(film)){
            throw new NotFoundException("Фильм еще не зарегестрирован");
        }
        filmStorage.updateFilm(film);
        return film;
    }

    public Film findById(String id){ // получение фильма по id
        return getFilmStorage(id);
    }

    private Integer stringForInt(final String supposedInt) {
        try {
            return Integer.valueOf(supposedInt);
        } catch (NumberFormatException exception) {
            return Integer.MIN_VALUE;
        }
    }

    public Film getFilmStorage(String id) {
        Integer filmId = stringForInt(id);
        if (filmId == Integer.MIN_VALUE) {
            throw new ErrorIdException("Не удалось распознать идентификатор фильма: "
                    + id);
        }
        Film film = filmStorage.getFilm(filmId);
        if (film == null) {
            throw new NotFoundException("Фильм с ID " + id + " не зарегестрирован");
        }
        return film;
    }

    // пользователь ставит лайк фильму(надо добавить бин ЮзерСтородж?)
    public void addLike(String id, String userId) {
        Film film = getFilmStorage(id);
        User user = userServicee.getUserStorage(userId);
        filmStorage.addLike(film.getId(), user.getId());
    } // делать проверку что пользователь может поставить лайк только 1 раз

    // пользователь удаляет лайк
    public void deleteLike(String id, String userId) {
        Film film = getFilmStorage(id);
        User user = userServicee.getUserStorage(userId);
        filmStorage.deleteLike(film.getId(), user.getId());
    }

    // ?count={count} - возвращает список из первых count фильмов по количеству лайков.
    // Если значение параметра count не задано, верните первые 10. Film count?
    public Collection<Film> getPopularFilm(String count){
        int size = stringForInt(count);
        if (size == Integer.MIN_VALUE) {
            size = 10;
        }
        return  filmStorage.getTop10Film(size);
    }

}
