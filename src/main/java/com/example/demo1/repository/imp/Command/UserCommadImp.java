package com.example.demo1.repository.imp.Command;


import com.example.demo1.model.User;
import com.example.demo1.repository.ICommandRepository.IUserCommandRep;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Repository
public class UserCommadImp implements IUserCommandRep {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private ObjectMapper objectMapper;

    RestHighLevelClient client = new RestHighLevelClient(
            RestClient.builder(new HttpHost("localhost", 9200, "http")));

    @Override
    public void update(User user) {
         jdbcTemplate.update("UPDATE user SET email = ?, password = ? WHERE (username = ?);", user.getEmail(), user.getPassword(), user.getUsername());
    }

    @Override
    public void signup(User user){
         jdbcTemplate.update("INSERT INTO `mydb`.`user`(username , password, email) " + "VALUES(?,?,?);", user.getUsername(), user.getPassword(), user.getEmail());
    }




}
