package ru.geekbrains.oskin_di.core.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import ru.geekbrains.oskin_di.command.Command;
import ru.geekbrains.oskin_di.service.Callback;


public class CommandInboundHandler extends SimpleChannelInboundHandler<Command>{

    private Callback resultCommand;

    public void setResultCommand(Callback resultCommand) {
        this.resultCommand = resultCommand;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Command command) throws Exception {
        if (resultCommand != null) {
            resultCommand.callback(command);
        }
    }


}


