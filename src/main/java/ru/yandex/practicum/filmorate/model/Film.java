package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.validator.CorrectReleaseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Builder
public class Film {
    private Integer id;

    @NonNull
    private @NotBlank String name;

    @Size(min= 1, max= 200)
    private @NotBlank String description;

    @CorrectReleaseDate
    private @PastOrPresent LocalDate releaseDate;

    @Positive
    private int duration;
}
