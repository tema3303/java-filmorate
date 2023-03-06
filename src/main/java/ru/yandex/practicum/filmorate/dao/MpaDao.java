package ru.yandex.practicum.filmorate.dao;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.Optional;

@Component
public interface MpaDao {

    Collection<Mpa> findAll();

    Optional<Mpa> getMpaById(Integer id);
}