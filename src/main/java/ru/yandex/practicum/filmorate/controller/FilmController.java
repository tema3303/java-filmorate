package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.ValidationFilms;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Integer, Film> films = new HashMap<>();
    private int generator = 1;
    private final static Logger log = LoggerFactory.getLogger(FilmController.class);


    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        ValidationFilms.validationFilms(film);
        int filmId = generator++;
        film.setId(filmId);
        films.put(film.getId(), film);
        log.info("Сохранили фильм - " + film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        ValidationFilms.validationFilms(film);
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Id фильма отсуствует в списке");
        }
        films.put(film.getId(), film);
        log.info("Обновили фильм - " + film);
        return film;
    }
}