package com.example.demo1.service.Query;

import com.example.demo1.DTO.UserDTO;
import com.example.demo1.model.User;
import com.example.demo1.repository.IQueryRepository.IUserQueryRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserQueryService {
    @Autowired
    private IUserQueryRep iUserQueryRep;

    public List<UserDTO> findAll(){
        return iUserQueryRep.findAll();
    }
}
