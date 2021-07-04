package ru.geekbrains.oskin_di.service;

import ru.geekbrains.oskin_di.command.Command;

public interface CommandDictionaryService {

    Command processCommand(Command command);

}
