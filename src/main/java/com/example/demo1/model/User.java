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

    private long id;
    private String email;

}
