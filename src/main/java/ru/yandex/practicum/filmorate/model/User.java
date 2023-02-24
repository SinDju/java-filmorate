package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private Integer id;

    @Email(message = "Неккоректная почта")
    private String email;

    @NotBlank
    private String login;

    private String name;


    @PastOrPresent
    private @NotNull LocalDate birthday;

}
