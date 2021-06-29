package com.example.demo1.controller.Query;

import com.example.demo1.DTO.UserDTO;
import com.example.demo1.DTO.UserEmailDTO;
import com.example.demo1.DTO.UserResponseDTO;
import com.example.demo1.service.Query.UserQueryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
public class UserQueryController {
    private final UserQueryService userQueryService;

    public UserQueryController(UserQueryService userQueryService) {
        this.userQueryService = userQueryService;
    }

    @GetMapping("/GetUser")
    public List<UserDTO> addUser() {
        return userQueryService.findAll();
    }

    @PostMapping(value = "/allUser")
    public List<UserDTO> searchByName(@RequestBody UserEmailDTO userEmailDTO) {
        return userQueryService.findByNameAndEmail(userEmailDTO);
    }

    @GetMapping(value = "/listUserHbase")
    public UserResponseDTO AllUserFromHbase() throws Exception{
        return userQueryService.findAllFromHbase();
    }
}
