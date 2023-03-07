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
        String sql = "SELECT USERS.USER_ID, USERS.EMAIL, USERS.LOGIN, USERS.NAME, USERS.BIRTHDAY " +
                "FROM USERS";
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
        String sql = "UPDATE USERS SET EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY =? WHERE USER_ID = ?";
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
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT USER_ID, EMAIL, LOGIN, NAME, BIRTHDAY " +
                "FROM USERS WHERE USER_ID = ?", id);
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
        String sql = String.format("DELETE\nFROM FRIEND_STATUS\n " +
                "WHERE USER_ID = %d AND FRIEND_ID = %d", userId, friendId);
        return jdbcTemplate.update(sql) > 0;
    }

    @Override
    public List<User> getAllFriends(Integer id) {
        String sql = String.format("SELECT USERS.USER_ID, USERS.EMAIL, USERS.LOGIN, USERS.NAME, USERS.BIRTHDAY\n " +
                "FROM USERS\n JOIN FRIEND_STATUS FS on USERS.USER_ID = FS.FRIEND_ID\n " +
                "WHERE FS.USER_ID = ?", id);
        return jdbcTemplate.query(sql, this::mapRowToUser, id);
    }

    @Override
    public List<User> getCommonFriends(Integer userId, Integer friendId) {
        String sql = String.format("SELECT USERS.USER_ID, USERS.EMAIL, USERS.LOGIN, USERS.NAME, USERS.BIRTHDAY\n " +
                "FROM USERS" +
                " JOIN FRIEND_STATUS AS FS on USERS.USER_ID = FS.FRIEND_ID " +
                "WHERE FS.USER_ID = ? AND FS.FRIEND_ID IN" +
                "(SELECT FRIEND_ID " +
                "FROM FRIEND_STATUS " +
                "WHERE USER_ID = ?)", userId, friendId);
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