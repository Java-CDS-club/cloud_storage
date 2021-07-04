package ru.geekbrains.oskin_di.database;

import java.util.Optional;

public interface AuthenticationProvided {

        void init();

        boolean isCorrect(String login, String password);

        boolean isLoginBusy(String login);

        boolean addClient(String login, String password);

        void shutdown();

}