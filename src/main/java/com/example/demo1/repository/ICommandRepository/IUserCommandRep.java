package com.example.demo1.repository.ICommandRepository;

import com.example.demo1.model.User;


public interface IUserCommandRep {
    void update(User user);
    void signup(User user);
    void saveUserToHbase (User user) throws Exception;
    void updateUserHbase(User user) throws Exception;
}
