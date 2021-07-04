package ru.geekbrains.oskin_di.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {

    private static final String PATH_TO_PROPERTIES = "client/src/main/resources/properties/Client.properties";

    private static int port;

    private static String address;

    public static void considerProperties() {
        try (FileInputStream propertyFileInputStream = new FileInputStream(PATH_TO_PROPERTIES)) {
            Properties properties = new Properties();
            properties.load(propertyFileInputStream);

            port = Integer.parseInt(properties.getProperty("port"));
            address = properties.getProperty("address");

        } catch (IOException ioException) {
            System.out.println("Ошибка в программе: файл " + PATH_TO_PROPERTIES + " не обнаружен");
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
