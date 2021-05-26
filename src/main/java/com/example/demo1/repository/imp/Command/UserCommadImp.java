package com.example.demo1.repository.imp.Command;


import com.example.demo1.model.User;
import com.example.demo1.repository.ICommandRepository.IUserCommandRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserCommadImp implements IUserCommandRep {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void update(User user) {
         jdbcTemplate.update("UPDATE user SET email = ?, password = ? WHERE (username = ?);", user.getEmail(), user.getPassword(), user.getUsername());
    }

    @Override
    public void signup(User user){
         jdbcTemplate.update("INSERT INTO `mydb`.`user`(username , password, email) " + "VALUES(?,?,?);", user.getUsername(), user.getPassword(), user.getEmail());
    }
}
