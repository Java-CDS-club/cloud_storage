package ru.geekbrains.oskin_di.service;

import ru.geekbrains.oskin_di.command.Command;

public interface NetworkService {

    void sendCommand(Command command, Callback callback);

    void openConnection();

    void closeConnection();

}
