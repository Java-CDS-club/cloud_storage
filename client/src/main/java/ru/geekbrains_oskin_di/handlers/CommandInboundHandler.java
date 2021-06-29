package ru.geekbrains_oskin_di.handlers;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import ru.geekbrains_oskin_di.Command;


public class CommandInboundHandler extends SimpleChannelInboundHandler<Command>{

    private static Command resultCommand;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Command command) throws Exception {
        System.out.println(command);
        resultCommand = command;
    }

    public static Command getResultCommand() {
        return resultCommand;
    }
}


