package com.example.demo1.repository.IQueryRepository;

import com.example.demo1.DTO.UserDTO;
import com.example.demo1.model.User;

import java.util.List;

public interface IUserQueryRep {
    List<UserDTO> findAll();
}
