package ru.yandex.practicum.filmorate.validations;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.ValidationUser;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserTest {

    @Test
    void createUserFailMail() throws ValidationException {
        User user = User.builder().email("123mail.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(2000, 11, 22))
                .build();
        Throwable throwable = assertThrows(ValidationException.class, () -> {
            ValidationUser.validationUser(user);
        });
        assertEquals(throwable.getMessage(), "электронная почта не может быть пустой и должна содержать символ - @");
    }

    @Test
    void createUserFailLogin() throws ValidationException {
        User user = User.builder().email("123@mail.ru")
                .login(" ")
                .name("name")
                .birthday(LocalDate.of(2000, 11, 22))
                .build();
        Throwable throwable = assertThrows(ValidationException.class, () -> {
            ValidationUser.validationUser(user);
        });
        assertEquals(throwable.getMessage(), "логин не может быть пустым и содержать пробелы");
    }

    @Test
    void createUserFailBirthday() throws ValidationException {
        User user = User.builder().email("123@gmail.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(2025, 11, 22))
                .build();
        Throwable throwable = assertThrows(ValidationException.class, () -> {
            ValidationUser.validationUser(user);
        });
        assertEquals(throwable.getMessage(), "дата рождения не может быть в будущем");
    }
}