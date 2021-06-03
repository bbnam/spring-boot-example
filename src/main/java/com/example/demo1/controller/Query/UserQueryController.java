package com.example.demo1.controller.Query;

import com.example.demo1.DTO.UserDTO;
import com.example.demo1.model.User;
import com.example.demo1.service.Query.UserQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
public class UserQueryController {
    @Autowired
    private UserQueryService userQueryService;
    @GetMapping("/GetUser")
    public List<UserDTO> addUser() {
        return userQueryService.findAll();
    }

    @GetMapping(value = "/alluser/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserDTO> searchByName(@PathVariable String username) {
        return userQueryService.findByName(username);
    }

}
