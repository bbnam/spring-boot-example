package com.example.demo1.repository.imp.Query;


import com.example.demo1.model.Book;
import com.example.demo1.repository.IQueryRepository.IBookQueryRep;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookQueryImp implements IBookQueryRep{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Book> findAll() {
        return jdbcTemplate.query("Select * from book", (rs, rowNum) -> new Book(rs.getInt("id"), rs.getString("name"), rs.getString("publisher"), rs.getInt("amount")));
    }

    @Override
    public List<Book> findByName(String name) {


        return null;
    }
}
