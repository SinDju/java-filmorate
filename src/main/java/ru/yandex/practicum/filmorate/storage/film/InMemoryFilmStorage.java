package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private HashMap<Integer, Film> filmsMap = new HashMap<>();

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(filmsMap.values());
    }

    @Override
    public Film createFilm(Film film) {
        filmsMap.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        filmsMap.put(film.getId(), film);
        return film;
    }

    @Override
    public void deleteFilm(Film film) {
        if (!filmsMap.containsKey(film.getId())) {
            throw new ValidationException("Фильм " + film + " не может быть удален, так как еще не зарегестрирован");
        }
        filmsMap.remove(film.getId());
    }

    @Override
    public Film getFilm(int id) { // получение фильма по id
        return filmsMap.get(id);
    }

    @Override
    public void addLike(int id, int userId) { // пользователь ставит лайк фильму
        Film film = filmsMap.get(id);
        film.addLike(userId);
        updateFilm(film);
    }

    @Override
    public void deleteLike(int id, int userId) { // пользователь удаляет лайк
        Film film = filmsMap.get(id);
        film.deleteLike(userId);
        updateFilm(film);
    }

    @Override
    // ?count={count} - возвращает список из первых count фильмов по количеству лайков.
    // Если значение параметра count не задано, верните первые 10. Film count?
    public Collection<Film> getTop10Film(int count) {
        Collection<Film> top10Film = getAllFilms().stream()
                .sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .limit(count)
                .collect(Collectors.toCollection(HashSet::new));
        return top10Film;
    }
}

