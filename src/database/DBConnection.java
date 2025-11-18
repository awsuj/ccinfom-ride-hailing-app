package com.src.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static String URL = "jdbc:mysql://localhost:3306/ridehailing";
    private static String USER = "root";
    private static String PASSWORD = "MamaMoBadingLmao321"; // ...don't use my password. Change password here going to the SQL server ya'll are using. 

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
