package com.example.demo1.service.Command;


import com.example.demo1.DTO.BookRequestDTO;
import com.example.demo1.DTO.UserBookDTO;
import com.example.demo1.model.Book;
import com.example.demo1.repository.imp.Command.BookCommandImplements;
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
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Service
public class BookCommandService {
    private final BookCommandImplements bookCommandImplements;
    final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();

    public BookCommandService(BookCommandImplements bookCommandImplements) {
        this.bookCommandImplements = bookCommandImplements;
    }


    public RestHighLevelClient test(){
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials("admin", "rIOwtIeDGQiPjWQbUtHm"));

        return new RestHighLevelClient(
                RestClient.builder(new HttpHost("https://10.3.104.30/", 9200, "http"))
                        .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
                                .setDefaultCredentialsProvider(credentialsProvider)
                        ));
    }


    public void addBook(Book book){
        bookCommandImplements.addBook(book);
    }
    public void updateBook(Book book){
        bookCommandImplements.updateBook(book);
    }

    public void userBook(UserBookDTO userBookDTO){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Calendar cal = Calendar.getInstance();
        String currDate = sdf.format(cal.getTime());

        cal.add(Calendar.DAY_OF_MONTH, 7);
        String newDate = sdf.format(cal.getTime());

        for(int i =0; i < userBookDTO.getBook().size(); i++){
            try {
                bookCommandImplements.userBook(
                        userBookDTO.getUser().getId(),
                        userBookDTO.getBook().get(i).getId(),
                        currDate,
                        newDate
                );
                bookCommandImplements.updateAmount(userBookDTO.getBook().get(i).getId());

            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }

        }
    }

    public void saveBookElasticsearch(Book book) throws  IOException {
        IndexRequest request = new IndexRequest("book");
        request.id(String.valueOf(book.getId()));
        System.out.println(new ObjectMapper().writeValueAsString(book));
        request.source(new ObjectMapper().writeValueAsString(book), XContentType.JSON);
        IndexResponse indexResponse = test().index(request, RequestOptions.DEFAULT);
    }

    public void saveBookHbase(BookRequestDTO book) throws Exception{
        SequenceGenerator sequenceGenerator = new SequenceGenerator();

        Book book1 = new Book((int) sequenceGenerator.nextId(), book.getName(), book.getPublisher(), book.getAmount());
        bookCommandImplements.addBookToHbase(book1);
    }

    public void updateBookHbase(Book book) throws Exception{

        bookCommandImplements.updateBookHbase(book);
    }



}
