package com.example.demo1.repository.imp.Command;


import com.example.demo1.DTO.UserBookDTO;
import com.example.demo1.model.Book;
import com.example.demo1.model.User;
import com.example.demo1.repository.Hbase;
import com.example.demo1.repository.ICommandRepository.IBookCommandRep;
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
                "INSERT INTO `mydb`.`book` (`id`, `name`, `publisher`, `amount`) VALUES (?, ?, ?, ?);",
                book.getId(),
                book.getName(),
                book.getPublisher(),
                book.getAmount());
    }

    @Override
    public int updateBook(Book book) {
        return jdbcTemplate.update(
                "UPDATE `mydb`.`book` SET `name`=?, `publisher` = ?, `amount` = ? WHERE (`id` = ?);",
                book.getName(),
                book.getPublisher(),
                book.getAmount(),
                book.getId());
    }

    @Override
    public int userBook(UserBookDTO userBookDTO) {

//        System.out.println(userBookDTO);

        return jdbcTemplate.update(
                "INSERT INTO `mydb`.`user_has_book` " +
                        "(`user_id`, `book_id`, `time_borrowed`, `time_back`, `check`, `quantity`) " +
                        "VALUES (?, ?, ?, ?, ?, ?);",
                userBookDTO.getUserDTO().getId(),
                userBookDTO.getBook().getId(),
                userBookDTO.getTime_borrowed(),
                userBookDTO.getTime_back(),
                userBookDTO.getStatus(),
                userBookDTO.getBook().getAmount()
        );
    }

    @Override
    public int updateAmount(long id, int amount) {
        return jdbcTemplate.update("UPDATE `mydb`.`book` SET `amount` = amount - ? WHERE (`id` = ?);",  amount, id);
    }

    @Override
    public void addBookToHbase(Book book) throws Exception {
        Configuration conf = HBaseConfiguration.create();
        Connection connection = ConnectionFactory.createConnection(conf);

        Table table = null;

        try {
            table = connection.getTable(TableName.valueOf("book_lib"));
            System.out.println("Table: " + table.toString());



            String row_key = String.valueOf(book.getId());

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

            String row_key = String.valueOf(book.getId());

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
