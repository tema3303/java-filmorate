package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private InMemoryFilmStorage inMemoryFilmStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage inMemoryFilmStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }

    public Collection<Film> findAll() {
        return inMemoryFilmStorage.findAll();
    }

    public Film create(Film film) {
        return inMemoryFilmStorage.create(film);
    }

    public Film update(Film film) {
        return inMemoryFilmStorage.update(film);
    }

    public Film getFilmById(Integer id) {
        return inMemoryFilmStorage.getFilmById(id);
    }

    public void addLikeFilm(Integer filmId, Integer userId) {
        correctId(userId);
        if (!inMemoryFilmStorage.getFilmById(filmId).getUserLikeFilm().contains(userId)) {
            inMemoryFilmStorage.getFilmById(filmId).addLike(userId);
        }
    }

    public void deleteLikeFilm(Integer filmId, Integer userId) {
        correctId(userId);
        inMemoryFilmStorage.getFilmById(filmId).deleteLike(userId);
    }

    public List<Film> getMostPopularFilm(Integer count) {
        Comparator<Film> filmComparator = (film1, film2) -> {
            if (film2.getRating().compareTo(film1.getRating()) == 0) {
                return film1.getName().compareTo(film2.getName());
            }
            return film2.getRating().compareTo(film1.getRating());
        };
        return inMemoryFilmStorage.findAll().stream()
                .sorted(filmComparator)
                .limit(count)
                .collect(Collectors.toList());
    }

    private void correctId(Integer id) {
        if (id < 1) {
            throw new NotFoundException("Id должно быть больше 0");
        }
    }
}