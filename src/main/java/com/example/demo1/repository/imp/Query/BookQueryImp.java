package com.example.demo1.repository.imp.Query;


import com.example.demo1.DTO.UserBookResponseDTO;
import com.example.demo1.DTO.UserDTO;
import com.example.demo1.model.Book;
import com.example.demo1.repository.Hbase;
import com.example.demo1.repository.IQueryRepository.IBookQueryRep;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
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

    @Override
    public List<Book> findAllHbase() throws Exception {
        Scan scan = new Scan();

        scan.addColumn(Bytes.toBytes("book"), Bytes.toBytes("name"));
        scan.addColumn(Bytes.toBytes("book"), Bytes.toBytes("publisher"));
        scan.addColumn(Bytes.toBytes("book"), Bytes.toBytes("amount"));

        Hbase hbase = new Hbase();
        return hbase.ScanBookTable(TableName.valueOf("book_lib"), scan);
    }

    @Override
    public List<UserBookResponseDTO> getUserBook() {

        return jdbcTemplate.query("" +
                        "SELECT user_has_book.*, user.*, book.* " +
                        "FROM user_has_book  " +
                        "INNER JOIN user ON user_has_book.user_id = user.id  " +
                        "INNER JOIN book on user_has_book.book_id = book.id " +
                        "AND user_has_book.check = 0",
                (rs, rowNum) -> new UserBookResponseDTO(
                        new UserDTO(
                                rs.getString("user_id"),
                                rs.getString("username"),
                                rs.getString("email")
                        ),
                        new Book(
                                rs.getInt("book_id"),
                                rs.getString("name"),
                                rs.getString("publisher"),
                                rs.getInt("quantity")),
                        rs.getString("time_borrowed"),
                        rs.getString("time_back")));
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
