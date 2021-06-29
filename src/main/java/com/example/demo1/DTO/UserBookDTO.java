package com.example.demo1.DTO;


import com.example.demo1.model.User;
import lombok.Data;

import java.util.List;

@Data
public class UserBookDTO {
    private User user;
    private List<BookDTO> book;

    public UserBookDTO(User user, List<BookDTO> book){
        this.user = user;
        this.book = book;
    }
}
