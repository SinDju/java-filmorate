package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private Integer id;

    @Email(message = "Неккоректная почта")
    private String email;

    @Pattern(regexp = "\\S*", message = "Логин не может содержать пробелы.")
    private @NotEmpty String login;
    private String name;

    @PastOrPresent(message = "Дата рождения не может быть в будущем.")
    private @NotNull LocalDate birthday;

    @Builder.Default
    private Set<Integer> friendIds = new HashSet<>();

    public boolean addFriend(final Integer id) {
        return friendIds.add(id);
    }

    public boolean deleteFriend(final Integer id) {
        return friendIds.remove(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getId() == user.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
