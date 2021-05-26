package com.example.demo1.controller.Command;


import com.example.demo1.DTO.UserBookDTO;
import com.example.demo1.model.Book;
import com.example.demo1.service.Command.BookCommandService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookCommandController {
    private BookCommandService bookCommandService;
    @PostMapping("/AddBook")
    public void AddBook(@RequestBody Book book){
        bookCommandService.addBook(book);
    }

    @PutMapping("/UpdateBook")
    public void UpdateBook(@RequestBody Book book){
        bookCommandService.updateBook(book);
    }

    @PostMapping("/userhasbook")
    public void UserHasBook(@RequestBody UserBookDTO userBookDTO){
        bookCommandService.userBook(userBookDTO);
    }

}
