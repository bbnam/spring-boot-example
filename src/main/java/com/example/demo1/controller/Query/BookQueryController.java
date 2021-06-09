package com.example.demo1.controller.Query;


import com.example.demo1.model.Book;
import com.example.demo1.service.Query.BookQueryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class BookQueryController {
    private final BookQueryService bookQueryService;

    public BookQueryController(BookQueryService bookQueryService) {
        this.bookQueryService = bookQueryService;
    }

    @GetMapping("/GetBook")
    public List<Book> getBook(){
        return bookQueryService.findAll();
    }

    @PostMapping(value = "book")
    public List<Book> searchByName(@RequestBody Book name){
        if (name.getName().isEmpty()){
            return bookQueryService.findAllEl();
        }
        return bookQueryService.findByName(name.getName());
    }
}
