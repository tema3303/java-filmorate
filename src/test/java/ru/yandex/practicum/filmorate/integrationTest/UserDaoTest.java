package ru.yandex.practicum.filmorate.integrationTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.impl.FilmDbStorage;
import ru.yandex.practicum.filmorate.dao.impl.GenreDbStorage;
import ru.yandex.practicum.filmorate.dao.impl.UserDbStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDaoTest {
    private final FilmDbStorage filmDbStorage;
    private final GenreDbStorage genreDbStorage;
    private final UserDbStorage userDbStorage;

    @BeforeEach
    void createUser() {
        if (filmDbStorage.findAll().size() != 2) {
            List<Genre> genres = new ArrayList<>();
            genres.add(new Genre(2, genreDbStorage.getGenreById(2)));
            genres.add(new Genre(3, genreDbStorage.getGenreById(3)));
            Film film = new Film("Гарри Поттер 1", "Про волшебников", LocalDate.parse("2001-12-23"),
                    120, 0, new Mpa(1, "G"), genres);
            filmDbStorage.create(film);
            film.setGenres(genres);
            Film film2 = new Film("Друзья", "Про друзей", LocalDate.parse("2004-12-23"),
                    120, 0, new Mpa(4, "R"), genres);
            filmDbStorage.create(film2);
            film2.setGenres(genres);
        }
        if (userDbStorage.findAll().size() != 2) {
            User user = new User("123@mail.com", "Artem33", "Artem", LocalDate.parse("1998-12-23"));
            userDbStorage.create(user);
            User user2 = new User("12333@mail.com", "Anton33", "Anton", LocalDate.parse("1998-12-23"));
            userDbStorage.create(user2);
        }
    }

    @Test
    public void testCreateUser() {
        User user = new User("1234@mail.com", "Anton33", "Mike", LocalDate.parse("1998-12-23"));
        userDbStorage.create(user);
        Assertions.assertEquals("Mike", user.getName(), "Пользователь не создался");
    }

    @Test
    public void testFindUserById() {
        Optional<User> filmOptional = userDbStorage.getUserById(1);
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    public void testGetAllUsers() {
        List<User> users = userDbStorage.findAll();
        Assertions.assertEquals(2, users.size(), "Число пользователей не совпадает");
    }

    @Test
    public void testUpdateUser() {
        User user = new User("1234@mail.com", "Anton33", "Anton", LocalDate.parse("1998-12-23"));
        user.setId(1);
        userDbStorage.create(user);
        Assertions.assertEquals("1234@mail.com", user.getEmail(), "Email не совпадает");
    }

    @Test
    public void testSendRequestFriend() {
        Assertions.assertTrue(userDbStorage.sendRequestFriend(1, 2), "Запрос не отправлен");
    }

    @Test
    public void testDeleteFriend() {
        userDbStorage.sendRequestFriend(1, 2);
        Assertions.assertTrue(userDbStorage.deleteFriend(1, 2), "Друг не удален");
    }

    @Test
    public void testGetСommonFriend() {
        User user = new User("123@mail.com", "Artem33", "Artem", LocalDate.parse("1998-12-23"));
        userDbStorage.create(user);
        User user2 = new User("14443@mail.com", "A3", "Artem", LocalDate.parse("1998-12-23"));
        userDbStorage.create(user);
        userDbStorage.create(user2);
        userDbStorage.sendRequestFriend(1, 2);
        userDbStorage.sendRequestFriend(1, 3);
        userDbStorage.sendRequestFriend(2, 3);
        userDbStorage.sendRequestFriend(2, 4);
        userDbStorage.sendRequestFriend(1, 4);
        List<User> friends = userDbStorage.getCommonFriends(1, 2);
        Assertions.assertEquals(2, friends.size(), "Количество друзей не совпадает");
    }

    @Test
    public void testGetAllFriend() {
        userDbStorage.sendRequestFriend(1, 2);
        List<User> friends = userDbStorage.getAllFriends(1);
        Assertions.assertEquals(4, friends.size(), "Количество друзей не совпадает");
    }
}