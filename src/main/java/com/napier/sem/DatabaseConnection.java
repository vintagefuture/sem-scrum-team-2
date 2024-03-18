package com.napier.sem;

import com.mysql.cj.jdbc.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://db:3306/world?useSSL=false";
    private static final String USER = "root";
    private static final String PASS = "example";

    public static Connection getConnection() {
        Connection con = null;

        try {
            Class.forName(JDBC_DRIVER);

            int retries = 10;
            for (int i = 0; i < retries; ++i) {
                System.out.println("Connecting to database...");
                // Wait a bit for db to start
                Thread.sleep(30000);
                con = DriverManager.getConnection(DB_URL, USER, PASS);
                System.out.println("Successfully connected");
                break;
            }
            return con;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
