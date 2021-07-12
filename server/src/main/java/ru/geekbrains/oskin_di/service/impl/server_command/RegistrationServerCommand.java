package ru.geekbrains.oskin_di.service.impl.server_command;

import ru.geekbrains.oskin_di.command.Command;
import ru.geekbrains.oskin_di.command.TypeCommand;
import ru.geekbrains.oskin_di.database.AuthenticationProvided;
import ru.geekbrains.oskin_di.factory.Factory;
import ru.geekbrains.oskin_di.service.ServerCommandService;


public class RegistrationServerCommand implements ServerCommandService {

    AuthenticationProvided authenticationProvided = Factory.getAuthenticationProvided();

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

        if (authenticationProvided.addClient(login, password)) {
            return new Command(TypeCommand.REGISTRATION_SUCCESSFULLY);
        } else {
            return new Command(TypeCommand.REGISTRATION_FAILURE);
        }
    }

    @Override
    public TypeCommand getTypeCommand() {
        return TypeCommand.REGISTRATION;
    }
}
