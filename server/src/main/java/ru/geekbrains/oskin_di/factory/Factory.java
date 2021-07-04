package ru.geekbrains.oskin_di.factory;

import ru.geekbrains.oskin_di.core.NettyServer;
import ru.geekbrains.oskin_di.database.AuthenticationProvided;
import ru.geekbrains.oskin_di.database.impl.DBAuthenticationProvided;
import ru.geekbrains.oskin_di.service.CommandDictionaryService;
import ru.geekbrains.oskin_di.service.ServerCommandService;
import ru.geekbrains.oskin_di.service.impl.CommandDictionaryServiceImpl;
import ru.geekbrains.oskin_di.service.impl.server_command.AuthorizationServerCommand;
import ru.geekbrains.oskin_di.service.impl.server_command.RegistrationServerCommand;

import java.util.Arrays;
import java.util.List;

public class Factory {

    public static NettyServer getNettyServer(){
        return NettyServer.getInstance();
    }

    public static AuthenticationProvided getAuthenticationProvided(){
        return DBAuthenticationProvided.getInstance();
    }

    public static CommandDictionaryService getCommandDirectoryService() {
        return new CommandDictionaryServiceImpl();
    }

    public static List<ServerCommandService> getCommandServices() {
        return Arrays.asList(new AuthorizationServerCommand(), new RegistrationServerCommand());
    }
}
