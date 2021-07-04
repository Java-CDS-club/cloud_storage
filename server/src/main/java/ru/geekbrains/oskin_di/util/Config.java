package ru.geekbrains.oskin_di.util;

import javafx.fxml.Initializable;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {

    private static final String PATH_TO_PROPERTIES = "server/src/main/resources/properties/Server.properties";

    private static String db_url;

    private static int port;

    private static String db_login;

    private static String db_password;

    private static String db_driver;

    public static void considerProperties() {
        try (FileInputStream propertyFileInputStream = new FileInputStream(PATH_TO_PROPERTIES)) {
            Properties properties = new Properties();
            properties.load(propertyFileInputStream);

            db_url = properties.getProperty("db_url");
            port = Integer.parseInt(properties.getProperty("port"));
            db_login = properties.getProperty("db_login");
            db_password = properties.getProperty("db_password");
            db_driver = properties.getProperty("db_driver");

        } catch (IOException ioException) {
            System.out.println("Ошибка в программе: файл " + PATH_TO_PROPERTIES + " не обнаружен");
            ioException.printStackTrace();
        }
    }

    public static String getDb_url() {
        return db_url;
    }

    public static int getPort() {
        return port;
    }

    public static String getDb_login() {
        return db_login;
    }

    public static String getDb_password() {
        return db_password;
    }

    public static String getDb_driver() {
        return db_driver;
    }
}
