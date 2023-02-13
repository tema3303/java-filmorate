package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private int id;

    @NotEmpty
    @Email
    private final String email;

    @NotBlank
    @Pattern(regexp = "^\\S*")
    private final String login;

    private String name;

    @Past
    private final LocalDate birthday;

    Set<Integer> friends = new HashSet<>();

    public User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        if (name == null || name.isEmpty() || name.isBlank()) {
            this.name = login;
        } else {
            this.name = name;
        }
        this.birthday = birthday;
    }

    public void addFriend(Integer idUser) {
        friends.add(idUser);
    }

    public void deleteFriend(Integer idUser) {
        friends.remove(idUser);
    }
}