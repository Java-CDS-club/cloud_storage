package ru.geekbrains.oskin_di.core.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import ru.geekbrains.oskin_di.command.Command;
import ru.geekbrains.oskin_di.command.TypeCommand;
import ru.geekbrains.oskin_di.factory.Factory;
import ru.geekbrains.oskin_di.service.CommandDictionaryService;

public class AuthenticationInboundHandler extends SimpleChannelInboundHandler<Command> {


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Command command) throws Exception {

        CommandDictionaryService commandDictionaryService = Factory.getCommandDirectoryService();

        if (command.getTypeCommand() == TypeCommand.REGISTRATION) {
            Command resultCommand = commandDictionaryService.processCommand(command);
            channelHandlerContext.writeAndFlush(resultCommand);
        }

        if (command.getTypeCommand() == TypeCommand.AUTHORIZATION) {
            Command resultCommand = commandDictionaryService.processCommand(command);
            channelHandlerContext.writeAndFlush(resultCommand);
            if (resultCommand.getTypeCommand() == TypeCommand.CORRECT_LOGIN_AND_PASSWORD) {
                channelHandlerContext.channel().pipeline().addLast(new CommandInBoundHandler());
                channelHandlerContext.channel().pipeline().remove(this);
            }
        }
    }
}
