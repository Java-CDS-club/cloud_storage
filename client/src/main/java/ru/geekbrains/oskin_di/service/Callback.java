package ru.geekbrains.oskin_di.service;

import ru.geekbrains.oskin_di.command.Command;

public interface Callback {

    void callback(Command command);

}
