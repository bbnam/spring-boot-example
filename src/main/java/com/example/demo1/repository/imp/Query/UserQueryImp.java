package com.example.demo1.repository.imp.Query;

import com.example.demo1.DTO.UserDTO;
import com.example.demo1.repository.IQueryRepository.IUserQueryRep;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class UserQueryImp implements IUserQueryRep {
    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;
    final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();


    public RestHighLevelClient test(){
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials("admin", "rIOwtIeDGQiPjWQbUtHm"));

        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("https://10.3.104.30/", 9200, "http"))
                        .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
                                .setDefaultCredentialsProvider(credentialsProvider)
                        ));
        return client;
    }

    public UserQueryImp(ObjectMapper objectMapper, JdbcTemplate jdbcTemplate) {
        this.objectMapper = objectMapper;
        this.jdbcTemplate = jdbcTemplate;

    }

    @Override
    public List<UserDTO> findAll() {
        return jdbcTemplate.query("select * from user", (rs, rowNum) -> new UserDTO(rs.getInt("id"),
                rs.getString("username"), rs.getString("email")));
    }

    @Override
    public List<UserDTO> findByNameAndEmail(String name, String email) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("bbn-doc-user");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.boolQuery()
                .must(QueryBuilders.matchQuery("username", name))
                .must(QueryBuilders.matchQuery("email", email)));
        searchRequest.source(searchSourceBuilder);
        List<UserDTO> userList = new ArrayList<>();

        try {
            SearchResponse searchResponse;
            searchResponse =test().search(searchRequest, RequestOptions.DEFAULT);
            if (searchResponse.getHits().getHits().length > 0) {
                SearchHit[] searchHit = searchResponse.getHits().getHits();
                for (SearchHit hit : searchHit) {
                    Map<String, Object> map = hit.getSourceAsMap();
                    userList.add(objectMapper.convertValue(map, UserDTO.class));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userList;
    }

}
