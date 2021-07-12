package ru.geekbrains.oskin_di.database.flyway;

import org.flywaydb.core.Flyway;
import ru.geekbrains.oskin_di.util.Config;

public class Migration {

    public static void start() {
        Flyway flyway = Flyway.configure()
                .dataSource(Config.getDbUrl(), Config.getDbLogin(), Config.getDbPassword())
                .load();
        flyway.migrate();
    }
}