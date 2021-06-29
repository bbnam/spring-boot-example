package com.example.demo1.controller.Command;


import com.example.demo1.DTO.MessageResponseDTO;
import com.example.demo1.model.Book;
import com.example.demo1.model.Message;
import com.example.demo1.model.User;
import com.example.demo1.service.Command.UserCommandService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class UserCommandController {
    private final UserCommandService userCommandService;

    public UserCommandController(UserCommandService userCommandService) {
        this.userCommandService = userCommandService;
    }

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
        userCommandService.saveUserToElasticsearch(user);
        userCommandService.sendToKafka(user);
    }

    @PostMapping("/test")
    public void test_hbase (@RequestBody User user) throws Exception {
        userCommandService.saveToHbase(user);
    }

    @PostMapping("/bookHbase")
    public MessageResponseDTO updateUserHbase(@RequestBody User user) throws Exception{
        userCommandService.updateUserHbase(user);
        Message message = new Message("Cập nhập thông tin thành công!");
        return new MessageResponseDTO(0,200, message);
    }

}

