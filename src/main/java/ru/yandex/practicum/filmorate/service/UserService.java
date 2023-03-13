package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.impl.UserDbStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private UserDbStorage userDbStorage;

    public Collection<User> findAll() {
        return userDbStorage.findAll();
    }

    public int create(User user) {
        return userDbStorage.create(user);
    }

    public User update(User user) {
        correctId(user.getId());
        return userDbStorage.update(user);
    }

    public User getUserById(Integer id) {
        return userDbStorage.getUserById(id).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    public Boolean addFriend(Integer idUser, Integer idFriend) {
        correctId(idUser);
        correctId(idFriend);
        return userDbStorage.sendRequestFriend(idUser, idFriend);
    }

    public Boolean deleteFriend(Integer idUser, Integer idFriend) {
        correctId(idUser);
        correctId(idFriend);
        return userDbStorage.deleteFriend(idUser, idFriend);
    }

    public List<User> getAllFriends(Integer idUser) {
        return userDbStorage.getAllFriends(idUser);
    }

    public List<User> getCommonFriends(Integer idUser1, Integer idUser2) {
        correctId(idUser1);
        correctId(idUser2);
        return userDbStorage.getCommonFriends(idUser1, idUser2);
    }

    private Boolean correctId(Integer id) {
        if (userDbStorage.getUserById(id).isEmpty()) {
            throw new NotFoundException("Id пользователя отсуствует в списке");
        } else {
            return true;
        }
    }
}