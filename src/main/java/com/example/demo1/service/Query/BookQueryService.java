package com.example.demo1.service.Query;

import com.example.demo1.DTO.BookResponseDTO;
import com.example.demo1.model.Book;
import com.example.demo1.repository.imp.Query.BookQueryImp;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class BookQueryService {
    @Autowired
    private BookQueryImp bookQueryImp;

    public BookResponseDTO findAll() throws JSONException {

        return new BookResponseDTO(0, 200,bookQueryImp.findAll());
    }

    public List<Book> findByName(String name){return bookQueryImp.findByName(name);}
    public List<Book> findAllEl(){
        return bookQueryImp.findAllEl();
    }

    public BookResponseDTO findAllBookHbase() throws Exception{
        return new BookResponseDTO(0,200,bookQueryImp.findAllHbase());
    }
}
