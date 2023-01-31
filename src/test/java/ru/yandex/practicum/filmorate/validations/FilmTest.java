package ru.yandex.practicum.filmorate.validations;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.ValidationFilms;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmTest {
    @Test
    void createFilmFailDate() throws ValidationException {
        Film film = Film.builder().name("444")
                .description("des")
                .releaseDate(LocalDate.of(1895, 11, 22))
                .duration(-120)
                .build();
        Throwable throwable = assertThrows(ValidationException.class, () -> {
            ValidationFilms.validationFilms(film);
        });
        assertEquals(throwable.getMessage(), "дата релиза — не раньше 28 декабря 1895года");
    }
}