package com.store.test_admin;

import com.store.test_admin.util.DBConnect;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@SpringBootApplication
public class TestAdminApplication {

    public static void main(String[] args) {


        SpringApplication.run(TestAdminApplication.class, args
        );
    }

}
