package ru.geekbrains.oskin_di.database.impl;

import ru.geekbrains.oskin_di.core.NettyServer;
import ru.geekbrains.oskin_di.database.AuthenticationProvided;
import ru.geekbrains.oskin_di.database.DBConnection;

import java.sql.ResultSet;
import java.sql.SQLException;


public class DBAuthenticationProvided implements AuthenticationProvided {

    private DBConnection dbConnection;
    private static DBAuthenticationProvided instance;

    public static DBAuthenticationProvided getInstance() {
        if (instance == null) {
            instance = new DBAuthenticationProvided();
        }
        return instance;
    }

    @Override
    public void init() {
        dbConnection = new DBConnection();

    }

    @Override
    public boolean isCorrect(String login, String password) {
        String query = String.format("select id from clients where login = '%s' and password = '%s';", login, password);
        try (ResultSet rs = dbConnection.getStmt().executeQuery(query)) {
            if (rs.next()) {
                System.out.println("Успешно вошел пользователь" + login);
                return true;
            }
        } catch (SQLException e) {
            new SQLException();
        }
        return false;
    }


    @Override
    public boolean isLoginBusy(String login) {
        String query = String.format("select id from clients where login = '%s';", login);
        try (ResultSet rs = dbConnection.getStmt().executeQuery(query)) {
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            new SQLException();
        }
        return false;
    }

    @Override
    public boolean addClient(String login, String password) {
        if (isLoginBusy(login)) {
            return false;
        }
        String query = String.format("insert into clients (login, password) VALUES ('%S', '%S');", login, password);
        try {
            dbConnection.getStmt().executeUpdate(query);
            System.out.println("Успешно добавлен клиент" + login);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public void shutdown() {
        dbConnection.close();
    }
}
