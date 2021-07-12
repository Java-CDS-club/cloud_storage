package ru.geekbrains.oskin_di.core.pipeline;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.SocketChannel;
import ru.geekbrains.oskin_di.FileInfo;
import ru.geekbrains.oskin_di.service.Callback;

public interface PipelineEditor {

    void clear(SocketChannel channel);

    void switchToCommand(ChannelHandlerContext ctx);

    void switchToFileLoad(SocketChannel channel, FileInfo fileInfo, Callback callback);

}
