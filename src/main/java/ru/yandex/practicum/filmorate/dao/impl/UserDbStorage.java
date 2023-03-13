package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@RequiredArgsConstructor
public class UserDbStorage implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<User> findAll() {
        String sql = "SELECT * " +
                "FROM users";
        return jdbcTemplate.query(sql, this::mapRowToUser);
    }

    @Override
    public int create(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("USERS")
                .usingGeneratedKeyColumns("USER_ID");
        return simpleJdbcInsert.executeAndReturnKey(user.toMap()).intValue();
    }

    @Override
    public User update(User user) {
        String sql = "UPDATE users SET email = ?, login = ?, name = ?, birthday =? WHERE user_id = ?";
        jdbcTemplate.update(sql,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        return user;
    }

    @Override
    public Optional<User> getUserById(Integer id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * " +
                "FROM users WHERE user_id = ?", id);
        if (userRows.next()) {
            User user = new User(userRows.getString("EMAIL"),
                    userRows.getString("LOGIN"),
                    userRows.getString("NAME"),
                    userRows.getDate("BIRTHDAY").toLocalDate());
            user.setId(userRows.getInt("USER_ID"));
            return Optional.of(user);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Boolean sendRequestFriend(Integer userId, Integer friendId) {
        HashMap<String, Integer> mapFriend = new HashMap<>();
        mapFriend.put("USER_ID", userId);
        mapFriend.put("FRIEND_ID", friendId);
        mapFriend.put("STATUS", 1);
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("FRIEND_STATUS")
                .usingGeneratedKeyColumns("FRIEND_STATUS_ID");
        return simpleJdbcInsert.executeAndReturnKey(mapFriend).intValue() == 1;
    }

    @Override
    public Boolean deleteFriend(Integer userId, Integer friendId) {
        String sql = String.format("DELETE\nFROM friend_status\n " +
                "WHERE user_id = %d AND friend_id = %d", userId, friendId);
        return jdbcTemplate.update(sql) > 0;
    }

    @Override
    public List<User> getAllFriends(Integer id) {
        String sql = String.format("SELECT *\n " +
                "FROM users AS u\n JOIN friend_status AS fs ON u.user_id = fs.friend_id\n " +
                "WHERE fs.user_id = ?", id);
        return jdbcTemplate.query(sql, this::mapRowToUser, id);
    }

    @Override
    public List<User> getCommonFriends(Integer userId, Integer friendId) {
        String sql = String.format("SELECT *\n " +
                "FROM users AS u" +
                " JOIN friend_status AS fs ON u.user_id = fs.friend_id " +
                "WHERE fs.user_id = ? AND fs.friend_id IN" +
                "(SELECT friend_id " +
                "FROM friend_status " +
                "WHERE user_id = ?)", userId, friendId);
        return jdbcTemplate.query(sql, this::mapRowToUser, userId, friendId);
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        User user = new User(resultSet.getString("EMAIL"),
                resultSet.getString("LOGIN"),
                resultSet.getString("NAME"),
                resultSet.getDate("BIRTHDAY").toLocalDate());
        user.setId(resultSet.getInt("USER_ID"));
        return user;
    }
}