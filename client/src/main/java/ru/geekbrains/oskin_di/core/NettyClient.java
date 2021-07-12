package ru.geekbrains.oskin_di.core;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.stream.ChunkedFile;
import io.netty.handler.stream.ChunkedWriteHandler;
import ru.geekbrains.oskin_di.command.Command;
import ru.geekbrains.oskin_di.core.handler.CommandInboundHandler;
import ru.geekbrains.oskin_di.core.pipeline.PipelineEditor;
import ru.geekbrains.oskin_di.factory.Factory;
import ru.geekbrains.oskin_di.service.Callback;
import ru.geekbrains.oskin_di.util.Config;

import java.io.File;
import java.io.IOException;


public class NettyClient {

    private SocketChannel channel;

    private static NettyClient instance;

    public static NettyClient getInstance() {
        if (instance == null) {
            instance = new NettyClient ();
        }
        return instance;
    }

    public void startClient() {
        Thread thread = new Thread (() -> {
            EventLoopGroup workerGroup = new NioEventLoopGroup ();
            try {
                Bootstrap bootstrap = new Bootstrap ();
                bootstrap.group (workerGroup)
                        .channel (NioSocketChannel.class)
                        .handler (new ChannelInitializer<SocketChannel> () {
                            @Override
                            protected void initChannel(SocketChannel socketChannel) {
                                channel = socketChannel;
                                socketChannel.pipeline ().addLast (
                                        new ObjectDecoder (ClassResolvers.cacheDisabled (null)),
                                        new ObjectEncoder (),
                                        new CommandInboundHandler (),
                                        new ChunkedWriteHandler ()
                                );
                            }
                        });


                ChannelFuture future = bootstrap.connect (Config.getAddress (), Config.getPort ()).sync ();
                future.channel ().closeFuture ().sync ();
            } catch (Exception e) {
                e.printStackTrace ();
            } finally {
                workerGroup.shutdownGracefully ();
            }
        });
        thread.start ();
    }

    public void stopClient() {
        try {
            if (channel.isActive ()) {
                channel.close ().sync ();
            }
        } catch (InterruptedException e) {
            e.printStackTrace ();
        }
    }

    public void sendCommand(Command command, Callback callback) {
        channel.pipeline ().get (CommandInboundHandler.class).setResultCommand (callback);
        channel.writeAndFlush (command);
    }

    public void unloadingFile(Command command, Callback callback) {
        try {
            channel.writeAndFlush (new ChunkedFile (new File (command.getFileInfo ().getStringPath ())));
            channel.pipeline ().get (CommandInboundHandler.class).setResultCommand (callback);
        } catch (IOException e) {
            e.printStackTrace ();
        }
    }

    public void loadingFile(Command command, Callback callback) {
        PipelineEditor pipelineEditor = Factory.getPipelineEditor ();
        channel.writeAndFlush (command);
        pipelineEditor.clear (channel);
        pipelineEditor.switchToFileLoad (channel, command.getFileInfo (), callback);
    }

}
