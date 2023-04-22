package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
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
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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
                .genres(getGenresByFilm(rs.getInt("id")))
                .duration(rs.getInt("DURATION"))
                .mpa(getMpaById(rs.getInt("MPARating_id"))).build();
        film.getLikes().addAll(getLikesFilm(film.getId()));
        return film;
    }

    private List<Integer> getLikesFilm(int filmId) {
        String sql = "SELECT USER_ID FROM USER_LIKES WHERE FILM_ID = ?";
        return jdbcTemplate.queryForList(sql, Integer.class, filmId);
    }

    private MPARating getMpaById(int id) {
        String sqlGetMPA = "SELECT * FROM MPARATING WHERE ID = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlGetMPA, id);
        if (!sqlRowSet.next()) {
            throw new NotFoundException("Такого рейтенга нет");
        }
        return new MPARating(sqlRowSet.getInt("ID"),
                sqlRowSet.getString("RATING_NAME"));
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
                    .genres(getGenresByFilm(rowFilm.getInt("id")))
                    .mpa(getMpaById(rowFilm.getInt("MPARating_id"))).build();
            film.getLikes().addAll(getLikesFilm(film.getId()));
            log.info("Найден фильм: {} {} {} {} {} {}", film.getId(), film.getName(), film.getDescription(),
                    film.getReleaseDate(), film.getDuration(), film.getMpa().getName());
            return Optional.of(film);
        } else {
            log.info("Фильм с идентификатором {} не найден.", id);
            return Optional.empty();
        }
    }

    private List<Genre> getGenresByFilm(int filmId) {
        String sqlGetGenres = "SELECT GENRES.ID, GENRES.GENRE_NAME FROM GENRES " +
                "INNER JOIN FILMS_GENRES FG on GENRES.ID = FG.GENRE_ID " +
                "WHERE FILM_ID = ?";
        return jdbcTemplate.query(sqlGetGenres, this::makeGenre, filmId);
    }

    private Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        return new Genre(rs.getInt("ID"), rs.getString("GENRE_NAME"));
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
        if (!film.getLikes().isEmpty()) {
            for (int userId : film.getLikes()) {
                addLike(film.getId(), userId);
            }
        }
        log.info("Создан фильм: {} {} {} {} {} {}", film.getId(), film.getDescription(),
                film.getDuration(), film.getReleaseDate(), film.getMpa(), film.getLikes());
        return film;
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
