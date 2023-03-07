package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.impl.GenreDbStorage;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class GenreService {

    private GenreDbStorage genreDbStorage;

    public Collection<Genre> findAll() {
        return genreDbStorage.findAll();
    }

    public Genre getGenreById(Integer id) {
        return new Genre(id, genreDbStorage.getGenreById(id));
    }

    public List<Genre> getGenres(Integer id) {
        return genreDbStorage.getGenres(id);
    }
}