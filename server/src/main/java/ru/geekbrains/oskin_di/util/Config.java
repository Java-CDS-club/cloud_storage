package ru.geekbrains.oskin_di.util;


import lombok.Getter;
import lombok.extern.log4j.Log4j2;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Log4j2
public class Config {
    @Getter
    private static String dbUrl;
    @Getter
    private static int port;
    @Getter
    private static String dbLogin;
    @Getter
    private static String dbPassword;
    @Getter
    private static String dbDriver;
    @Getter
    private static String cloudPath;
    @Getter
    private static String cloudPathPrefix;

    public static void considerProperties() {
        try (InputStream propertyInputStream = Config.class.getResourceAsStream("/properties/Server.properties")) {
            Properties properties = new Properties();
            properties.load(propertyInputStream);

            dbUrl = properties.getProperty("db_url");
            port = Integer.parseInt(properties.getProperty("port"));
            dbLogin = properties.getProperty("db_login");
            dbPassword = properties.getProperty("db_password");
            dbDriver = properties.getProperty("db_driver");
            cloudPath = properties.getProperty("cloud_path");
            cloudPathPrefix = properties.getProperty("cloud_path_prefix");

        } catch (IOException ioException) {
            log.error("Файл с настройками не найден");
        }
    }
}
