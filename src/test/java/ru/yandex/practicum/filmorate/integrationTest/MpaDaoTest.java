package ru.yandex.practicum.filmorate.integrationTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.impl.MpaDbStorage;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaDaoTest {

    private final MpaDbStorage mpaDbStorage;

    @Test
    public void testFindAll() {
        Assertions.assertEquals(5, mpaDbStorage.findAll().size(), "Размер списка не соответсвует");
    }

    @Test
    public void testGetMpaById() {
        Optional<Mpa> mpaOptional = mpaDbStorage.getMpaById(2);
        assertThat(mpaOptional)
                .isPresent()
                .hasValueSatisfying(mpa ->
                        assertThat(mpa).hasFieldOrPropertyWithValue("id", 2)
                );
    }
}