package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/User";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root12345";

    public static Connection connectionToDB() {
        Connection connection = null;
        {
            try {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("Connection to DataBase was successful!");
            } catch (SQLException ex) {
                System.out.println("JDBC driver is not found!" + '\'' + ex);
            }
        }
        return connection;
    }
}