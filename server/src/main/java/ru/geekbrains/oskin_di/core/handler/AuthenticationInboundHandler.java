package ru.geekbrains.oskin_di.core.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import ru.geekbrains.oskin_di.command.Command;
import ru.geekbrains.oskin_di.command.TypeCommand;
import ru.geekbrains.oskin_di.core.pipeline.PipelineEditor;
import ru.geekbrains.oskin_di.factory.Factory;
import ru.geekbrains.oskin_di.service.CommandDictionaryService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AuthenticationInboundHandler extends SimpleChannelInboundHandler<Command> {


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Command command) throws Exception {

        CommandDictionaryService commandDictionaryService = Factory.getCommandDirectoryService();
        PipelineEditor pipelineEditor = Factory.getPipelineEditor ();

        if (command.getTypeCommand() == TypeCommand.REGISTRATION) {
            Command resultCommand = commandDictionaryService.processCommand(command);
            channelHandlerContext.writeAndFlush(resultCommand);
        }

        if (command.getTypeCommand() == TypeCommand.AUTHORIZATION) {
//            Command resultCommand = commandDictionaryService.processCommand(command);
            String stringPath = null;
            try {
                Path pathUser = Paths.get("./CLOUD", "MAX" + "-CLOUD/");
                Files.createDirectories(pathUser);
                stringPath = pathUser.toString();

            } catch (IOException e) {
                e.printStackTrace();
            }
            Command resultCommand = new Command (TypeCommand.CORRECT_LOGIN_AND_PASSWORD,stringPath);
            channelHandlerContext.writeAndFlush(resultCommand);

            if (resultCommand.getTypeCommand() == TypeCommand.CORRECT_LOGIN_AND_PASSWORD) {
                pipelineEditor.clear (channelHandlerContext);
                pipelineEditor.switchToCommand (channelHandlerContext);
            }
        }
    }
}
