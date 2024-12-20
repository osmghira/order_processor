package service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    public static Connection getConnection() throws SQLException {
        String dbUrl = ConfigReader.getProperty("db.url");
        String dbUser = ConfigReader.getProperty("db.user");
        String dbPassword = ConfigReader.getProperty("db.password");

        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }
}
