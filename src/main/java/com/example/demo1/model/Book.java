package com.example.demo1.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Book {

    private int id;
    private String name;
    private String publisher;
    private int amount;

    public Book(int id, String name, String publisher, int amount ){
        this.id = id;
        this.name = name;
        this.publisher = publisher;
        this.amount = amount;
    }
}
