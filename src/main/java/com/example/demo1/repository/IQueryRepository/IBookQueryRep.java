package com.example.demo1.repository.IQueryRepository;

import com.example.demo1.model.Book;

import java.util.List;

public interface IBookQueryRep {
    List<Book> findAll();
    List<Book> findByName(String name);
}
