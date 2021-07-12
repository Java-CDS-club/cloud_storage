package ru.geekbrains.oskin_di;

import ru.geekbrains.oskin_di.database.flyway.Migration;
import ru.geekbrains.oskin_di.factory.Factory;
import ru.geekbrains.oskin_di.util.Config;

public class Main {
    public static void main(String[] args) {
        Config.considerProperties();
//        Migration.start();
        Factory.getNettyServer().startServer();
    }
}
