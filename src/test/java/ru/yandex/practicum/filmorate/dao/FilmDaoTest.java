package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.dao.impl.FilmDbStorage;
import ru.yandex.practicum.filmorate.dao.impl.UserDbStorage;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FilmDaoTest {
    private final FilmDbStorage filmStorage;
    private final UserDbStorage userDao;
    private final GenreDao genreDao;
    private final MPARatingDao mpaRatingDao;

    @Order(1)
    @Test
    void getAllFilms() throws SQLException {
        filmStorage.createFilm(Film.builder()
                .name("test")
                .description("test")
                .releaseDate(LocalDate.now())
                .duration(100)
                .genres(List.of(new Genre(1, "Комедия")))
                .mpa(new MPARating(3, "PG-13"))
                .build());
        filmStorage.createFilm(Film.builder()
                .name("test1")
                .description("test1")
                .releaseDate(LocalDate.now())
                .duration(100)
                .genres(List.of(new Genre(1, "Комедия")))
                .mpa(new MPARating(3, "PG-13"))
                .build());
        filmStorage.createFilm(Film.builder()
                .name("test2")
                .description("test2")
                .releaseDate(LocalDate.now())
                .duration(100)
                .genres(List.of(new Genre(1, "Комедия")))
                .mpa(new MPARating(3, "PG-13"))
                .build());

        List<Film> films = filmStorage.getAllFilms();
        assertThat(films.get(0).getName()).isEqualTo("test");
        assertThat(films.get(0).getDescription()).isEqualTo("test");
        assertThat(films.get(2).getId()).isEqualTo(3);
        assertThat(films.size()).isEqualTo(3);
        assertThat(films.get(1).getName()).isEqualTo("test1");
        assertThat(films.get(1).getDescription()).isEqualTo("test1");
        assertThat(films.get(1).getReleaseDate()).isNotNull();
        assertThat(films.get(1).getDuration()).isEqualTo(100);
        assertThat(films.get(2).getGenres()).isEqualTo(List.of(new Genre(1, "Комедия")));
        assertThat(films.get(2).getMpa().getId()).isEqualTo(3);
        assertThat(films.get(2).getMpa().getName()).isEqualTo("PG-13");
    }

    @Order(2)
    @Test
    void updateFilm() {
        filmStorage.createFilm(Film.builder()
                .name("test")
                .description("test")
                .releaseDate(LocalDate.now())
                .duration(100)
                .genres(List.of(new Genre(1, "Комедия")))
                .mpa(new MPARating(3, "PG-13"))
                .build());
        Film film = filmStorage.getFilm(1).get();
        film.setName("TestCheck");
        filmStorage.updateFilm(film);
        assertThat(filmStorage.getFilm(1).get().getName()).isEqualTo("TestCheck");
        assertThat(filmStorage.getFilm(1).get().getDescription()).isEqualTo("test");
        assertThat(filmStorage.getFilm(1).get().getReleaseDate()).isNotNull();
        assertThat(filmStorage.getFilm(1).get().getDuration()).isEqualTo(100);
        assertThat(filmStorage.getFilm(1).get().getGenres()).isEqualTo(List.of(new Genre(1, "Комедия")));
        assertThat(filmStorage.getFilm(2).get().getMpa().getId()).isEqualTo(3);
        assertThat(filmStorage.getFilm(2).get().getMpa().getName()).isEqualTo("PG-13");
    }

    @Order(3)
    @Test
    void getFilmById() {
        filmStorage.createFilm(Film.builder()
                .id(4)
                .name("test")
                .description("test")
                .releaseDate(LocalDate.now())
                .duration(100)
                .genres(List.of(new Genre(1, "Комедия")))
                .mpa(new MPARating(3, "PG-13"))
                .build());
        Film film = filmStorage.getFilm(4).get();
        assertThat(film.getName()).isEqualTo("test");
        assertThat(film.getDescription()).isEqualTo("test");
        assertThat(film.getReleaseDate()).isNotNull();
        assertThat(film.getDuration()).isEqualTo(100);
        assertThat(film.getGenres()).isEqualTo(List.of(new Genre(1, "Комедия")));
        assertThat(film.getMpa().getId()).isEqualTo(3);
        assertThat(film.getMpa().getName()).isEqualTo("PG-13");
    }

    @Order(4)
    @Test
    void like_and_UnlikeFilm() throws NotFoundException {
        filmStorage.createFilm(Film.builder()
                .name("test")
                .description("test")
                .releaseDate(LocalDate.now())
                .duration(100)
                .genres(List.of(new Genre(1, "Комедия")))
                .mpa(new MPARating(3, "PG-13"))
                .build());
        userDao.createUser(User.builder()
                .login("Test")
                .birthday(LocalDate.now())
                .name("TEST")
                .email("test@ya.ru")
                .build());

        assertThat(filmStorage.getFilm(1).get().getLikes().size()).isEqualTo(0);
        filmStorage.addLike(1, 1);
        assertThat(filmStorage.getFilm(1).get().getLikes().size()).isEqualTo(1);
        assertThat(filmStorage.getFilm(1).get().getLikes().contains(1)).isTrue();
        filmStorage.deleteLike(1,1);
        assertThat(filmStorage.getFilm(1).get().getLikes().size()).isEqualTo(0);
    }

    @Test
    void testGetAllMpa() {
        List<MPARating> mpaRatingList = mpaRatingDao.getAllMpa();
        assertThat(mpaRatingList.size()).isEqualTo(5);
    }

    @Test
    public void testGetMpaById() {
        MPARating mpaRating = mpaRatingDao.getMpaById(1);
        assertThat(mpaRating).hasFieldOrPropertyWithValue("id", 1);
    }

    @Test
    void testGetAllGenres() {
        List<Genre> genres = genreDao.getGenres();
        assertThat(genres.size()).isEqualTo(6);
    }

    @Test
    public void testGetGenreById() {
        Genre genre = genreDao.getGenreById(1);
        assertThat(genre).hasFieldOrPropertyWithValue("id", 1);
    }
}
