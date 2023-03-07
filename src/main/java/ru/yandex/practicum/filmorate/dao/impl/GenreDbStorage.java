package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GenreDbStorage implements GenreDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Genre> findAll() {
        String sql = "SELECT * " +
                "FROM GENRE";
        return jdbcTemplate.query(sql, this::mapRowToGenre);
    }

    @Override
    public String getGenreById(Integer id) {
        String sqlQuery = String.format("SELECT NAME " +
                "FROM GENRE WHERE GENRE_ID = %d", id);
        List<String> names = jdbcTemplate.queryForList(sqlQuery, String.class);
        if (names.size() != 1) {
            throw new NotFoundException("Не корректный ID GENRE");
        }
        return names.get(0);
    }

    public List<Genre> getGenres(Integer idFilm) {
        ;
        String sqlQuery = String.format("SELECT GENRE_ID\n" +
                "FROM FILM_GENRES\n" +
                "WHERE FILM_ID = %d", idFilm);
        List<Integer> idGenres = jdbcTemplate.queryForList(sqlQuery, Integer.class);
        List<Genre> genres = new ArrayList<>();
        for (Integer id : idGenres) {
            genres.add(new Genre(id, getGenreById(id)));
        }
        return genres;
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        Genre genre = new Genre(resultSet.getString("NAME"));
        genre.setId(resultSet.getInt("GENRE_ID"));
        return genre;
    }
}