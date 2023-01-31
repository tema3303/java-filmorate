package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder
public class Film {
    private int id;

    @NotEmpty
    private final String name;

    @Size(max = 200)
    private final String description;

    private final LocalDate releaseDate;

    @Positive
    private final int duration;
}