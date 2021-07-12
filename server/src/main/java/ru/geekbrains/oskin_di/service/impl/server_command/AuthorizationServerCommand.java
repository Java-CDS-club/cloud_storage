package ru.geekbrains.oskin_di.service.impl.server_command;

import lombok.extern.log4j.Log4j2;
import ru.geekbrains.oskin_di.command.Command;
import ru.geekbrains.oskin_di.command.TypeCommand;
import ru.geekbrains.oskin_di.database.AuthenticationProvided;
import ru.geekbrains.oskin_di.factory.Factory;
import ru.geekbrains.oskin_di.service.ServerCommandService;
import ru.geekbrains.oskin_di.util.Config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Log4j2
public class AuthorizationServerCommand implements ServerCommandService {

    private final AuthenticationProvided authenticationProvided = Factory.getAuthenticationProvided();


    @Override
    public Command processCommand(Command command) {
        String[] value = command.getContext().split("_");
        String login = null;
        String password = null;
        if (value.length == 2) {
            login = value[0];
            password = value[1];
        } else {
            return new Command(TypeCommand.INCORRECT_LOGIN_OR_PASSWORD);
        }

        if (authenticationProvided.isCorrect(login, password)) {
            String stringPath = null;
            try {
                Path pathUser = Paths.get(Config.getCloudPath(), login + Config.getCloudPathPrefix());
                Files.createDirectories(pathUser);
                stringPath = pathUser.toString();
            } catch (IOException e) {
                log.error("Нельзя создать директорию");
            }
            return new Command(TypeCommand.CORRECT_LOGIN_AND_PASSWORD, stringPath);
        } else {
            return new Command(TypeCommand.INCORRECT_LOGIN_OR_PASSWORD);
        }
    }

    @Override
    public TypeCommand getTypeCommand() {
        return TypeCommand.AUTHORIZATION;
    }
}
