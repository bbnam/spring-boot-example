package com.example.demo1.service.Query;

import com.example.demo1.model.Book;
import com.example.demo1.repository.imp.Query.BookQueryImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class BookQueryService {
    @Autowired
    private BookQueryImp bookQueryImp;

    public List<Book> findAll(){
        return  bookQueryImp.findAll();
    }
}
