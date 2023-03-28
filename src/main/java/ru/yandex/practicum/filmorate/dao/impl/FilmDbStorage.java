package ru.yandex.practicum.filmorate.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
public class FilmDbStorage implements FilmStorage {
    private final Logger log = LoggerFactory.getLogger(FilmDbStorage.class);
    private final JdbcTemplate jdbcTemplate;
    private MPARatingDbStorage mpaDbStorage;
    private GenreDbStorage genreDbStorage;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, MPARatingDbStorage mpaDbStorage, GenreDbStorage genreDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaDbStorage = mpaDbStorage;
        this.genreDbStorage = genreDbStorage;
    }

    @Override
    public List<Film> getAllFilms() {
        String sql = "SELECT * FROM FILM";
        return jdbcTemplate.query(sql, (rs, rowNum) -> filmRowMapper(rs));
    }

    private final Film filmRowMapper(ResultSet rs) throws SQLException {
        Film film = Film.builder()
                .id(rs.getInt("ID"))
                .name(rs.getString("NAME"))
                .description(rs.getString("DESCRIPTION"))
                .releaseDate(rs.getDate("RELEASEDATE").toLocalDate())
                .genres(genreDbStorage.getGenresByFilm(rs.getInt("id")))
                .duration(rs.getInt("DURATION"))
                .mpa(mpaDbStorage.getMpaById(rs.getInt("MPARating_id"))).build();
        film.getLikes().addAll(getLikesFilm(film.getId()));
        return film;
    }

    private List<Integer> getLikesFilm(int filmId) {
        String sql = "SELECT USER_ID FROM USER_LIKES WHERE FILM_ID = ?";
        return jdbcTemplate.queryForList(sql, Integer.class, filmId);
    }

    @Override
    public Optional<Film> getFilm(int id) {
        String sql = "select * from film where id = ?";
        SqlRowSet rowFilm = jdbcTemplate.queryForRowSet(sql, id);
        if (rowFilm.next()) {
            Film film = Film.builder().id(rowFilm.getInt("id"))
                    .name(rowFilm.getString("name"))
                    .description(rowFilm.getString("description"))
                    .duration(rowFilm.getInt("duration"))
                    .releaseDate(rowFilm.getDate("releasedate").toLocalDate())
                    .genres(genreDbStorage.getGenresByFilm(rowFilm.getInt("id")))
                    .mpa(mpaDbStorage.getMpaById(rowFilm.getInt("MPARating_id"))).build();
            film.getLikes().addAll(getLikesFilm(film.getId()));
            log.info("Найден фильм: {} {} {} {} {} {}", film.getId(), film.getName(), film.getDescription(),
                    film.getReleaseDate(), film.getDuration(), film.getMpa().getName());
            return Optional.of(film);
        } else {
            log.info("Фильм с идентификатором {} не найден.", id);
            return Optional.empty();
        }
    }

    @Override
    public Film createFilm(Film film) {
        String sql = "insert into film(name, description, duration, releasedate, mparating_id) " +
                "values (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, film.getName());
            statement.setString(2, film.getDescription());
            statement.setInt(3, film.getDuration());
            statement.setDate(4, Date.valueOf(film.getReleaseDate()));
            statement.setInt(5, film.getMpa().getId());
            return statement;
        }, keyHolder);
        film.setId(keyHolder.getKey().intValue());
        genreDbStorage.addGenreInFilm(film.getId(), film.getGenres());
        if (!film.getLikes().isEmpty()) {
            for (int userId : film.getLikes()) {
                addLike(film.getId(), userId);
            }
        }
        log.info("Создан фильм: {} {} {} {} {} {}", film.getId(), film.getDescription(),
                film.getDuration(), film.getReleaseDate(), film.getMpa(), film.getLikes());
        return getFilm(film.getId()).get();
    }

    @Override
    public void addLike(int filmId, int userId) {
        String sql = "SELECT * FROM USER_LIKES WHERE USER_ID = ? AND FILM_ID = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, userId, filmId);
        if (!sqlRowSet.next()) {
            String sqlAddLike = "INSERT INTO USER_LIKES (USER_ID, FILM_ID) VALUES (?, ?)";
            jdbcTemplate.update(sqlAddLike, userId, filmId);
        }
    }

    @Override
    public Film updateFilm(Film film) {
        String sql = "UPDATE FILM SET NAME = ?, DESCRIPTION = ?, RELEASEDATE = ?, DURATION = ?, MPARATING_ID = ?" +
                "WHERE ID = ?";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(),
                film.getReleaseDate(), film.getDuration(), film.getMpa().getId(), film.getId());
        genreDbStorage.addGenreInFilm(film.getId(), film.getGenres());
        if (!film.getLikes().isEmpty()) {
            for (int userId : film.getLikes()) {
                addLike(film.getId(), userId);
            }
        }
        return getFilm(film.getId()).get();
    }

    @Override
    public void deleteFilm(Film film) {
        String sql = "DELETE FROM FILM WHERE ID = ?";
        jdbcTemplate.update(sql, film.getId());
    }

    @Override
    public void deleteLike(int id, int userId) {
        String sql = "DELETE FROM USER_LIKES WHERE FILM_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(sql, id, userId);
        Film film = getFilm(id).get();
        film.deleteLike(userId);
    }

    @Override
    public Collection<Film> getTop10Film(int count) {
        String sqlGetPopular = "SELECT f.ID, f.NAME, f.DESCRIPTION, f.RELEASEDATE, f.DURATION, f.MPARATING_ID, " +
                "COUNT(ul.USER_ID) " +
                "FROM FILM f " +
                "INNER JOIN USER_LIKES ul ON f.ID = ul.FILM_ID " +
                "GROUP BY f.ID " +
                "ORDER BY COUNT(ul.USER_ID) DESC " +
                "LIMIT ?";
        List<Film> top10Film = List.copyOf(jdbcTemplate.query(sqlGetPopular,
                (rs, rowNum) -> filmRowMapper(rs), count));
        if (top10Film.isEmpty()) {
            top10Film = List.copyOf(getAllFilms());
        }
        return top10Film;
    }


}