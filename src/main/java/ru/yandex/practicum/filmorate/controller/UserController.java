package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final static Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> findAll() {
        return userService.findAll();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Сохранили пользователя - " + user);
        user.setId(userService.create(user));
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Обновили пользователя - " + user);
        return userService.update(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public ResponseEntity<Boolean> addFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.info("Добавляем в друзья пользователя - " + userService.getUserById(friendId));
        return new ResponseEntity<>(userService.addFriend(id, friendId), HttpStatus.OK);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity<Boolean> deleteFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.info("Удаляем их друзей пользователя - " + userService.getUserById(friendId));
        return new ResponseEntity<>(userService.deleteFriend(id, friendId), HttpStatus.OK);
    }

    @GetMapping("/{id}/friends")
    public List<User> getUserFriends(@PathVariable Integer id) {
        log.info("Выводим друзей пользователя - " + userService.getUserById(id));
        return userService.getAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        log.info("Выводим общих друзей пользователя - " + userService.getUserById(id) + "и" + userService.getUserById(otherId));
        return userService.getCommonFriends(id, otherId);
    }
}