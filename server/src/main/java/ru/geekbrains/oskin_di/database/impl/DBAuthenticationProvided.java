package ru.geekbrains.oskin_di.database.impl;

import lombok.extern.log4j.Log4j2;
import ru.geekbrains.oskin_di.database.AuthenticationProvided;
import ru.geekbrains.oskin_di.database.DBConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Log4j2
public class DBAuthenticationProvided implements AuthenticationProvided {

    private DBConnection dbConnection;
    private static DBAuthenticationProvided instance;

    public static DBAuthenticationProvided getInstance() {
        if (instance == null) {
            instance = new DBAuthenticationProvided();

        }
        return instance;
    }

    public DBAuthenticationProvided() {
        init();
    }

    @Override
    public void init() {
        dbConnection = new DBConnection();
    }

    @Override
    public boolean isCorrect(String login, String password) {
        String query = String.format("select id from clients where login = '%s' and password = '%s';", login, password);
        try (Statement stmt = dbConnection.getStmt();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                log.info("Успешно вошел пользователь" + login);
                return true;
            }
        } catch (SQLException e) {
            log.error("Ошибка в запросе к базе данных");
        }
        return false;
    }


    @Override
    public boolean isLoginBusy(String login) {
        String query = String.format("select id from clients where login = '%s';", login);
        try (Statement stmt = dbConnection.getStmt();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                log.info("Клиент " + login + " есть в базе");
                return true;
            }
        } catch (SQLException e) {
            log.error("Ошибка в запросе к базе данных");
        }
        return false;
    }

    @Override
    public boolean addClient(String login, String password) {
        if (isLoginBusy(login)) {
            return false;
        }
        String query = String.format("insert into clients (login, password) VALUES ('%S', '%S');", login, password);
        try (Statement stmt = dbConnection.getStmt()) {
            stmt.executeUpdate(query);
            log.info("Успешно добавлен клиент " + login);
            return true;
        } catch (SQLException e) {
            log.error("Ошибка в запросе к базе данных");
            return false;
        }
    }

    @Override
    public void shutdown() {
        dbConnection.close();
    }
}
