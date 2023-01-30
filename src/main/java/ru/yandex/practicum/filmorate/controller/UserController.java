package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.ValidationUser;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int generator = 1;
    private final static Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        ValidationUser.validationUser(user);
        int userId = generator++;
        user.setId(userId);
        users.put(user.getId(), user);
        log.info("Сохранили пользователя - " + user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) {
        ValidationUser.validationUser(user);
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Id фильма отсуствует в списке");
        }
        users.put(user.getId(), user);
        log.info("Обновили пользователя - " + user);
        return user;
    }
}