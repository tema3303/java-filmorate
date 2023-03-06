package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
public class FilmDbStorage implements FilmDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Film> findAll() {
        String sql = "SELECT FILMS.FILM_ID, FILMS.NAME, FILMS.DESCRIPTION, FILMS.RELEASE_DATE, FILMS.DURATION, FILMS.RATE, FILMS" +
                ".MPA_ID, M.MPA_NAME" +
                " FROM FILMS " +
                "JOIN MPA AS M ON FILMS.MPA_ID = M.MPA_ID";
        return jdbcTemplate.query(sql, this::mapRowToFilm);
    }

    @Override
    public Integer create(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("FILMS")
                .usingGeneratedKeyColumns("FILM_ID");
        return simpleJdbcInsert.executeAndReturnKey(film.toMap()).intValue();
    }

    @Override
    public Film update(Film film) {
        String sql = "UPDATE FILMS SET NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION =?, RATE = ?, " +
                "MPA_ID = ? WHERE FILM_ID = ?";
        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRate(),
                film.getMpa().getId(),
                film.getId());
        return film;
    }

    @Override
    public Optional<Film> getFilmById(Integer id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT FILMS.FILM_ID, FILMS.NAME, FILMS.DESCRIPTION, FILMS.RELEASE_DATE, FILMS.DURATION, FILMS.RATE, " +
                "FILMS.MPA_ID, MPA.MPA_NAME" +
                " FROM FILMS " +
                "JOIN MPA ON FILMS.MPA_ID = MPA.MPA_ID WHERE FILM_ID = ?", id);
        if (filmRows.next()) {
            Film film = new Film(filmRows.getString("NAME"),
                    filmRows.getString("DESCRIPTION"),
                    filmRows.getDate("RELEASE_DATE").toLocalDate(),
                    filmRows.getInt("DURATION"),
                    filmRows.getInt("RATE"),
                    new Mpa(filmRows.getInt("MPA_ID"), filmRows.getString("MPA_NAME")),
                    new ArrayList<>());
            film.setId(filmRows.getInt("FILM_ID"));
            return Optional.of(film);
        } else {
            return Optional.empty();
        }
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = new Film(resultSet.getString("NAME"),
                resultSet.getString("DESCRIPTION"),
                resultSet.getDate("RELEASE_DATE").toLocalDate(),
                resultSet.getInt("DURATION"),
                resultSet.getInt("RATE"),
                new Mpa(resultSet.getInt("MPA_ID"), resultSet.getString("MPA_NAME")),
                new ArrayList<>());
        film.setId(resultSet.getInt("FILM_ID"));
        return film;
    }

    public boolean addLike(Integer idFilm, Integer idUser) {
        String sql = String.format("INSERT INTO LIKES(FILM_ID, USER_ID) VALUES (%d, %d)", idFilm, idUser);
        String sqlUpd = String.format("UPDATE FILMS SET RATE = RATE + 1 " +
                "WHERE FILM_ID = %d", idFilm);
        jdbcTemplate.update(sqlUpd);
        jdbcTemplate.update(sql);
        return true;
    }

    public boolean deleteLike(Integer idFilm, Integer idUser) {
        String sql = String.format("DELETE FROM LIKES WHERE FILM_ID = ? AND USER_ID = ?", idFilm, idUser);
        String sqlUpd = String.format("UPDATE FILMS SET RATE = RATE - 1 " +
                "WHERE FILM_ID = %d", idFilm);
        jdbcTemplate.update(sqlUpd);
        jdbcTemplate.update(sql, idFilm, idUser);
        return true;
    }

    public List<Film> getMostPopularFilm(Integer count) {
        String sql = String.format("SELECT FILMS.FILM_ID, FILMS.NAME, FILMS.DESCRIPTION, FILMS.RELEASE_DATE, FILMS.DURATION, FILMS.RATE, FILMS" +
                ".MPA_ID, M.MPA_NAME" +
                " FROM FILMS " +
                "JOIN MPA AS M ON FILMS.MPA_ID = M.MPA_ID " +
                "WHERE RATE > 0 " +
                "ORDER BY RATE DESC " +
                "LIMIT(%d)", count);
        return jdbcTemplate.query(sql, this::mapRowToFilm);
    }

    public boolean setGenre(Integer idGenre, Integer idFilm) {
        if (!findGenreToFilm(idGenre, idFilm)) {
            String sql = String.format("INSERT INTO FILM_GENRES(GENRE_ID,FILM_ID) VALUES (%d, %d)", idGenre, idFilm);
            return jdbcTemplate.update(sql) == 1;
        }
        return true;
    }

    public boolean deleteGenre(Integer idGenre, Integer idFilm) {
        if (findGenreToFilm(idGenre, idFilm)) {
            String sql = String.format("DELETE FROM FILM_GENRES " +
                    "WHERE GENRE_ID = ? AND FILM_ID = ?", idGenre, idFilm);
            return jdbcTemplate.update(sql, idGenre, idFilm) > 0;
        }
        return false;
    }

    private boolean findGenreToFilm(Integer idGenre, Integer idFilm) {
        String sql = String.format("SELECT COUNT(*) " +
                "FROM FILM_GENRES\n" +
                "WHERE GENRE_ID = %d AND FILM_ID = %d", idGenre, idFilm);
        return jdbcTemplate.queryForObject(sql, Integer.class) == 1;
    }
}