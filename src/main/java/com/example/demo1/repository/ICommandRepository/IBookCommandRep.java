package com.example.demo1.repository.ICommandRepository;

import com.example.demo1.model.Book;
import com.example.demo1.model.User;


public interface IBookCommandRep {
    int addBook(Book book);
    int updateBook(Book book);
    int userBook(long user_id, long book_id, String time_borrowed, String time_out);
    int updateAmount(long id);
    void addBookToHbase(Book book) throws Exception;
    void userBookToHbase (User user, Book book, String time_borrowed, String time_out) throws Exception;
    void updateBookHbase(Book book) throws Exception;
}
