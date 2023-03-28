package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MPARating {
    private int id;
    @NotBlank
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MPARating mpaRating = (MPARating) o;
        return id == mpaRating.id && Objects.equals(name, mpaRating.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
