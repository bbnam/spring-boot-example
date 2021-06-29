package com.example.demo1.DTO;

import lombok.Data;

@Data
public class  UserDTO {
    private String id;
    private String username;
    private String email;


    public UserDTO(String id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

}
