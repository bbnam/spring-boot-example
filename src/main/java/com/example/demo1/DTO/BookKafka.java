package com.example.demo1.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookKafka {
    private long  id;
    private String name;
    private String publisher;
    private int amount;
    private int code;
}
