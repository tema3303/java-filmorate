package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {

    final FilmService filmService;
    private final static Logger log = LoggerFactory.getLogger(FilmController.class);

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> findAll() {
        return filmService.findAll();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Сохранили фильм - " + film);
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Обновили фильм - " + film);
        return filmService.update(film);
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable Integer id) {
        return filmService.getFilmById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<Film> addLikeFilm(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("Ставим лайк фильму - " + filmService.getFilmById(id));
        filmService.addLikeFilm(id, userId);
        return new ResponseEntity<>(filmService.getFilmById(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public ResponseEntity<Film> deleteLikeFilm(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("Удаляем лайк фильму - " + filmService.getFilmById(id));
        filmService.deleteLikeFilm(id, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/popular")
    public List<Film> getMostPopularFilm(@RequestParam(required = false, defaultValue = "10") Integer count) {
        log.info("Получение популярных фильмов");
        return filmService.getMostPopularFilm(count);
    }
}