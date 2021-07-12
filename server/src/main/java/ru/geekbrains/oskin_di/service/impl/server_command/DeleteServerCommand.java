package ru.geekbrains.oskin_di.service.impl.server_command;

import lombok.extern.log4j.Log4j2;
import ru.geekbrains.oskin_di.command.Command;
import ru.geekbrains.oskin_di.command.TypeCommand;
import ru.geekbrains.oskin_di.service.ServerCommandService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Log4j2
public class DeleteServerCommand implements ServerCommandService {

    @Override
    public Command processCommand(Command command) {
        Path path = Paths.get(command.getFileInfo().getStringPath());
        deleteFile(path);
        return new Command(TypeCommand.DELETION_END);
    }

    @Override
    public TypeCommand getTypeCommand() {
        return TypeCommand.DELETION;
    }

    private void deleteFile(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            log.error("Невозможно удалить файл");
        }
    }
}
