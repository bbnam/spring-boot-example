package com.example.demo1.controller.Command;


import com.example.demo1.model.User;
import com.example.demo1.service.Command.UserCommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserCommandController {
    @Autowired
    private UserCommandService userCommandService;

    @PostMapping("/PostUser")
    public void updateUser(@RequestBody User user){
        userCommandService.update_user(user);

    }

    @PostMapping("/SignUp")
    public void signUp(@RequestBody User user){
        userCommandService.signup(user);
    }
}

