package com.example.demo1.repository;

import com.example.demo1.DTO.UserDTO;
import com.example.demo1.model.Book;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.tomcat.util.buf.UEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Hbase {
    public List<UserDTO> ScanUserTable(TableName tableName, Scan scan) throws Exception{
        Configuration conf = HBaseConfiguration.create();
        List<UserDTO> userDTOList = new ArrayList<>();

        try (Connection connection = ConnectionFactory.createConnection(conf);
             Table table = connection.getTable(tableName))
        {
            ResultScanner scanner = table.getScanner(scan);
            for (Result result = scanner.next(); result != null; result = scanner.next()){
                String row_key = Bytes.toString(result.getRow());
                String user_name = Bytes.toString(result.getValue(Bytes.toBytes("user"), Bytes.toBytes("username")));
                String email = Bytes.toString(result.getValue(Bytes.toBytes("user"), Bytes.toBytes("email")));
                UserDTO userDTO = new UserDTO(row_key, user_name, email);
                userDTOList.add(userDTO);

            }
            scanner.close();
        }
        return userDTOList;
    }

    public List<Book> ScanBookTable(TableName tableName, Scan scan) throws Exception{
        Configuration conf = HBaseConfiguration.create();
        List<Book> bookList = new ArrayList<>();

        try (Connection connection = ConnectionFactory.createConnection(conf);
             Table table = connection.getTable(tableName))
        {
            ResultScanner scanner = table.getScanner(scan);
            for (Result result = scanner.next(); result != null; result = scanner.next()){


                String  row_key = Bytes.toString(result.getRow());
                String name = Bytes.toString(result.getValue(Bytes.toBytes("book"), Bytes.toBytes("name")));

                String publisher = Bytes.toString(result.getValue(Bytes.toBytes("book"), Bytes.toBytes("publisher")));

                int amount = Bytes.toInt(result.getValue(Bytes.toBytes("book"), Bytes.toBytes("amount")));
                Book book = new Book(row_key, name, publisher,amount);
                bookList.add(book);

            }
            scanner.close();
        }
        return bookList;
    }

    public int ScanAmount(TableName tableName, Scan scan) throws Exception{
        Configuration conf = HBaseConfiguration.create();
        int amount = 0;
        try (Connection connection = ConnectionFactory.createConnection(conf);
             Table table = connection.getTable(tableName))
        {
            ResultScanner scanner = table.getScanner(scan);
            for (Result result = scanner.next(); result != null; result = scanner.next()){

                amount = Bytes.toInt(result.getValue(Bytes.toBytes("book"), Bytes.toBytes("amount")));

            }
            scanner.close();
        }
        return amount;
    }

    public void UpdateData(TableName tableName, Put put) throws Exception{
        Configuration conf = HBaseConfiguration.create();

        try (Connection connection = ConnectionFactory.createConnection(conf);
             Table table = connection.getTable(tableName))
        {
            table.put(put);
        }
    }

}
