package ru.yandex.practicum.filmorate.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class ValidationUser {
    private final static Logger log = LoggerFactory.getLogger(ValidationUser.class);

    public static void validationUser(User user) {
        log.info("Так как не указано имя пользователя, имя заменено на введенный логин");
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}