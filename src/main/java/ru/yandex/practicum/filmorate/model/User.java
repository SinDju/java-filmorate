package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;

//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
@Data
@Builder
public class User {
    private Integer id;

    @Email(message = "Неккоректная почта")
    private String email;

    @Pattern(regexp = "\\S*", message = "Логин не может содержать пробелы.")
    private @NotEmpty String login;

    private String name;


    @PastOrPresent
    private @NotNull LocalDate birthday;

}
