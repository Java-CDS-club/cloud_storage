package ru.geekbrains.oskin_di.service.impl.server_command;

import ru.geekbrains.oskin_di.command.Command;
import ru.geekbrains.oskin_di.command.TypeCommand;
import ru.geekbrains.oskin_di.database.AuthenticationProvided;
import ru.geekbrains.oskin_di.factory.Factory;
import ru.geekbrains.oskin_di.service.ServerCommandService;


public class RegistrationServerCommand implements ServerCommandService {

    AuthenticationProvided authenticationProvided = Factory.getAuthenticationProvided();

    @Override
    public Command processCommand(Command command){

        String login = command.getContext().split("_",2)[0];
        String password = command.getContext().split("_",2)[0];

        if (authenticationProvided.addClient(login,password)) {
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
