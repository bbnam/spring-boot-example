package com.example.demo1.DTO;


import com.example.demo1.model.Book;
import lombok.Data;

import java.util.List;

@Data
public class UserBookDTO {
    private int id;
    private List<BookDTO> book;

    public UserBookDTO(int id, List<BookDTO> book){
        this.id = id;
        this.book = book;
    }
}
