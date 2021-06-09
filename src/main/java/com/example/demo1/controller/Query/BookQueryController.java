package com.example.demo1.controller.Query;


import com.example.demo1.model.Book;
import com.example.demo1.service.Query.BookQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Access;
import java.util.List;
import java.util.Set;

@RestController
public class BookQueryController {
    @Autowired
    private BookQueryService bookQueryService;

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
