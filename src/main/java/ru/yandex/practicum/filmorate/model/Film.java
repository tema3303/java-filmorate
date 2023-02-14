package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    private int id;

    @NotEmpty
    private final String name;

    @Size(max = 200)
    private final String description;

    private final LocalDate releaseDate;

    @Positive
    private final int duration;
    private Integer rating;
    private Set<Integer> userLikeFilm = new HashSet<>();

    public Film(String name, String description, LocalDate releaseDate, int duration, Integer rating) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        if (rating == null) {
            this.rating = 0;
        } else {
            this.rating = rating;
        }
    }

    public void addLike(Integer idUser) {
        userLikeFilm.add(idUser);
        rating = userLikeFilm.size();
    }

    public void deleteLike(Integer idUser) {
        rating = userLikeFilm.size();
        userLikeFilm.remove(idUser);
    }
}