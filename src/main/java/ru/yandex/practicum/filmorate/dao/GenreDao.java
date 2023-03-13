package ru.yandex.practicum.filmorate.dao;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Optional;

@Component
public interface GenreDao {

    Collection<Genre> findAll();

    String getGenreById(Integer id);
}