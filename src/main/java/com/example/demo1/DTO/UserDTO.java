package com.example.demo1.DTO;

import com.example.demo1.model.User;
import lombok.Data;

@Data
public class  UserDTO {
    private int id;
    private String username;
    private String email;

    UserDTO(){
        super();
    }



    public UserDTO(int id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public UserDTO(User user){
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
    }
}
