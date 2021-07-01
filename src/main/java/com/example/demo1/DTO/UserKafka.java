package com.example.demo1.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserKafka {
    private long id;
    private String password;
    private String username;
    private String email;
    private int code;
}
