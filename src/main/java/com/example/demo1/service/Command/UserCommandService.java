package com.example.demo1.service.Command;


import com.example.demo1.DTO.UserRequestDTO;
import com.example.demo1.model.Book;
import com.example.demo1.model.User;
import com.example.demo1.repository.imp.Command.UserCommadImp;
import com.example.demo1.service.SequenceGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;


@Service
public class UserCommandService {
    @Autowired
    private final UserCommadImp userCommadImp;

    @Autowired
    private final KafkaTemplate<String, UserRequestDTO> kafkaTemplate;

    final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();


    public RestHighLevelClient test(){
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials("admin", "rIOwtIeDGQiPjWQbUtHm"));

        return new RestHighLevelClient(
                RestClient.builder(new HttpHost("https://10.3.104.30/", 9200, "http"))
                        .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
                                .setDefaultCredentialsProvider(credentialsProvider)
                        ));
    }



    public UserCommandService(UserCommadImp userCommadImp,  KafkaTemplate<String, UserRequestDTO> kafkaTemplate) {
        this.userCommadImp = userCommadImp;

        this.kafkaTemplate = kafkaTemplate;
    }

    public void update_user(User user){
        userCommadImp.update(user);
    }

//    @Bean
//    public boolean createUserIndex() throws Exception {
//
//        credentialsProvider.setCredentials(AuthScope.ANY,
//                new UsernamePasswordCredentials("admin", "rIOwtIeDGQiPjWQbUtHm"));
//
//        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(
//                RestClient.builder(new HttpHost("10.3.104.30", 9200, "http"))
//                        .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
//                                .setDefaultCredentialsProvider(credentialsProvider)
//                        ));
//
//        CreateIndexRequest createIndexRequest = new CreateIndexRequest("bbnam-doc-user");
//
//        Map<String, Object> username = new HashMap<>();
//        username.put("type", "text");
//        username.put("analyzer", "autocomplete");
//
//        Map<String, Object> id = new HashMap<>();
//        id.put("type", "long");
//
//        Map<String, Object> properties = new HashMap<>();
//        properties.put("username", username);
//        properties.put("id", id);
//
//
//        Map<String, Object> mapping = new HashMap<>();
//        mapping.put("properties", properties);
//        createIndexRequest.mapping(mapping);
//
//        createIndexRequest.settings(Settings.builder()
//                .put("index.number_of_shards", 1)
//                .put("index.number_of_replicas", 0)
//                .put("analysis.filter.autocomplete_filter.type", "ngram")
//                .put("analysis.filter.autocomplete_filter.min_gram", "1")
//                .put("analysis.filter.autocomplete_filter.max_gram", "1")
//                .put("analysis.analyzer.autocomplete.type", "custom")
//                .put("analysis.analyzer.autocomplete.tokenizer", "standard")
//                .putList("analysis.analyzer.autocomplete.filter", "lowercase", "autocomplete_filter")
//        );
//        restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
//
//
//        // tao index book
//        CreateIndexRequest createIndexRequest_2 = new CreateIndexRequest("bbnam-doc-book");
//
//        createIndexRequest_2.settings(Settings.builder()
//                .put("index.number_of_shards", 1)
//                .put("index.number_of_replicas", 0)
//        );
//        restHighLevelClient.indices().create(createIndexRequest_2, RequestOptions.DEFAULT);
//
//        return true;
//    }

    public void saveUserToElasticsearch (User user) throws IOException {

        IndexRequest request = new IndexRequest("bbnam-doc-user");
        request.id(String.valueOf(user.getId()));
        System.out.println(new ObjectMapper().writeValueAsString(user.getUsername()));
        request.source(new ObjectMapper().writeValueAsString(user), XContentType.JSON);
        IndexResponse indexResponse = test().index(request, RequestOptions.DEFAULT);

    }



    public void updateUserHbase(User user) throws Exception{
        userCommadImp.updateUserHbase(user);
    }


    public void sendUserRequestToKafka(UserRequestDTO user){
        kafkaTemplate.send("book_lib",0, null , user);
    }

    @KafkaListener(groupId = "book_lib", topicPartitions = @TopicPartition(
            topic = "book_lib",
            partitions = "0"))
    public void listen1(UserRequestDTO userRequestDTO) throws Exception {
        SequenceGenerator sequenceGenerator = new SequenceGenerator();
        UUID uuid = UUID.randomUUID();

        System.out.println(uuid.getMostSignificantBits());
        User user = new User(userRequestDTO.getUsername(),
                userRequestDTO.getPassword(),
                5,
                userRequestDTO.getEmail());

        userCommadImp.saveUserToHbase(user);
        userCommadImp.signup(user);
    }








}
