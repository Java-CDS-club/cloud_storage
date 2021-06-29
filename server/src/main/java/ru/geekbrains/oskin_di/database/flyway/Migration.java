package ru.geekbrains.oskin_di.database.flyway;

import org.flywaydb.core.Flyway;

public class Migration {

    public static void startMigration(){

        Flyway flyway = Flyway.configure()
                .dataSource("jdbc:postgresql://localhost:5435/cloud","postgres","postgrespass")
                .load();
        flyway.migrate();
    }
}