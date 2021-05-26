package com.example.demo1.repository.ICommandRepository;

import com.example.demo1.model.Book;


public interface IBookCommandRep {
    int addBook(Book book);
    int updateBook(Book book);
    int userBook(int user_id, int book_id, String time_borrowed, String time_out);
    int updateAmount(int id);
}
