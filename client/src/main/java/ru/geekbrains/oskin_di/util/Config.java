package ru.geekbrains.oskin_di.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

    private static int port;

    private static String address;

    public static void considerProperties() {
        try (InputStream propertyInputStream = Config.class.getResourceAsStream("/properties/Client.properties")) {
            Properties properties = new Properties();
            properties.load(propertyInputStream);

            port = Integer.parseInt(properties.getProperty("port"));
            address = properties.getProperty("address");

        } catch (IOException ioException) {
            System.out.println("Ошибка в программе: файл настроек не обнаружен");
            ioException.printStackTrace();
        }
    }

    public static int getPort() {
        return port;
    }

    public static String getAddress() {
        return address;
    }
}
