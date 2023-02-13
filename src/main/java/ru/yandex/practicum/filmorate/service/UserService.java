package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class UserService {

    InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public Collection<User> findAll() {
        return inMemoryUserStorage.findAll();
    }

    public User create(User user) {
        return inMemoryUserStorage.create(user);
    }

    public User update(User user) {
        return inMemoryUserStorage.update(user);
    }

    public void delete(Integer id) {
        inMemoryUserStorage.delete(id);
    }

    public User getUserById(Integer id) {
        return inMemoryUserStorage.getUserById(id);
    }

    public void addFriend(Integer idUser, Integer idFriend) {
        inMemoryUserStorage.getUserById(idUser).addFriend(idFriend);
        inMemoryUserStorage.getUserById(idFriend).addFriend(idUser);
    }

    public void deleteFriend(Integer idUser, Integer idFriend) {
        inMemoryUserStorage.getUserById(idUser).deleteFriend(idFriend);
        inMemoryUserStorage.getUserById(idFriend).deleteFriend(idUser);
    }

    public List<User> getAllFriends(Integer idUser) {
        List<User> friends = new ArrayList<>();
        for (Integer idFriend : inMemoryUserStorage.getUserById(idUser).getFriends()) {
            friends.add(inMemoryUserStorage.getUserById(idFriend));
        }
        return friends;
    }

    public List<User> getCommonFriends(Integer idUser1, Integer idUser2) {
        List<User> friends = new ArrayList<>();
        for (Integer idFriend : inMemoryUserStorage.getUserById(idUser1).getFriends()) {
            if (inMemoryUserStorage.getUserById(idUser2).getFriends().contains(idFriend)) {
                friends.add(inMemoryUserStorage.getUserById(idFriend));
            }
        }
        return friends;
    }
}