package com.example.demo1.DTO;

import com.example.demo1.model.Book;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBookResponseDTO {
    private UserDTO userDTO;
    private Book book;
    private String time_borrowed;
    private String time_back;
}
