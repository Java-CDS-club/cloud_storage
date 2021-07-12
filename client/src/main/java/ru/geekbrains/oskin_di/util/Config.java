package ru.geekbrains.oskin_di.util;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Log4j2
public class Config {

    @Getter
    private static int port;

    @Getter
    private static String address;

    public static void considerProperties() {
        try (InputStream propertyInputStream = Config.class.getResourceAsStream("/properties/Client.properties")) {
            Properties properties = new Properties();
            properties.load(propertyInputStream);

            port = Integer.parseInt(properties.getProperty("port"));
            address = properties.getProperty("address");

        } catch (IOException ioException) {
            log.error("Файл с настройками не обнаружен");
        }
    }
}
