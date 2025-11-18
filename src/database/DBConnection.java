package com.src.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // Make sure 'ridehailing' schema exists
    private static String URL = "jdbc:mysql://localhost:3306/ridehailing";
    private static String USER = "root";
    // Change this to your local MySQL password
    private static String PASSWORD = "12345678";

    public static Connection getConnection() throws SQLException {
        try {
            // This forces the driver to load
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("MySQL JDBC Driver not found! Add the library to your project.");
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}