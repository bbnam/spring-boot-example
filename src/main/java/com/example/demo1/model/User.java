package com.example.demo1.model;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import javax.persistence.Id;

@Getter
@Setter
//@Document(indexName = "doc-user")
public class User {

//    @Field(type = FieldType.Text, analyzer = "analysis")
    private String username;
    private String password;
//    @Id
    private int id;
    private String email;

    public User(String username, String email, int id) {
        this.username = username;
        this.email = email;
        this.id = id;
    }
}
