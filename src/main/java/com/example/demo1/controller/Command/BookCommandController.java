package com.example.demo1.controller.Command;


import com.example.demo1.DTO.UserBookDTO;
import com.example.demo1.model.Book;
import com.example.demo1.service.Command.BookCommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class BookCommandController {
    private final BookCommandService bookCommandService;

    public BookCommandController(BookCommandService bookCommandService) {
        this.bookCommandService = bookCommandService;
    }

    @PostMapping("/AddBook")
    public void AddBook(@RequestBody Book book){
        bookCommandService.addBook(book);
    }

    @PutMapping("/UpdateBook")
    public void UpdateBook(@RequestBody Book book){
        bookCommandService.updateBook(book);
    }

    @PostMapping("/userHasBook")
    public void UserHasBook(@RequestBody UserBookDTO userBookDTO){
        bookCommandService.userBook(userBookDTO);
    }


    @PostMapping("/ElasticsearchBook")
    public void test(@RequestBody Book book) throws IOException {
        bookCommandService.saveBookElasticsearch(book);
    }
}
