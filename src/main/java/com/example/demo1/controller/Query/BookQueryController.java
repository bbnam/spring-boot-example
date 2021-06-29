package com.example.demo1.controller.Query;


import com.example.demo1.DTO.BookResponseDTO;
import com.example.demo1.model.Book;
import com.example.demo1.service.Query.BookQueryService;
import org.json.JSONException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
public class BookQueryController {
    private final BookQueryService bookQueryService;

    public BookQueryController(BookQueryService bookQueryService) {
        this.bookQueryService = bookQueryService;
    }

    @GetMapping(value = "/GetBook")
    public ResponseEntity<BookResponseDTO> allBook() throws JSONException {

        return ResponseEntity.ok().body(bookQueryService.findAll());

    }

    @PostMapping(value = "book")
    public List<Book> searchByName(@RequestBody Book name){
        if (name.getName().isEmpty()){
            return bookQueryService.findAllEl();
        }
        return bookQueryService.findByName(name.getName());
    }

    @GetMapping(value = "/bookHbase")
    public BookResponseDTO allBookHbase() throws Exception{
        return bookQueryService.findAllBookHbase();
    }
}
