package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.ErrorIdException;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.List;

@Service
public class FilmService {
    private final UserService userService;
    private final FilmStorage filmStorage;
    private int generatoreId = 0;

    @Autowired
    FilmService(UserService userService,
                @Qualifier("filmDbStorage") FilmStorage filmStorage) {
        this.userService = userService;
        this.filmStorage = filmStorage;
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film createFilm(Film film) {
//        if (filmStorage.getAllFilms().contains(film)) {
//            throw new FilmValidationException("Фильм уже зарегестрирован");
//        }
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        getFilmStorage(film.getId().toString());
        return filmStorage.updateFilm(film);
    }

    public Film findById(String id) { // получение фильма по id
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
        Film film = filmStorage.getFilm(filmId).orElseThrow(() ->
        new NotFoundException("Пользователь с ID " +
                id + " не зарегистрирован!"));
        return film;
    }

    // пользователь ставит лайк фильму
    public void addLike(String id, String userId) {
        Film film = getFilmStorage(id);
        User user = userService.getUserStorage(userId);
        filmStorage.addLike(film.getId(), user.getId());
    }

    // пользователь удаляет лайк
    public void deleteLike(String id, String userId) {
        Film film = getFilmStorage(id);
        User user = userService.getUserStorage(userId);
        filmStorage.deleteLike(film.getId(), user.getId());
    }

    // ?count={count} - возвращает список из первых count фильмов по количеству лайков.
    // Если значение параметра count не задано, верните первые 10.
    public Collection<Film> getPopularFilm(String count) {
        int size = stringForInt(count);
        if (size == Integer.MIN_VALUE) {
            size = 10;
        }
        return filmStorage.getTop10Film(size);
    }

}
