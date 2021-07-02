package com.example.demo1.service.Command;


import com.example.demo1.DTO.BookKafka;
import com.example.demo1.DTO.UserBookDTO;
import com.example.demo1.DTO.UserBookRequestDTO;
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
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

@Service
public class BookCommandService {
    private final KafkaTemplate<String, BookKafka> kafkaTemplate;


    private final BookCommandImplements bookCommandImplements;
    final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();

    public BookCommandService(KafkaTemplate<String, BookKafka> kafkaTemplate, BookCommandImplements bookCommandImplements) {
        this.kafkaTemplate = kafkaTemplate;
        this.bookCommandImplements = bookCommandImplements;
    }


    public RestHighLevelClient test() {
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials("admin", "rIOwtIeDGQiPjWQbUtHm"));

        return new RestHighLevelClient(
                RestClient.builder(new HttpHost("https://10.3.104.30/", 9200, "http"))
                        .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
                                .setDefaultCredentialsProvider(credentialsProvider)
                        ));
    }

    public void updateBook(Book book) {
        bookCommandImplements.updateBook(book);
    }

    public int userBook(UserBookRequestDTO userBookRequestDTO) {
        LocalDateTime time_now = LocalDateTime.now();
        DateTimeFormatter format_time = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        String time_format = time_now.format(format_time);

        if (time_format.compareTo(userBookRequestDTO.getTime_back()) > 0){
            return 0;
        }
        bookCommandImplements.updateAmount(userBookRequestDTO.getBook().getId(),
                userBookRequestDTO.getBook().getAmount() );

        bookCommandImplements.userBook(
                new UserBookDTO(
                        userBookRequestDTO.getUserDTO(),
                        userBookRequestDTO.getBook(),
                        time_format ,
                        userBookRequestDTO.getTime_back(),
                        0));

        return 1;


    }

    public void saveBookElasticsearch(Book book) throws IOException {
        IndexRequest request = new IndexRequest("book");
        request.id(String.valueOf(book.getId()));
        System.out.println(new ObjectMapper().writeValueAsString(book));
        request.source(new ObjectMapper().writeValueAsString(book), XContentType.JSON);
        IndexResponse indexResponse = test().index(request, RequestOptions.DEFAULT);
        System.out.println(indexResponse);
    }


    public void sendBookRequestToKafka(BookKafka bookKafka) {
        kafkaTemplate.send("book_lib_test", 1, null, bookKafka);
    }

    @KafkaListener(groupId = "book_lib_test", topicPartitions = @TopicPartition(
            topic = "book_lib_test",
            partitions = "1"))
    public void listen2(BookKafka bookKafka) throws Exception {
        System.out.println(bookKafka);
        if (bookKafka.getCode() == 0) {

            SequenceGenerator sequenceGenerator = new SequenceGenerator();

            long id = sequenceGenerator.nextId();

            Book book = new Book(id, bookKafka.getName(), bookKafka.getPublisher(), bookKafka.getAmount());


            bookCommandImplements.addBook(book);
            bookCommandImplements.addBookToHbase(book);
        }

        if (bookKafka.getCode() == 1) {


            Book book = new Book(bookKafka.getId(),
                    bookKafka.getName(),
                    bookKafka.getPublisher(),
                    bookKafka.getAmount());

            bookCommandImplements.updateBook(book);
            bookCommandImplements.updateBookHbase(book);
        }

    }




}
