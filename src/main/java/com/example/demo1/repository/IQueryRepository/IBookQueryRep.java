package com.example.demo1.repository.IQueryRepository;

import com.example.demo1.DTO.BookDTO;
import com.example.demo1.model.Book;

import java.util.List;
import java.util.Set;

public interface IBookQueryRep {
    List<Book> findAll();
    List<Book> findByName(String name);
    List<Book> findAllEl();
}
