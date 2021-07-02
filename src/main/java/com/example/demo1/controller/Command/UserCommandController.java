package com.example.demo1.controller.Command;


import com.example.demo1.DTO.MessageResponseDTO;
import com.example.demo1.DTO.UserKafka;
import com.example.demo1.DTO.UserRequestDTO;
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

    @PostMapping("/updateUser")
    public MessageResponseDTO updateUser(@RequestBody User user) throws Exception{
        UserKafka userKafka = new UserKafka(user.getId(), user.getPassword(), user.getUsername(), user.getEmail(), 1);

        userCommandService.sendUserRequestToKafka(userKafka);

        Message message = new Message("Cập nhập thông tin thành công!");
        return new MessageResponseDTO(0,200, message);
    }

    @PostMapping("/ElasticsearchUser")
    public void test(@RequestBody User user) throws IOException {
        userCommandService.saveUserToElasticsearch(user);
    }

    @PostMapping("/signUp")
    public MessageResponseDTO saveUser (@RequestBody UserRequestDTO user) throws Exception{
        UserKafka userKafka = new UserKafka(0, user.getPassword(), user.getUsername(), user.getEmail(), 0);
        userCommandService.sendUserRequestToKafka(userKafka);

        Message message = new Message("Đăng ký thành công!");

        return new MessageResponseDTO(0,200, message);
    }


}

