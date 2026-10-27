package com.store.test_admin.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {
    public static Connection connect() {
        String url = "jdbc:sqlserver://localhost:1433;databaseName=AdminTest;encrypt=false;";
        String user = "sa";
        String password = "123";

        Connection con = null;

        try {
            // Load SQL Server driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // Connect to DB
            con = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }

        return con;
    }

    public static Connection getConnection() {
        return connect();
    }
}
