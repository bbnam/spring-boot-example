package com.example.demo1.service.Command;


import com.example.demo1.model.Book;
import com.example.demo1.model.User;
import com.example.demo1.repository.imp.Command.UserCommadImp;
import com.example.demo1.repository.imp.Query.UserQueryImp;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static sun.nio.cs.Surrogate.is;

@Service
public class UserCommandService {
    private UserCommadImp userCommadImp;
    private UserQueryImp userQueryImp;


    RestHighLevelClient client = new RestHighLevelClient(
            RestClient.builder(new HttpHost("localhost", 9200, "http")));

    public void update_user(User user){
        userCommadImp.update(user);
    }
    public void signup(User user){
        userCommadImp.signup(user);
    }

    @Bean
    public boolean createUserIndex(RestHighLevelClient restHighLevelClient) throws Exception {
        try{
            DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("doc-user");
            restHighLevelClient.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT); // 1

            DeleteIndexRequest deleteIndexRequest_book = new DeleteIndexRequest("book");
            restHighLevelClient.indices().delete(deleteIndexRequest_book, RequestOptions.DEFAULT); // 1
        } catch (Exception ignored) {
        }
        CreateIndexRequest createIndexRequest = new CreateIndexRequest("doc-user");

        Map<String, Object> username = new HashMap<>();
        username.put("type", "text");
        username.put("analyzer", "autocomplete");

        Map<String, Object> id = new HashMap<>();
        id.put("type", "long");

        Map<String, Object> properties = new HashMap<>();
        properties.put("username", username);
        properties.put("id", id);


        Map<String, Object> mapping = new HashMap<>();
        mapping.put("properties", properties);
        createIndexRequest.mapping(mapping);

        createIndexRequest.settings(Settings.builder()
                .put("index.number_of_shards", 1)
                .put("index.number_of_replicas", 0)
                .put("analysis.filter.autocomplete_filter.type", "ngram")
                .put("analysis.filter.autocomplete_filter.min_gram", "1")
                .put("analysis.filter.autocomplete_filter.max_gram", "1")
                .put("analysis.analyzer.autocomplete.type", "custom")
                .put("analysis.analyzer.autocomplete.tokenizer", "standard")
                .putList("analysis.analyzer.autocomplete.filter", "lowercase", "autocomplete_filter")
        );
        restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);


        // tao index book
        CreateIndexRequest createIndexRequest_2 = new CreateIndexRequest("book");

        createIndexRequest_2.settings(Settings.builder()
                .put("index.number_of_shards", 1)
                .put("index.number_of_replicas", 0)
        );
        restHighLevelClient.indices().create(createIndexRequest_2, RequestOptions.DEFAULT);

        return true;
    }

    public void shouldSaveUser(User user) throws JsonProcessingException, IOException {

        IndexRequest request = new IndexRequest("doc-user");
        request.id(String.valueOf(user.getId()));
        System.out.println(new ObjectMapper().writeValueAsString(user.getUsername()));
        request.source(new ObjectMapper().writeValueAsString(user), XContentType.JSON);
        IndexResponse indexResponse = client.index(request, RequestOptions.DEFAULT);


    }


}
