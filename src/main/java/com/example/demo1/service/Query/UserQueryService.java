package com.example.demo1.service.Query;

import com.example.demo1.DTO.UserDTO;
import com.example.demo1.DTO.UserEmailDTO;
import com.example.demo1.DTO.UserResponseDTO;
import com.example.demo1.repository.IQueryRepository.IUserQueryRep;
import com.example.demo1.service.SequenceGenerator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserQueryService {

    private final IUserQueryRep iUserQueryRep;

    public UserQueryService(IUserQueryRep iUserQueryRep) {
        this.iUserQueryRep = iUserQueryRep;
    }

    public List<UserDTO> findAll(){
        return iUserQueryRep.findAll();
    }
    public List<UserDTO> findByNameAndEmail(UserEmailDTO userEmailDTO) {
        return  iUserQueryRep.findByNameAndEmail(userEmailDTO.getUsername(), userEmailDTO.getEmail());
    }

    public UserResponseDTO findAllFromHbase() throws Exception {
        SequenceGenerator id = new SequenceGenerator(0);
        System.out.println(id.nextId());
        return new UserResponseDTO(0,200, iUserQueryRep.findAllFromHbase());
    }
}