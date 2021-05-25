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
        return jdbcTemplate.query("select * from user", (rs, rowNum) -> new User(rs.getString("username"), rs.getString("email"), rs.getInt("id")));
    }

    @Override
    public int update(User user) {
        return jdbcTemplate.update("UPDATE user SET email = ?, password = ? WHERE (username = ?);", user.getEmail(), user.getPassword(), user.getUsername());
    }

    @Override
    public int signup(User user){
        return jdbcTemplate.update("INSERT INTO `mydb`.`user`(username , password, email) " + "VALUES(?,?,?);", user.getUsername(), user.getPassword(), user.getEmail());
    }
}
