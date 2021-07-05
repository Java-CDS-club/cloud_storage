package ru.geekbrains.oskin_di.service.impl.server_command;

import ru.geekbrains.oskin_di.FileInfo;
import ru.geekbrains.oskin_di.command.Command;
import ru.geekbrains.oskin_di.command.TypeCommand;
import ru.geekbrains.oskin_di.service.ServerCommandService;

import java.nio.file.Paths;
import java.util.List;

public class GiveFilesServerCommand implements ServerCommandService {

    @Override
    public Command processCommand(Command command) {
        FileInfo fileInfo = new FileInfo(Paths.get(command.getContext()));
        fileInfo.writeSuccessor();
        return new Command(TypeCommand.NEW_CLOUD_TABLE, command.getContext(),fileInfo);
    }

    @Override
    public TypeCommand getTypeCommand() {
        return TypeCommand.UPDATE_CLOUD_TABLE;
    }
}
