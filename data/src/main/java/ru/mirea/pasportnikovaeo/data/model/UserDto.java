package ru.mirea.pasportnikovaeo.data.model;

import ru.mirea.pasportnikovaeo.domain.model.User;


public class UserDto {
    private String id;
    private String email;
    private String name;

    public UserDto(String id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public User toUser() {
        return new User(id, email, name);
    }
}