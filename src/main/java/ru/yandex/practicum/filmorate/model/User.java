package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder
public class User {
    private int id;

    @NotEmpty
    @Email
    private final String email;

    @NotBlank
    @Pattern(regexp = "^\\S*")
    private final String login;

    private String name;

    @Past
    private final LocalDate birthday;
}