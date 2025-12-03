package com.issuetracker.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

public class DatabaseUtil {
    
    private static String URL;
    private static String USERNAME;
    private static String PASSWORD;
    private static String DRIVER_CLASS;
    
    static {
        loadDatabaseProperties();
    }
    
    private static void loadDatabaseProperties() {
        try (InputStream input = DatabaseUtil.class.getClassLoader()
                .getResourceAsStream("database.properties")) {
            
            Properties prop = new Properties();
            if (input == null) {
                throw new RuntimeException("Unable to find database.properties");
            }
            
            prop.load(input);
            URL = prop.getProperty("db.url");
            USERNAME = prop.getProperty("db.username");
            PASSWORD = prop.getProperty("db.password");
            DRIVER_CLASS = prop.getProperty("db.driver");
            
            Class.forName(DRIVER_CLASS);
            
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to load database configuration", e);
        }
    }
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
    
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}