package ru.geekbrains.oskin_di.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {

    private static final String DB_URL = "jdbc:postgresql://localhost:5435/cloud";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgrespass";

    private Connection connection;
    private Statement stmt;

    public Statement getStmt() {
        return stmt;
    }

    public DBConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            this.connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            this.stmt = connection.createStatement();
            System.out.println("Успешно подключены к БД");
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Невозможно подключиться к базе данных");
        }
    }

    public void close() {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {

            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {

            }
        }
    }

}
