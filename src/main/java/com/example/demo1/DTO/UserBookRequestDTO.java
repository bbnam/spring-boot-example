package com.example.demo1.DTO;

import com.example.demo1.model.Book;
import com.example.demo1.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;




@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBookRequestDTO {
    private UserDTO userDTO;
    private Book book;
    private String time_back;
}
