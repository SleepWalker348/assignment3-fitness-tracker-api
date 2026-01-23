package data;

import exception.DatabaseOperationException;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    String connectionUrl = "jdbc:postgresql://localhost:5432/fitness_tracker";
    Connection con = null;
    public Connection getConnection() throws DatabaseOperationException {
        try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection(connectionUrl, "postgres", "1111");
            return con;
        } catch (Exception e) {
            throw new DatabaseOperationException("Failed to connect to database: " + e.getMessage());
        }
    }
}