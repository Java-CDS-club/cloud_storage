package ru.geekbrains.oskin_di.database;

import lombok.extern.log4j.Log4j2;
import ru.geekbrains.oskin_di.util.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Log4j2
public class DBConnection {

    private static final String URL = Config.getDbUrl();
    private static final String USER = Config.getDbLogin();
    private static final String PASSWORD = Config.getDbPassword();

    private final Connection connection;
    private final Statement stmt;

    public Statement getStmt() {
        return stmt;
    }

    public DBConnection() {
        try {
            Class.forName(Config.getDbDriver());
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            this.stmt = connection.createStatement();
            log.info("Успешно подключились к базе данных");
        } catch (ClassNotFoundException | SQLException e) {
            log.error("Невозможно подключиться к базе данных");
            throw new RuntimeException();
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
        log.info("Отключились от базы данных");
    }

}
