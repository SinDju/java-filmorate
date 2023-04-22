package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Service
public class GenreService {
    GenreDao genreDao;

    @Autowired
    public GenreService(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    public List<Genre> getGenres() {
        return genreDao.getGenres();
    }

    public Genre getGenreByID(int id) {
        return genreDao.getGenreById(id);
    }

    public void addGenreInFilm(int filmId, List<Genre> genres) {
        genreDao.addGenreInFilm(filmId, genres);
    }
}
