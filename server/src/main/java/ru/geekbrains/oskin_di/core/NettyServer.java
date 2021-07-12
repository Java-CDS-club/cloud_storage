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
import lombok.extern.log4j.Log4j2;
import ru.geekbrains.oskin_di.core.handler.AuthenticationInboundHandler;
import ru.geekbrains.oskin_di.util.Config;

@Log4j2
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
            log.info("Сервер запущен на порту " + Config.getPort());
            future.channel().closeFuture().sync();
            log.info("Сервер завершил работу");
        } catch (InterruptedException e) {
            log.error("Сервер упал");
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
