package com.example.demo1.repository.IQueryRepository;

import com.example.demo1.DTO.UserDTO;

import java.util.List;


public interface IUserQueryRep {
    List<UserDTO> findAll();
    List<UserDTO> findByNameAndEmail(String name, String email);

}
