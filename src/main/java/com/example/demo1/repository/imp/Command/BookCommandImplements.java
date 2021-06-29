package com.example.demo1.repository.imp.Command;


import com.example.demo1.model.Book;
import com.example.demo1.model.User;
import com.example.demo1.repository.Hbase;
import com.example.demo1.repository.ICommandRepository.IBookCommandRep;
import org.apache.commons.net.ntp.TimeStamp;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
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
        return jdbcTemplate.update(
                "INSERT INTO `mydb`.`book` (`name`, `publisher`, `amount`) VALUES (?, ?, ?);",
                book.getName(),
                book.getPublisher(),
                book.getAmount());
    }

    @Override
    public int updateBook(Book book) {
        return jdbcTemplate.update("UPDATE `mydb`.`book` SET `publisher` = ?, `amount` = ? WHERE (`name` = ?);",
                book.getPublisher(),
                book.getAmount(),
                book.getName());
    }

    @Override
    public int userBook(int user_id, int book_id, String time_borrowed, String time_out) {

        return jdbcTemplate.update(
                "INSERT INTO `mydb`.`user_has_book` (`user_id`, `book_id`, `time_borrowed`, `time_out`) VALUES (?, ?, ?, ?);",
                user_id,
                book_id,
                time_borrowed,
                time_out);
    }

    @Override
    public int updateAmount(int id) {
        return jdbcTemplate.update("UPDATE `mydb`.`book` SET `amount` = amount - 1 WHERE (`id` = ?);",  id);
    }

    @Override
    public void addBookToHbase(Book book) throws Exception {
        Configuration conf = HBaseConfiguration.create();
        Connection connection = ConnectionFactory.createConnection(conf);

        Table table = null;

        try {
            table = connection.getTable(TableName.valueOf("book_lib"));
            System.out.println("Table: " + table.toString());

            TimeStamp timeStamp = new TimeStamp(System.currentTimeMillis());

            String row_key =timeStamp.toString();

            Put p = new Put(Bytes.toBytes(row_key));

            p.addColumn(Bytes.toBytes("book"),
                    Bytes.toBytes("name"),
                    Bytes.toBytes(book.getName()));

            p.addColumn(Bytes.toBytes("book"),
                    Bytes.toBytes("publisher"),
                    Bytes.toBytes(book.getPublisher()));

            p.addColumn(Bytes.toBytes("book"),
                    Bytes.toBytes("amount"),
                    Bytes.toBytes(book.getAmount()));

            table .put(p);
        } finally {
            assert table != null;
            table.close();
            connection.close();
        }
    }

    @Override
    public void userBookToHbase(User user, Book book, String time_borrowed, String time_out) throws Exception {
        Configuration conf = HBaseConfiguration.create();
        Connection connection = ConnectionFactory.createConnection(conf);

        Table table = null;

        try {
            table = connection.getTable(TableName.valueOf("book_lib"));
            System.out.println("Table: " + table.toString());

            TimeStamp timeStamp = new TimeStamp(System.currentTimeMillis());

            String row_key =timeStamp.toString();

            Put p = new Put(Bytes.toBytes(row_key));

            p.addColumn(Bytes.toBytes("user"),
                    Bytes.toBytes("username"),
                    Bytes.toBytes(user.getUsername()));

            p.addColumn(Bytes.toBytes("book"),
                    Bytes.toBytes("name"),
                    Bytes.toBytes(book.getName()));

            p.addColumn(Bytes.toBytes("book"),
                    Bytes.toBytes("publisher"),
                    Bytes.toBytes(book.getPublisher()));

            p.addColumn(Bytes.toBytes("book"),
                    Bytes.toBytes("time_borrowed"),
                    Bytes.toBytes(time_borrowed));

            p.addColumn(Bytes.toBytes("book"),
                    Bytes.toBytes("time_out"),
                    Bytes.toBytes(time_out));

            table.put(p);
        } finally {
            assert table != null;
            table.close();
            connection.close();
        }
    }

    @Override
    public void updateBookHbase(Book book) throws Exception {
        Put put = new Put(Bytes.toBytes(book.getId()));
        put.addColumn(Bytes.toBytes("book"), Bytes.toBytes("name"), Bytes.toBytes(book.getName()));
        put.addColumn(Bytes.toBytes("book"), Bytes.toBytes("publisher"), Bytes.toBytes(book.getPublisher()));
        put.addColumn(Bytes.toBytes("book"), Bytes.toBytes("amount"), Bytes.toBytes(book.getAmount()));

        Hbase hbase =new Hbase();
        hbase.UpdateData(TableName.valueOf("book_lib"), put);
    }

    public void updateAmountBookHbase(String row_key) throws Exception{
        Scan scan = new Scan();

        scan.addColumn(Bytes.toBytes("book"), Bytes.toBytes("amount"));

        Hbase hbase = new Hbase();
        int amount = hbase.ScanAmount(TableName.valueOf("book_lib"), scan) - 1;

        Put put = new Put(Bytes.toBytes(row_key));

        put.addColumn(Bytes.toBytes("book"), Bytes.toBytes("amount"), Bytes.toBytes(amount));

        hbase.UpdateData(TableName.valueOf("book_lib"), put);
    }

}
