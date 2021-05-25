package com.example.demo1.repository;

import com.example.demo1.DTO.BookDTO;
import com.example.demo1.DTO.UserBookDTO;
import com.example.demo1.model.Book;

import java.util.Date;
import java.util.List;

public interface BookRep {
    List<Book> findAll();
    int addBook(Book book);
    int updateBook(Book book);
    int userBook(int user_id, int book_id, String time_borrowed, String time_out);
    int updateAmount(int amount, int id);
    int findAmount(int book_id);

}
