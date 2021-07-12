package ru.geekbrains.oskin_di.core.pipeline;

import io.netty.channel.ChannelHandlerContext;
import ru.geekbrains.oskin_di.FileInfo;

public interface PipelineEditor {

    void clear(ChannelHandlerContext ctx);

    void switchToCommand(ChannelHandlerContext ctx);

    void switchToFileUpload(ChannelHandlerContext ctx, FileInfo fileinfo);
}
