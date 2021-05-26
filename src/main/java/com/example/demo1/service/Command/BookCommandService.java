package com.example.demo1.service.Command;


import com.example.demo1.DTO.UserBookDTO;
import com.example.demo1.model.Book;
import com.example.demo1.repository.imp.Command.BookCommandImplements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Service
public class BookCommandService {
    @Autowired
    private BookCommandImplements bookCommandImplements;
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
                        userBookDTO.getId(),
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
}
