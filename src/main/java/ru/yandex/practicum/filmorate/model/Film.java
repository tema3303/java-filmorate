package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.*;

@Data
public class Film {
    private int id;

    @NotEmpty
    private final String name;

    @Size(max = 200)
    private final String description;

    private final LocalDate releaseDate;

    @Positive
    private final Integer duration;
    private Integer rate; //количество лайков
    private Mpa mpa;
    private Collection<Genre> genres;

    public Film(String name, String description, LocalDate releaseDate, int duration, Integer rate, Mpa mpa,
                List<Genre> genres) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        if (rate == null) {
            this.rate = 0;
        } else {
            this.rate = rate;
        }
        this.mpa = mpa;
        if (genres == null) {
            this.genres = new ArrayList<>();
        } else {
            this.genres = genres;
        }
    }

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("NAME", name);
        values.put("DESCRIPTION", description);
        values.put("RELEASE_DATE", releaseDate);
        values.put("DURATION", duration);
        values.put("RATE", rate);
        values.put("MPA_ID", mpa.getId());
        return values;
    }
}