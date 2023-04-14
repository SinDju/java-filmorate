package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.validator.CorrectReleaseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Film {
    private Integer id;
    @NonNull
    private @NotBlank String name;
    @Size(min = 1, max = 200)
    private @NotBlank String description;
    @CorrectReleaseDate
    private @PastOrPresent LocalDate releaseDate;
    @Positive
    private int duration;
    @Builder.Default
    private Set<Integer> likes = new HashSet<>();
    @Builder.Default
    private List<Genre> genres = new ArrayList<>();
    private MPARating mpa;

    public boolean addGenre(Genre genre) {
        return genres.add(genre);
    }

    public boolean deleteGenre(Integer genreId) {
        return genres.remove(genreId);
    }

    public boolean addLike(Integer userId) {
        return likes.add(userId);
    }

    public boolean deleteLike(Integer userId) {
        return likes.remove(userId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Film)) return false;
        Film film = (Film) o;
        return getId() == film.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
