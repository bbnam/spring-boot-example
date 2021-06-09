package com.example.demo1.controller.Command;


import com.example.demo1.model.User;
import com.example.demo1.repository.imp.Command.UserCommadImp;
import com.example.demo1.service.Command.UserCommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class UserCommandController {
    @Autowired
    private UserCommandService userCommandService;
    private UserCommadImp userCommadImp;
    @PostMapping("/PostUser")
    public void updateUser(@RequestBody User user){
        userCommandService.update_user(user);

    }

    @PostMapping("/SignUp")
    public void signUp(@RequestBody User user){
        userCommandService.signup(user);
    }

    @PostMapping("/ElasticsearchUser")
    public void test(@RequestBody User user) throws IOException {
        userCommandService.shouldSaveUser(user);
    }


}

