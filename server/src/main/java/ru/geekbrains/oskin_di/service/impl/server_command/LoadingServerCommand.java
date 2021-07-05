package ru.geekbrains.oskin_di.service.impl.server_command;

import ru.geekbrains.oskin_di.command.Command;
import ru.geekbrains.oskin_di.command.TypeCommand;
import ru.geekbrains.oskin_di.service.ServerCommandService;

public class LoadingServerCommand implements ServerCommandService {

    @Override
    public Command processCommand(Command command) {
        return new Command(TypeCommand.LOADING_START);
    }

    @Override
    public TypeCommand getTypeCommand() {
        return TypeCommand.LOADING;
    }
}
