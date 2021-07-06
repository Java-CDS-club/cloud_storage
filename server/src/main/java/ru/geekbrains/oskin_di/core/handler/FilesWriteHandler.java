package ru.geekbrains.oskin_di.core.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import ru.geekbrains.oskin_di.FileInfo;
import ru.geekbrains.oskin_di.command.Command;
import ru.geekbrains.oskin_di.command.TypeCommand;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Paths;

public class FilesWriteHandler extends ChannelInboundHandlerAdapter {

    private FileInfo fileInfo;

    public FilesWriteHandler(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object chunkedFile) throws Exception {

        ByteBuf byteBuf = (ByteBuf) chunkedFile;

        try (OutputStream os = new BufferedOutputStream(new FileOutputStream(fileInfo.getStringPath(), true))) {
            while (byteBuf.isReadable()) {
                os.write(byteBuf.readByte());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            byteBuf.release();
        }

        channelHandlerContext.channel().pipeline().remove(this);
        channelHandlerContext.channel().pipeline().addLast (new ObjectEncoder ());
        channelHandlerContext.channel().pipeline().addLast (new ObjectDecoder (ClassResolvers.cacheDisabled(null)));
        channelHandlerContext.channel().pipeline().addLast(new ChunkedWriteHandler());
        channelHandlerContext.channel().pipeline().addLast(new CommandInBoundHandler());

        channelHandlerContext.writeAndFlush (new Command (TypeCommand.LOADING_END));

    }
}
