package ru.yandex.practicum.filmorate.validations;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.ValidationFilms;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmTest {
    @Test
    void createFilmFailDate() throws ValidationException {
        final Film film = new Film("444","des",LocalDate.of(1895, 11, 22),120, 0);
        Throwable throwable = assertThrows(ValidationException.class, () -> {
            ValidationFilms.validationFilms(film);
        });
        assertEquals(throwable.getMessage(), "дата релиза — не раньше 28 декабря 1895года");
    }
}