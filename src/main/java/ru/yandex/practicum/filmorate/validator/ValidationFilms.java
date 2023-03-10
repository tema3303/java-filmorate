package ru.yandex.practicum.filmorate.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

public class ValidationFilms {
    private final static Logger log = LoggerFactory.getLogger(ValidationFilms.class);

    public static void validationFilms(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.info("Валидация не пройдена, по причине некорректной даты релиза");
            throw new ValidationException("дата релиза — не раньше 28 декабря 1895года");
        }
    }
}