package ru.yandex.practicum.filmorate.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class ValidationUser {
    private final static Logger log = LoggerFactory.getLogger(ValidationUser.class);

    public static void validationUser(User user) {
        if (!user.getEmail().contains("@")) {
            log.info("Валидация не пройдена, по причине некорректного emaila");
            throw new ValidationException("электронная почта не может быть пустой и должна содержать символ - @");
        }
        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.info("Валидация не пройдена, по причине некорректного логина");
            throw new ValidationException("логин не может быть пустым и содержать пробелы");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.info("Валидация не пройдена, по причине некорректной даты");
            throw new ValidationException("дата рождения не может быть в будущем");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}