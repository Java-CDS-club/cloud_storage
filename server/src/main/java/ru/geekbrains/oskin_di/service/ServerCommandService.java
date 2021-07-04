package ru.geekbrains.oskin_di.service;

import ru.geekbrains.oskin_di.command.Command;
import ru.geekbrains.oskin_di.command.TypeCommand;

import java.io.IOException;

public interface ServerCommandService {

    Command processCommand(Command command);

    TypeCommand getTypeCommand();
}
