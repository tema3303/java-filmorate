package ru.yandex.practicum.filmorate.integrationTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.impl.FilmDbStorage;
import ru.yandex.practicum.filmorate.dao.impl.GenreDbStorage;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreDaoTest {
    private final GenreDbStorage genreDbStorage;

    @Test
    public void testFindAll() {
        Assertions.assertEquals(6, genreDbStorage.findAll().size(), "Размер списка не соответсвует");
    }

    @Test
    public void testGetGenreById() {
        Assertions.assertEquals("Комедия", genreDbStorage.getGenreById(1), "Размер списка не соответсвует");
    }
}