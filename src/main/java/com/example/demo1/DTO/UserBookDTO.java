package com.example.demo1.DTO;


import com.example.demo1.model.Book;
import com.example.demo1.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBookDTO {
    private UserDTO userDTO;
    private Book book;
    private String time_borrowed;
    private String time_back;
    private int status;

}
