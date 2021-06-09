package com.example.demo1.service.Query;

import com.example.demo1.model.Book;
import com.example.demo1.repository.imp.Query.BookQueryImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class BookQueryService {
    @Autowired
    private BookQueryImp bookQueryImp;

    public List<Book> findAll(){
        return  bookQueryImp.findAll();
    }

    public List<Book> findByName(String name){return bookQueryImp.findByName(name);}
    public List<Book> findAllEl(){
        return bookQueryImp.findAllEl();
    }
}
