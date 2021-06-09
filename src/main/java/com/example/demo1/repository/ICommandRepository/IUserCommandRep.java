package com.example.demo1.repository.ICommandRepository;

import com.example.demo1.model.User;
import org.elasticsearch.client.RestHighLevelClient;

import java.util.concurrent.ExecutionException;

public interface IUserCommandRep {
    void update(User user);
    void signup(User user);


}
