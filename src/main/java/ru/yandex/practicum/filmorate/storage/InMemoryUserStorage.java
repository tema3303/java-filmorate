package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();
    private int generator = 1;

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public User create(User user) {
        int userId = generator++;
        user.setId(userId);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        notContain(user.getId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User getUserById(Integer id) {
        notContain(id);
        return users.get(id);
    }

    private Boolean notContain(Integer id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("Id пользователя отсуствует в списке");
        } else {
            return true;
        }
    }

}