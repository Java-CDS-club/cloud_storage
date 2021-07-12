package ru.geekbrains.oskin_di.core.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.log4j.Log4j2;
import ru.geekbrains.oskin_di.command.Command;
import ru.geekbrains.oskin_di.command.TypeCommand;
import ru.geekbrains.oskin_di.core.pipeline.PipelineEditor;
import ru.geekbrains.oskin_di.factory.Factory;
import ru.geekbrains.oskin_di.service.CommandDictionaryService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Log4j2
public class AuthenticationInboundHandler extends SimpleChannelInboundHandler<Command> {


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Command command) throws Exception {

        CommandDictionaryService commandDictionaryService = Factory.getCommandDirectoryService();
        PipelineEditor pipelineEditor = Factory.getPipelineEditor();

        if (command.getTypeCommand() == TypeCommand.REGISTRATION) {
            log.info("Команда на регистрацию");
            Command resultCommand = commandDictionaryService.processCommand(command);
            channelHandlerContext.writeAndFlush(resultCommand);
        }

        if (command.getTypeCommand() == TypeCommand.AUTHORIZATION) {
            log.info("Команда на авторизацию");
            Command resultCommand = commandDictionaryService.processCommand(command);
            channelHandlerContext.writeAndFlush(resultCommand);

            if (resultCommand.getTypeCommand() == TypeCommand.CORRECT_LOGIN_AND_PASSWORD) {
                log.info("Авторизация прошла успешно");
                pipelineEditor.clear(channelHandlerContext);
                pipelineEditor.switchToCommand(channelHandlerContext);
            }
        }
    }
}
