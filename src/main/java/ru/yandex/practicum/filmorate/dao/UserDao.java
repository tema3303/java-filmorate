package ru.yandex.practicum.filmorate.dao;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
public interface UserDao {

    Collection<User> findAll();

    int create(User user);

    User update(User user);

    Optional<User> getUserById(Integer id);

    Boolean sendRequestFriend(Integer userId, Integer friendId);

    Boolean deleteFriend(Integer userId, Integer friendId);

    List<User> getAllFriends(Integer id);

    List<User> getCommonFriends(Integer userId, Integer friendId);
}