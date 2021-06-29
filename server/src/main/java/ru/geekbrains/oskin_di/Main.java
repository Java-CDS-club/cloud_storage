package ru.geekbrains.oskin_di;

import ru.geekbrains.oskin_di.database.flyway.Migration;
import ru.geekbrains.oskin_di.network.Network;

public class Main {
    public static void main(String[] args) {

        Migration.startMigration();
        Network network = new Network();
        network.startServer();

    }
}
