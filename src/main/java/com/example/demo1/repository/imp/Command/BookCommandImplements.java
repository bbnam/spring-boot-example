package com.example.demo1.repository.imp.Command;

import com.example.demo1.model.Book;
import com.example.demo1.repository.ICommandRepository.IBookCommandRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BookCommandImplements implements IBookCommandRep {
    private final JdbcTemplate jdbcTemplate;

    public BookCommandImplements(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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
    public int updateAmount(int id) {
        return jdbcTemplate.update("UPDATE `mydb`.`book` SET `amount` = amount - 1 WHERE (`id` = ?);",  id);
    }


}
