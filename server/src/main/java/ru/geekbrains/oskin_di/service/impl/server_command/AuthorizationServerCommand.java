package ru.geekbrains.oskin_di.service.impl.server_command;

import ru.geekbrains.oskin_di.command.Command;
import ru.geekbrains.oskin_di.command.TypeCommand;
import ru.geekbrains.oskin_di.database.AuthenticationProvided;
import ru.geekbrains.oskin_di.factory.Factory;
import ru.geekbrains.oskin_di.service.ServerCommandService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AuthorizationServerCommand implements ServerCommandService {

    private final AuthenticationProvided authenticationProvided = Factory.getAuthenticationProvided();


    @Override
    public Command processCommand(Command command) {
        String login = command.getContext().split("_",2)[0];
        String password = command.getContext().split("_",2)[0];

        if (authenticationProvided.isCorrect(login,password)) {
            String stringPath = null;
            try {
                Path pathUser = Paths.get("./Cloud/", login + "-Cloud");
                Files.createDirectory(pathUser);
                stringPath = pathUser.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new Command(TypeCommand.CORRECT_LOGIN_AND_PASSWORD,stringPath);
        } else {
            return new Command(TypeCommand.INCORRECT_LOGIN_OR_PASSWORD);
        }
    }

    @Override
    public TypeCommand getTypeCommand() {
        return TypeCommand.AUTHORIZATION;
    }
}
