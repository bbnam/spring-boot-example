package com.example.demo1.repository.imp.Command;


import com.example.demo1.model.User;
import com.example.demo1.repository.Hbase;
import com.example.demo1.repository.ICommandRepository.IUserCommandRep;
import org.apache.commons.net.ntp.TimeStamp;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


@Repository
public class UserCommadImp implements IUserCommandRep {

    private final JdbcTemplate jdbcTemplate;

    public UserCommadImp(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void update(User user) {
         jdbcTemplate.update("UPDATE user SET email = ?, password = ? WHERE (username = ?);",
                 user.getEmail(),
                 user.getPassword(),
                 user.getUsername());
    }

    @Override
    public void signup(User user){
         jdbcTemplate.update("INSERT INTO `mydb`.`user`(username , password, email) " + "VALUES(?,?,?);",
                 user.getUsername(),
                 user.getPassword(),
                 user.getEmail());
    }

    @Override
    public void saveUserToHbase(User user) throws Exception {
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

            p.addColumn(Bytes.toBytes("user"),
                    Bytes.toBytes("password"),
                    Bytes.toBytes(user.getPassword()));

            p.addColumn(Bytes.toBytes("user"),
                    Bytes.toBytes("email"),
                    Bytes.toBytes(user.getEmail()));

            table .put(p);

        } finally {
            table.close();
            connection.close();
        }


    }

    @Override
    public void updateUserHbase(User user) throws Exception {
        Put put = new Put(Bytes.toBytes(user.getId()));
        put.addColumn(Bytes.toBytes("book"), Bytes.toBytes("username"), Bytes.toBytes(user.getUsername()));
        put.addColumn(Bytes.toBytes("book"), Bytes.toBytes("password"), Bytes.toBytes(user.getPassword()));
        put.addColumn(Bytes.toBytes("book"), Bytes.toBytes("email"), Bytes.toBytes(user.getEmail()));

        Hbase hbase =new Hbase();
        hbase.UpdateData(TableName.valueOf("book_lib"), put);
    }


}
