package com.example.demo1.service.Command;


import com.example.demo1.DTO.BookKafka;
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
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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

    public void userBook(UserBookDTO userBookDTO) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Calendar cal = Calendar.getInstance();
        String currDate = sdf.format(cal.getTime());

        cal.add(Calendar.DAY_OF_MONTH, 7);
        String newDate = sdf.format(cal.getTime());

        for (int i = 0; i < userBookDTO.getBook().size(); i++) {
            try {
                bookCommandImplements.userBook(
                        userBookDTO.getUser().getId(),
                        userBookDTO.getBook().get(i).getId(),
                        currDate,
                        newDate
                );
                bookCommandImplements.updateAmount(userBookDTO.getBook().get(i).getId());

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }
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
        kafkaTemplate.send("book_lib", 1, null, bookKafka);
    }

    @KafkaListener(groupId = "book_lib", topicPartitions = @TopicPartition(
            topic = "book_lib",
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
