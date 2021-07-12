package ru.geekbrains.oskin_di.core.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.stream.ChunkedFile;
import ru.geekbrains.oskin_di.command.Command;
import ru.geekbrains.oskin_di.command.TypeCommand;
import ru.geekbrains.oskin_di.core.pipeline.PipelineEditor;
import ru.geekbrains.oskin_di.factory.Factory;
import ru.geekbrains.oskin_di.service.CommandDictionaryService;

import java.io.File;

public class CommandInBoundHandler extends SimpleChannelInboundHandler<Command> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Command command) throws Exception {

        CommandDictionaryService commandDictionaryService = Factory.getCommandDirectoryService();
        PipelineEditor pipelineEditor = Factory.getPipelineEditor();

        if (command.getTypeCommand() == TypeCommand.UPDATE_CLOUD_TABLE) {
            Command resultCommand = commandDictionaryService.processCommand(command);
            channelHandlerContext.writeAndFlush(resultCommand);
        }

        if (command.getTypeCommand() == TypeCommand.UNLOADING) {
            Command resultCommand = commandDictionaryService.processCommand(command);
            channelHandlerContext.writeAndFlush(resultCommand);
            pipelineEditor.clear(channelHandlerContext);
            pipelineEditor.switchToFileUpload(channelHandlerContext, command.getFileInfo());
        }

        if (command.getTypeCommand() == TypeCommand.LOADING) {
            Command resultCommand = commandDictionaryService.processCommand(command);
            channelHandlerContext.writeAndFlush(resultCommand);
        }

        if (command.getTypeCommand() == TypeCommand.LOADING_START) {
            ChunkedFile chunkedFile = new ChunkedFile(new File(command.getFileInfo().getStringPath()));
            channelHandlerContext.channel().writeAndFlush(chunkedFile);
        }

        if (command.getTypeCommand() == TypeCommand.DELETION) {
            Command resultCommand = commandDictionaryService.processCommand(command);
            channelHandlerContext.writeAndFlush(resultCommand);
        }


    }

}
