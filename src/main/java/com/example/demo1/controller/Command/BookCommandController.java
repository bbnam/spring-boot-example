package com.example.demo1.controller.Command;


import com.example.demo1.DTO.*;
import com.example.demo1.model.Book;
import com.example.demo1.model.Message;
import com.example.demo1.service.Command.BookCommandService;
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
    public MessageResponseDTO AddBook(@RequestBody BookRequestDTO bookRequestDTO){
        BookKafka bookKafka = new BookKafka(0,
                bookRequestDTO.getName(),
                bookRequestDTO.getPublisher(),
                bookRequestDTO.getAmount(),
                0);

        bookCommandService.sendBookRequestToKafka(bookKafka);

        Message message = new Message("Thêm sách thành công!");

        return new MessageResponseDTO(0,200, message);
    }


    @PutMapping("/UpdateBook")
    public MessageResponseDTO UpdateBook(@RequestBody Book book){
        bookCommandService.updateBook(book);
        BookKafka bookKafka = new BookKafka(book.getId(),
                book.getName(),
                book.getPublisher(),
                book.getAmount(),
                1);

        bookCommandService.sendBookRequestToKafka(bookKafka);

        Message message = new Message("Cập nhập sách thành công!");

        return new MessageResponseDTO(0,200, message);
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
