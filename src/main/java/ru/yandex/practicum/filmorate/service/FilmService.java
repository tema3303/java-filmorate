package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.impl.FilmDbStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.validator.ValidationFilms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class FilmService {

    private FilmDbStorage filmDbStorage;
    private MpaService mpaService;
    private GenreService genreService;

    @Autowired
    public FilmService(FilmDbStorage filmDbStorage, MpaService mpaService, GenreService genreService) {
        this.filmDbStorage = filmDbStorage;
        this.mpaService = mpaService;
        this.genreService = genreService;
    }

    public Collection<Film> findAll() {
        var result = filmDbStorage.findAll();
        for (Film film : result) {
            film.setGenres(genreService.getGenres(film.getId()));
        }
        return result;
    }

    public Film create(Film film) {
        ValidationFilms.validationFilms(film);
        film.setId(filmDbStorage.create(film));
        film.setMpa(mpaService.getMpaById(film.getMpa().getId()));
        List<Genre> genres = new ArrayList<>();
        for (Genre genre : film.getGenres()) {
            genres.add(genreService.getGenreById(genre.getId()));
            if (!filmDbStorage.setGenre(genre.getId(), film.getId())) {
                throw new NotFoundException("Не удалось установить жанр для фильма");
            }
        }
        film.setGenres(genres);
        return film;
    }

    public Film update(Film film) {
        ValidationFilms.validationFilms(film);
        correctId(film.getId());
        film.setMpa(mpaService.getMpaById(film.getMpa().getId()));
        List<Genre> newGenre = new ArrayList<>();
        for (Genre genre : film.getGenres()) {
            if (!newGenre.contains(genreService.getGenreById(genre.getId()))) {
                newGenre.add(genreService.getGenreById(genre.getId()));
            }
            if (!filmDbStorage.setGenre(genre.getId(), film.getId())) {
                throw new NotFoundException("Не удалось установить жанр для фильма");
            }
        }
        List<Genre> currentGenre = genreService.getGenres(film.getId());
        for (Genre current : currentGenre) {
            if (!newGenre.contains(current)) {
                filmDbStorage.deleteGenre(current.getId(), film.getId());
            }
        }
        film.setGenres(newGenre);
        return filmDbStorage.update(film);
    }

    public Film getFilmById(Integer id) {
        var result = filmDbStorage.getFilmById(id).orElseThrow(() -> new NotFoundException("Фильм не найден"));
        result.setGenres(genreService.getGenres(result.getId()));
        return result;
    }

    public void addLikeFilm(Integer filmId, Integer userId) {
        correctId(userId);
        filmDbStorage.addLike(filmId, userId);
    }

    public void deleteLikeFilm(Integer filmId, Integer userId) {
        correctId(userId);
        filmDbStorage.deleteLike(filmId, userId);
    }

    public List<Film> getMostPopularFilm(Integer count) {
        var result = filmDbStorage.getMostPopularFilm(count);
        for (Film film : result) {
            film.setGenres(genreService.getGenres(film.getId()));
        }
        return result;
    }

    private Boolean correctId(Integer id) {
        if (filmDbStorage.getFilmById(id).isEmpty()) {
            throw new NotFoundException("Id фильма отсуствует в списке");
        } else {
            return true;
        }
    }
}