package ru.geekbrains.oskin_di.core.pipeline.Impl;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import ru.geekbrains.oskin_di.FileInfo;
import ru.geekbrains.oskin_di.core.handler.CommandInboundHandler;
import ru.geekbrains.oskin_di.core.handler.FilesWriteHandler;
import ru.geekbrains.oskin_di.core.pipeline.PipelineEditor;
import ru.geekbrains.oskin_di.service.Callback;

public class PipelineEditorImpl implements PipelineEditor {

    private static PipelineEditorImpl instance;

    public static PipelineEditorImpl getInstance() {
        if (instance == null) {
            instance = new PipelineEditorImpl();
        }
        return instance;
    }

    @Override
    public void clear(SocketChannel channel) {
        ChannelPipeline pipeline = channel.pipeline();
        if (pipeline.get(ObjectDecoder.class) != null) {
            pipeline.remove(ObjectDecoder.class);
        }
        if (pipeline.get(ObjectEncoder.class) != null) {
            pipeline.remove(ObjectEncoder.class);
        }
        if (pipeline.get(CommandInboundHandler.class) != null) {
            pipeline.remove(CommandInboundHandler.class);
        }
        if (pipeline.get(ChunkedWriteHandler.class) != null) {
            pipeline.remove(ChunkedWriteHandler.class);
        }
        if (pipeline.get(FilesWriteHandler.class) != null) {
            pipeline.remove(FilesWriteHandler.class);
        }
    }

    @Override
    public void switchToCommand(ChannelHandlerContext ctx) {
        ChannelPipeline pipeline = ctx.pipeline();
        if (pipeline.get(ObjectDecoder.class) == null) {
            pipeline.addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
        }
        if (pipeline.get(ObjectEncoder.class) == null) {
            pipeline.addLast(new ObjectEncoder());
        }
        if (pipeline.get(CommandInboundHandler.class) == null) {
            pipeline.addLast(new CommandInboundHandler());
        }
        if (pipeline.get(ChunkedWriteHandler.class) == null) {
            pipeline.addLast(new ChunkedWriteHandler());
        }
    }

    @Override
    public void switchToFileLoad(SocketChannel channel, FileInfo fileInfo, Callback callback) {
        ChannelPipeline pipeline = channel.pipeline();
        if (pipeline.get(ObjectEncoder.class) == null) {
            pipeline.addLast(new ObjectEncoder());
        }
        if (pipeline.get(ChunkedWriteHandler.class) == null) {
            pipeline.addLast(new ChunkedWriteHandler());
        }
        if (pipeline.get(FilesWriteHandler.class) == null) {
            pipeline.addLast(new FilesWriteHandler(channel, fileInfo, callback));
        }
    }
}
