package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class GenreDbStorage implements GenreDao {
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> getGenres() {
        String sql = "SELECT * FROM GENRES";
        return jdbcTemplate.query(sql, this::makeGenre);
    }

    @Override
    public List<Genre> getGenresByFilm(int filmId) {
        String sql = "SELECT GENRES.ID, GENRES.GENRE_NAME FROM GENRES " +
                "INNER JOIN FILMS_GENRES FG on GENRES.ID = FG.GENRE_ID " +
                "WHERE FILM_ID = ?";
        return jdbcTemplate.query(sql, this::makeGenre, filmId);
    }

    private Genre makeGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return new Genre(resultSet.getInt("ID"), resultSet.getString("GENRE_NAME"));
    }

    @Override
    public Genre getGenreById(int id) {
        String sql = "SELECT * FROM GENRES WHERE ID = ?";
        Genre genre;
        try {
            genre = jdbcTemplate.queryForObject(sql, this::makeGenre, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Жанр с идентификатором " +
                    id + " не зарегистрирован!");
        }
        return genre;
    }

    public void deleteGenreInFilm(int filmId) {
        String sql = "DELETE FROM FILMS_GENRES WHERE FILM_ID = ?";
        jdbcTemplate.update(sql, filmId);
    }

    public void addGenreInFilm(int filmId, List<Genre> genres) {
        deleteGenreInFilm(filmId);
        if (genres.isEmpty()) {
            return;
        }
        String sql = "INSERT INTO FILMS_GENRES (FILM_ID, GENRE_ID) VALUES (?, ?)";
        Set<Integer> saveGenres = genres.stream().map(Genre::getId).collect(Collectors.toSet());
        for (Integer genreId : saveGenres) {
            jdbcTemplate.update(sql, filmId, genreId);
        }
    }
}
