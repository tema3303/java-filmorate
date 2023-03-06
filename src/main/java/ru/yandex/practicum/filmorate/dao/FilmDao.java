package ru.yandex.practicum.filmorate.dao;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
public interface FilmDao {
    List<Film> findAll();

    Integer create(Film film);

    Film update(Film film);

    Optional<Film> getFilmById(Integer id);
}