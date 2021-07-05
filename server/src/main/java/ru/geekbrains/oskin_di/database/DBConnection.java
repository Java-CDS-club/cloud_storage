package ru.geekbrains.oskin_di.database;

import ru.geekbrains.oskin_di.factory.Factory;
import ru.geekbrains.oskin_di.util.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {

    private static final String DB_URL = Config.getDb_url();
    private static final String USER = Config.getDb_login();
    private static final String PASSWORD = Config.getDb_password();

    private final Connection connection;
    private final Statement stmt;

    public Statement getStmt() {
        return stmt;
    }

    public DBConnection() {
        try {
            Class.forName(Config.getDb_driver());
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
                e.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
