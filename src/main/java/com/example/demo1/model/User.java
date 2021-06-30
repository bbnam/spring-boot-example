package com.example.demo1.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class User {

    private String username;
    private String password;

    private int id;
    private String email;

    public User(String username, String email, int id) {
        this.username = username;
        this.email = email;
        this.id = id;
    }
}
