package com.example.demo1.controller.Query;


import com.example.demo1.model.Book;
import com.example.demo1.service.Query.BookQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Access;
import java.util.List;

@RestController
public class BookQueryController {
    @Autowired
    private BookQueryService bookQueryService;

    @GetMapping("/GetBook")
    public List<Book> getBook(){
        return bookQueryService.findAll();
    }

    @GetMapping(value = "book/{name}")
    public List<Book> searchByName(@PathVariable String name){
        return bookQueryService.findByName(name);
    }
}
