package ru.geekbrains.oskin_di.core;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import ru.geekbrains.oskin_di.core.handler.AuthenticationInboundHandler;
import ru.geekbrains.oskin_di.util.Config;

public class NettyServer {

    private static NettyServer instance;

    public static NettyServer getInstance() {
        if (instance == null) {
            instance = new NettyServer();
        }
        return instance;
    }

    public void startServer() {

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) {
                        channel.pipeline()
                                .addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)))
                                .addLast(new ObjectEncoder())
                                .addLast(new AuthenticationInboundHandler());

                    }
                });

        try {
            ChannelFuture future = bootstrap.bind(Config.getPort()).sync();
            System.out.println("Сервер запущен на порту " + Config.getPort());
            future.channel().closeFuture().sync();
            System.out.println("Сервер завершил работу");
        } catch (InterruptedException e) {
            System.out.println("Сервер упал");
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
