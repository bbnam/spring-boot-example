package com.example.demo1.repository.imp;

import com.example.demo1.model.User;
import com.example.demo1.repository.UserRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

@Repository
public class UserImplements implements UserRep {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<User> findAll(){
        return jdbcTemplate.query("select * from user", (rs, rowNum) -> new User(rs.getString("username"), rs.getString("email")));
    }

    @Override
    public int update(User user) {
        return jdbcTemplate.update("UPDATE `mydb`.`user` SET `email` = ?, `password` = ? WHERE (`username` = ?);", user.getEmail(), user.getPassword(), user.getUsername());
    }
}
