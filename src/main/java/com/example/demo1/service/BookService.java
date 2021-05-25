package com.example.demo1.service;

import com.example.demo1.DTO.BookDTO;
import com.example.demo1.DTO.UserBookDTO;
import com.example.demo1.model.Book;
import com.example.demo1.repository.imp.BookImplements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@Service
public class BookService {
    @Autowired
    private final BookImplements bookImplements;

    public BookService(BookImplements bookImplements) {
        this.bookImplements = bookImplements;
    }

    public List<Book> findAll(){
        return  bookImplements.findAll();
    }

    public String addBook(Book book){
        bookImplements.addBook(book);
        return "Thêm sách thành công";
    }
    public String updateBook(Book book){
        bookImplements.updateBook(book);
        return "Cập nhập sách thành công!";
    }

    public String userBook(UserBookDTO userBookDTO){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Calendar cal = Calendar.getInstance();
        String currDate = sdf.format(cal.getTime());

        cal.add(Calendar.DAY_OF_MONTH, 7);
        String newDate = sdf.format(cal.getTime());

        for(int i =0; i < userBookDTO.getBook().size(); i++){
            try {
                bookImplements.userBook(
                        userBookDTO.getId(),
                        userBookDTO.getBook().get(i).getId(),
                        currDate,
                        newDate
                );

                int amount = bookImplements.findAmount(userBookDTO.getBook().get(i).getId()) - 1;
                bookImplements.updateAmount(amount, userBookDTO.getBook().get(i).getId());
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }

        }
        return "Mượn sách thành công!";
    }

}
