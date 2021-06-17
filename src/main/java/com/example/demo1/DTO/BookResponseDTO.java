package com.example.demo1.DTO;

import com.example.demo1.model.Book;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;


@Data
public class BookResponseDTO {
    private int status;
    private HttpStatus code;
    private List<Book> result;

    public BookResponseDTO(int status, int code, List<Book> result){
        this.status = status;
        this.code = code;
        this.result = result;
    }
}
