package ru.yandex.practicum.filmorate.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

public class ValidationFilms {
    private final static Logger log = LoggerFactory.getLogger(ValidationFilms.class);

    public static void validationFilms(Film film) {
        if (film.getName().isBlank()) {
            log.info("Валидация не пройдена, по причине отсуствия названия фильма");
            throw new ValidationException("Название фильма не может быть путсым");
        }
        if (film.getDescription().length() > 200) {
            log.info("Валидация не пройдена, по причине длинного описания");
            throw new ValidationException("максимальная длина описания — 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.info("Валидация не пройдена, по причине некорректной даты релиза");
            throw new ValidationException("дата релиза — не раньше 28 декабря 1895года");
        }
        if (film.getDuration() <= 0) {
            log.info("Валидация не пройдена, по причине отрицательной длительности фильма");
            throw new ValidationException("Длительность фильма должна быть положительной");
        }
    }
}