package com.example.demo1.controller;


import com.example.demo1.DTO.UserBookDTO;
import com.example.demo1.model.Book;
import com.example.demo1.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BookController {

    @Autowired
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/GetBook")
    public List<Book> getBook(){
        return bookService.findAll();
    }

    @PostMapping("/AddBook")
    public String AddBook(@RequestBody Book book){
        return bookService.addBook(book);
    }

    @PutMapping("/UpdateBook")
    public String UpdateBook(@RequestBody Book book){
        return bookService.updateBook(book);
    }

    @PostMapping("/userhasbook")
    public String UserHasBook(@RequestBody UserBookDTO userBookDTO){
        return bookService.userBook(userBookDTO);
    }
}
