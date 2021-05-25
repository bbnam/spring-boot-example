package com.example.demo1.repository;

import com.example.demo1.model.User;

import java.util.List;

public interface UserRep {
    List<User> findAll();
    int update(User user);
    int signup(User user);
    }


