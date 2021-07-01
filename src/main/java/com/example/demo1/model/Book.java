package com.example.demo1.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;

@Getter
@Setter
@Document(indexName = "")
public class Book {

    private long  id;
    private String name;
    private String publisher;
    private int amount;

    public Book(long id, String name, String publisher, int amount ){
        this.id = id;
        this.name = name;
        this.publisher = publisher;
        this.amount = amount;
    }

}
