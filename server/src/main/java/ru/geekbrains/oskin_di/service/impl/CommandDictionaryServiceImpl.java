package ru.geekbrains.oskin_di.service.impl;

import ru.geekbrains.oskin_di.command.Command;
import ru.geekbrains.oskin_di.command.TypeCommand;
import ru.geekbrains.oskin_di.factory.Factory;
import ru.geekbrains.oskin_di.service.CommandDictionaryService;
import ru.geekbrains.oskin_di.service.ServerCommandService;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandDictionaryServiceImpl implements CommandDictionaryService {

    private final Map<TypeCommand, ServerCommandService> commandDictionary;

    public CommandDictionaryServiceImpl() {
        commandDictionary = Collections.unmodifiableMap(getCommonDictionary());
    }

    private Map<TypeCommand, ServerCommandService> getCommonDictionary() {
        List<ServerCommandService> serverCommandServices = Factory.getCommandServices();

        Map<TypeCommand, ServerCommandService> commandDictionary = new HashMap<>();
        for (ServerCommandService serverCommandService : serverCommandServices) {
            commandDictionary.put(serverCommandService.getTypeCommand(), serverCommandService);
        }

        return commandDictionary;
    }

    @Override
    public Command processCommand(Command command) {
        if (commandDictionary.containsKey(command.getTypeCommand())) {
            return commandDictionary.get(command.getTypeCommand()).processCommand(command);
        }
        return new Command(TypeCommand.INVALID_COMMAND);
    }
}
