package ru.geekbrains.oskin_di.network;


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
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import ru.geekbrains.oskin_di.database.AuthenticationProvided;
import ru.geekbrains.oskin_di.database.impl.DBAuthenticationProvided;


public class Network {
    private AuthenticationProvided authenticationProvided;

    public AuthenticationProvided getAuthenticationProvided() {
        return authenticationProvided;
    }

    public void startServer() {

        authenticationProvided = new DBAuthenticationProvided();
        authenticationProvided.init();

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(
                                    new ObjectDecoder(ClassResolvers.cacheDisabled(null)),
                                    new ObjectEncoder(),
                                    new CommandInboundHandler(authenticationProvided)
                            );
                        }
                    });

            ChannelFuture future = serverBootstrap.bind(8189).sync();
            System.out.println("Сервер запущен на порту 8189");
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            System.out.println("Сервер упал");
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
