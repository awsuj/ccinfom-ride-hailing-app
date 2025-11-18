package com.src.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // Make sure 'ridehailing' schema exists
    private static String URL = "jdbc:mysql://localhost:3306/ridehailing";
    private static String USER = "root";
    // Change this to your local MySQL password
    private static String PASSWORD = "MamaMoBadingLmao321";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}