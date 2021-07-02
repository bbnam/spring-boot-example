package com.example.demo1.service.Query;

import com.example.demo1.DTO.BookResponseDTO;
import com.example.demo1.DTO.UserBookResponseDTO;
import com.example.demo1.model.Book;
import com.example.demo1.repository.imp.Query.BookQueryImp;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class BookQueryService {
    private final BookQueryImp bookQueryImp;

    private final KafkaTemplate<String, UserBookResponseDTO> kafkaTemplate;

    public BookQueryService(BookQueryImp bookQueryImp, KafkaTemplate<String, UserBookResponseDTO> kafkaTemplate) {
        this.bookQueryImp = bookQueryImp;
        this.kafkaTemplate = kafkaTemplate;
    }

    public BookResponseDTO findAll() {

        return new BookResponseDTO(0, 200,bookQueryImp.findAll());
    }

    public List<Book> findByName(String name){return bookQueryImp.findByName(name);}
    public List<Book> findAllEl(){
        return bookQueryImp.findAllEl();
    }

    @Bean
    @Async
    public void check() throws InterruptedException {

        while (true){
            System.out.println("adasdads");
            List<UserBookResponseDTO> userBookResponseDTOS = bookQueryImp.getUserBook();

            LocalDateTime time_now = LocalDateTime.now();
            DateTimeFormatter format_time = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

            String time_format = time_now.format(format_time);
            for (int i = 0; i< userBookResponseDTOS.size(); i ++){

                if (time_format.compareTo(userBookResponseDTOS.get(i).getTime_back()) > 0){
                    kafkaTemplate.send("book_lib_test", 0,null,userBookResponseDTOS.get(i));
                }
            }
            TimeUnit.SECONDS.sleep(30);

        }
    }
    public BookResponseDTO findAllBookHbase() throws Exception{
        return new BookResponseDTO(0,200,bookQueryImp.findAllHbase());
    }
}
