package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MpaDbStorage implements MpaDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Mpa> findAll() {
        String sql = "SELECT * " +
                "FROM MPA";
        return jdbcTemplate.query(sql, this::mapRowToMap);
    }

    @Override
    public Optional<Mpa> getMpaById(Integer id) {
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("SELECT * " +
                "FROM MPA WHERE MPA_ID = ?", id);
        if (mpaRows.next()) {
            Mpa mpa = new Mpa(mpaRows.getString("MPA_NAME"));
            mpa.setId(mpaRows.getInt("MPA_ID"));
            return Optional.of(mpa);
        } else {
            return Optional.empty();
        }
    }

    private Mpa mapRowToMap(ResultSet resultSet, int rowNum) throws SQLException {
        Mpa mpa = new Mpa(resultSet.getString("MPA_NAME"));
        mpa.setId(resultSet.getInt("MPA_ID"));
        return mpa;
    }
}