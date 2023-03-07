package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.impl.MpaDbStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

@Service
@AllArgsConstructor
public class MpaService {
    private MpaDbStorage mpaDbStorage;

    public Collection<Mpa> findAll() {
        return mpaDbStorage.findAll();
    }

    public Mpa getMpaById(Integer id) {
        return mpaDbStorage.getMpaById(id).orElseThrow(() -> new NotFoundException("MPA не найден"));
    }
}