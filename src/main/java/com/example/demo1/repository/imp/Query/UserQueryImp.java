package com.example.demo1.repository.imp.Query;

import com.example.demo1.DTO.UserDTO;
import com.example.demo1.model.User;
import com.example.demo1.repository.IQueryRepository.IUserQueryRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserQueryImp implements IUserQueryRep {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<UserDTO> findAll() {
        return jdbcTemplate.query("select * from user", (rs, rowNum) -> new UserDTO(rs.getInt("id"), rs.getString("username"), rs.getString("email")));
    }

}
