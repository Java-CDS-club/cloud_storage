package ru.geekbrains.oskin_di.core.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.stream.ChunkedFile;
import ru.geekbrains.oskin_di.command.Command;
import ru.geekbrains.oskin_di.command.TypeCommand;
import ru.geekbrains.oskin_di.factory.Factory;
import ru.geekbrains.oskin_di.service.CommandDictionaryService;

import java.io.File;

public class CommandInBoundHandler extends SimpleChannelInboundHandler<Command> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Command command) throws Exception {

        CommandDictionaryService commandDictionaryService = Factory.getCommandDirectoryService();

        if (command.getTypeCommand() == TypeCommand.UPDATE_CLOUD_TABLE) {
            Command resultCommand = commandDictionaryService.processCommand(command);
            channelHandlerContext.writeAndFlush(resultCommand);
        }

        if (command.getTypeCommand() == TypeCommand.LOADING) {
            Command resultCommand = commandDictionaryService.processCommand(command);
            channelHandlerContext.writeAndFlush(resultCommand);
            channelHandlerContext.channel().pipeline().remove(ObjectEncoder.class);
            channelHandlerContext.channel().pipeline().remove(ObjectDecoder.class);
            channelHandlerContext.channel().pipeline().remove(this);
            channelHandlerContext.channel().pipeline().addLast(new FilesWriteHandler(command.getFileInfo()));
        }

        if (command.getTypeCommand() == TypeCommand.UNLOADING) {
            Command resultCommand = commandDictionaryService.processCommand(command);
            channelHandlerContext.writeAndFlush(resultCommand);
        }

        if (command.getTypeCommand() == TypeCommand.UNLOADING_START) {
            ChunkedFile chunkedFile = new ChunkedFile(new File(command.getFileInfo().getStringPath()));
            channelHandlerContext.writeAndFlush(chunkedFile);
        }
    }

}
