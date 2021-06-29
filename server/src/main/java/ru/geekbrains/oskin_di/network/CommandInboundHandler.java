package ru.geekbrains.oskin_di.network;


import io.netty.channel.ChannelHandlerContext;

import io.netty.channel.SimpleChannelInboundHandler;
import ru.geekbrains.oskin_di.database.AuthenticationProvided;
import ru.geekbrains_oskin_di.Command;
import ru.geekbrains_oskin_di.FileInfo;
import ru.geekbrains_oskin_di.TypeCommand;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CommandInboundHandler extends SimpleChannelInboundHandler<Command> {

    private AuthenticationProvided authenticationProvided;

    private Path pathUser;


    public CommandInboundHandler(AuthenticationProvided authenticationProvided) {
        this.authenticationProvided = authenticationProvided;
    }




    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Command command) throws Exception {
        System.out.println(command);


        if (command.getTypeCommand() == TypeCommand.REGISTRATION) {
            String login = command.getContext().split("_", 2)[0];
            String password = command.getContext().split("_", 2)[1];
            if (authenticationProvided.addClient(login, password)) {
                channelHandlerContext.writeAndFlush(new Command(TypeCommand.REGISTRATION_SUCCESSFULLY, "Регистрация прошла успешно"));
            }
        }

        if (command.getTypeCommand() == TypeCommand.AUTHORIZATION) {
            String login = command.getContext().split("_", 2)[0];
            String password = command.getContext().split("_", 2)[1];
            if (authenticationProvided.isCorrect(login, password)) {
                pathUser = Paths.get("./Cloud/", login + "-Cloud");
                Files.createDirectories(pathUser);
                channelHandlerContext.writeAndFlush(new Command(TypeCommand.CORRECT_LOGIN_AND_PASSWORD, "Корректный логин и пароль"));
            }
        }

        if (command.getTypeCommand() == TypeCommand.UPDATE_CLOUD_TABLE) {
            FileInfo fileInfo = new FileInfo(pathUser);
            channelHandlerContext.writeAndFlush(new Command(TypeCommand.NEW_CLOUD_TABLE,fileInfo));
        }
    }
}

