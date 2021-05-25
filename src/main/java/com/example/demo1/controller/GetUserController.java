package com.example.demo1.controller;


import com.example.demo1.model.User;
import com.example.demo1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GetUserController {
    private final UserService userService;
    @Autowired
    public GetUserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/GetUser")
    public List<User> addUser(){
        return userService.findAll();
    }

    @PostMapping("/PostUser")
    public List<User> updateUser(@RequestBody User user){
        userService.update_user(user);
        return userService.findAll();
    }

    @PostMapping("/SignUp")
    public String signUp(@RequestBody User user){
        userService.signup(user);
        return "Đăng ký thành công";
    }
}
