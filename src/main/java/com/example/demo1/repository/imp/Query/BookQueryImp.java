package com.example.demo1.repository.imp.Query;


import com.example.demo1.model.Book;
import com.example.demo1.repository.IQueryRepository.IBookQueryRep;
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
public class BookQueryImp implements IBookQueryRep{
    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;
    private final SearchRequest searchRequest = new SearchRequest("bbnam-doc-book");

    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

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
    
    public BookQueryImp(ObjectMapper objectMapper, JdbcTemplate jdbcTemplate) {
        this.objectMapper = objectMapper;
        this.jdbcTemplate = jdbcTemplate;

    }

    @Override
    public List<Book> findAll() {
        return jdbcTemplate.query("Select * from book",
                (rs, rowNum) -> new Book(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("publisher"),
                        rs.getInt("amount")));
    }

    @Override
    public List<Book> findByName(String name) {

        searchSourceBuilder.query(QueryBuilders.multiMatchQuery(name, "name","publisher"));
        return getBooks(searchRequest);
    }


    @Override
    public List<Book> findAllEl() {

        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        return getBooks(searchRequest);
    }

    private List<Book> getBooks(SearchRequest searchRequest) {
        searchRequest.source(searchSourceBuilder);
        List<Book> bookList = new ArrayList<>();

        try {
            SearchResponse searchResponse  =test().search(searchRequest, RequestOptions.DEFAULT);
            if (searchResponse.getHits().getHits().length > 0) {
                SearchHit[] searchHit = searchResponse.getHits().getHits();
                for (SearchHit hit : searchHit) {
                    Map<String, Object> map = hit.getSourceAsMap();
                    bookList.add(objectMapper.convertValue(map, Book.class));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bookList;
    }
}
