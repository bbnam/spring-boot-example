package com.example.demo1.repository.imp;

import com.example.demo1.DTO.BookDTO;
import com.example.demo1.DTO.UserBookDTO;
import com.example.demo1.model.Book;
import com.example.demo1.repository.BookRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
@Repository
public class BookImplements implements BookRep {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Book> findAll() {
        return jdbcTemplate.query("Select * from book", (rs, rowNum) -> new Book(rs.getInt("id"), rs.getString("name"), rs.getString("publisher"), rs.getInt("amount")));
    }

    @Override
    public int addBook(Book book) {
        return jdbcTemplate.update("INSERT INTO `mydb`.`book` (`name`, `publisher`, `amount`) VALUES (?, ?, ?);", book.getName(), book.getPublisher(), book.getAmount());
    }

    @Override
    public int updateBook(Book book) {
        return jdbcTemplate.update("UPDATE `mydb`.`book` SET `publisher` = ?, `amount` = ? WHERE (`name` = ?);", book.getPublisher(), book.getAmount(), book.getName());
    }

    @Override
    public int userBook(int user_id, int book_id, String time_borrowed, String time_out) {

        return jdbcTemplate.update("INSERT INTO `mydb`.`user_has_book` (`user_id`, `book_id`, `time_borrowed`, `time_out`) VALUES (?, ?, ?, ?);", user_id, book_id, time_borrowed, time_out);
    }

    @Override
    public int updateAmount(int amount, int id) {
        return jdbcTemplate.update("UPDATE `mydb`.`book` SET `amount` = ? WHERE (`id` = ?);", amount, id);
    }

    @Override
    public int findAmount(int book_id) {
        String sql = "Select amount from book where id = ?;";
        int number = jdbcTemplate.queryForObject(sql,new Object[] { book_id },  Integer.class );

        return number;

    }




}
