package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class FilmDbStorage implements FilmDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Film> findAll() {
        String sql = "SELECT f.film_id, f.name, f.description, f.release_date, f.duration, f.rate, f" +
                ".mpa_id, m.mpa_name" +
                " FROM films AS f " +
                "JOIN mpa AS m ON f.mpa_id = m.mpa_id";
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
        String sql = "UPDATE films SET name = ?, description = ?, release_date = ?, duration =?, rate = ?, " +
                "mpa_id = ? WHERE film_id = ?";
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
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT f.film_id, f.name, f.description, f.release_date, f.duration, f.rate, " +
                "f.mpa_id, m.mpa_name" +
                " FROM films AS f " +
                "JOIN mpa AS m ON f.mpa_id = m.mpa_id WHERE f.film_id = ?", id);
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
        String sql = String.format("INSERT INTO likes(film_id, user_id) VALUES (%d, %d)", idFilm, idUser);
        String sqlUpd = String.format("UPDATE films SET rate = rate + 1 " +
                "WHERE film_id = %d", idFilm);
        jdbcTemplate.update(sqlUpd);
        jdbcTemplate.update(sql);
        return true;
    }

    public boolean deleteLike(Integer idFilm, Integer idUser) {
        String sql = String.format("DELETE FROM likes WHERE film_id = ? AND user_id = ?", idFilm, idUser);
        String sqlUpd = String.format("UPDATE films SET rate = rate - 1 " +
                "WHERE film_id = %d", idFilm);
        jdbcTemplate.update(sqlUpd);
        jdbcTemplate.update(sql, idFilm, idUser);
        return true;
    }

    public List<Film> getMostPopularFilm(Integer count) {
        String sql = String.format("SELECT f.film_id, f.name, f.description, f.release_date, f.duration, f.rate, f" +
                ".mpa_id, m.mpa_name" +
                " FROM films AS f " +
                "JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
                "WHERE rate > 0 " +
                "ORDER BY rate DESC " +
                "LIMIT(%d)", count);
        return jdbcTemplate.query(sql, this::mapRowToFilm);
    }

    public boolean setGenre(Integer idGenre, Integer idFilm) {
        if (!findGenreToFilm(idGenre, idFilm)) {
            String sql = String.format("INSERT INTO film_genres(genre_id,film_id) VALUES (%d, %d)", idGenre, idFilm);
            return jdbcTemplate.update(sql) == 1;
        }
        return true;
    }

    public boolean deleteGenre(Integer idGenre, Integer idFilm) {
        if (findGenreToFilm(idGenre, idFilm)) {
            String sql = String.format("DELETE FROM film_genres " +
                    "WHERE genre_id = ? AND film_id = ?", idGenre, idFilm);
            return jdbcTemplate.update(sql, idGenre, idFilm) > 0;
        }
        return false;
    }

    private boolean findGenreToFilm(Integer idGenre, Integer idFilm) {
        String sql = String.format("SELECT COUNT(*) " +
                "FROM film_genres\n" +
                "WHERE genre_id = %d AND film_id = %d", idGenre, idFilm);
        return jdbcTemplate.queryForObject(sql, Integer.class) == 1;
    }
}