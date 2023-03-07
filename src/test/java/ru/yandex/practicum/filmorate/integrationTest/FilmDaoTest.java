package ru.yandex.practicum.filmorate.integrationTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.impl.FilmDbStorage;
import ru.yandex.practicum.filmorate.dao.impl.GenreDbStorage;
import ru.yandex.practicum.filmorate.dao.impl.UserDbStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDaoTest {
    private final FilmDbStorage filmDbStorage;
    private final GenreDbStorage genreDbStorage;
    private final UserDbStorage userDbStorage;

    @BeforeEach
    void createFilm() {
        List<Genre> genres = new ArrayList<>();
        genres.add(new Genre(2, genreDbStorage.getGenreById(2)));
        genres.add(new Genre(3, genreDbStorage.getGenreById(3)));
        Film film = new Film("Гарри Поттер 1", "Про волшебников", LocalDate.parse("2001-12-23"),
                120, 0, new Mpa(1, "G"), genres);
        filmDbStorage.create(film);
        film.setGenres(genres);
        Film film2 = new Film("Друзья", "Про друзей", LocalDate.parse("2004-12-23"),
                120, 0, new Mpa(4, "R"), genres);
        filmDbStorage.create(film2);
        film2.setGenres(genres);
        User user = new User("123@mail.com", "Artem33", "Artem", LocalDate.parse("1998-12-23"));
        userDbStorage.create(user);
    }

    @Test
    public void testFindFilmById() {
        Optional<Film> filmOptional = filmDbStorage.getFilmById(1);
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    public void testGetAllFilms() {
        List<Film> films = filmDbStorage.findAll();
        Assertions.assertEquals(2, films.size(), "Число фильмов не совпадает");

    }

    @Test
    public void testCreateFilm() {
        List<Genre> genres = new ArrayList<>();
        Film film = new Film("Гарри Поттер 3", "Про волшебников", LocalDate.parse("2001-12-23"),
                120, 0, new Mpa(1, "G"), genres);
        filmDbStorage.create(film);
        film.setGenres(genres);
        List<Film> films = filmDbStorage.findAll();
        Assertions.assertEquals(3, films.size(), "Число фильмов не совпадает");
    }

    @Test
    public void testUpdateFilm() {
        Film film = new Film("Властелин Колец", "Про эльфов", LocalDate.parse("2001-12-23"),
                200, 0, new Mpa(1, "G"), new ArrayList<>());
        film.setId(1);
        filmDbStorage.update(film);
        Assertions.assertEquals("Властелин Колец", film.getName(), "Название не совпадает");
    }

    @Test
    public void testAddLike() {
        filmDbStorage.addLike(1, 1);
        Assertions.assertEquals(1, filmDbStorage.getFilmById(1).get().getRate(),
                "Рейтинг не совпадает");
    }

    @Test
    public void testDeleteLike() {
        filmDbStorage.addLike(1, 1);
        filmDbStorage.deleteLike(1, 1);
        Assertions.assertEquals(0, filmDbStorage.getFilmById(1).get().getRate(),
                "Рейтинг не совпадает");
    }

    @Test
    public void testGetMostPopularFilm() {
        filmDbStorage.addLike(1, 1);
        List<Film> films = filmDbStorage.getMostPopularFilm(1);
        Assertions.assertEquals("Гарри Поттер 1", films.get(0).getName(),
                "Название не совпадает");
    }
}