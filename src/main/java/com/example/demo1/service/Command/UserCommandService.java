package com.example.demo1.service.Command;


import com.example.demo1.model.User;
import com.example.demo1.repository.imp.Command.UserCommadImp;
import org.springframework.stereotype.Service;

@Service
public class UserCommandService {
    private UserCommadImp userCommadImp;

    public void update_user(User user){
        userCommadImp.update(user);
    }
    public void signup(User user){
        userCommadImp.signup(user);
    }
}
