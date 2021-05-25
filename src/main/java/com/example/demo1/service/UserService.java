package com.example.demo1.service;

import com.example.demo1.model.User;
import com.example.demo1.repository.UserRep;
import com.example.demo1.repository.imp.UserImplements;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserService {
    private final UserRep userRep;

    public UserService(UserRep userRep) {
        this.userRep = userRep;
    }

    public List<User> findAll(){
        return userRep.findAll();
    }
    public void update_user(User user){
        userRep.update(user);
    }
    public void signup(User user){
        userRep.signup(user);
    }

}
