package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.ValidationFilms;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();

    private int generator = 1;

    @Override
    public Collection<Film> findAll() {
        return films.values();
    }

    @Override
    public Film create(Film film) {
        ValidationFilms.validationFilms(film);
        int filmId = generator++;
        film.setId(filmId);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        ValidationFilms.validationFilms(film);
        notContain(film.getId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film getFilmById(Integer id) {
        notContain(id);
        return films.get(id);
    }

    private Boolean notContain(Integer id) {
        if (!films.containsKey(id)) {
            throw new NotFoundException("Id фильма отсуствует в списке");
        } else {
            return true;
        }
    }
}