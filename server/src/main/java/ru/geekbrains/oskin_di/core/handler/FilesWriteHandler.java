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
import ru.geekbrains.oskin_di.core.pipeline.PipelineEditor;
import ru.geekbrains.oskin_di.factory.Factory;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FilesWriteHandler extends ChannelInboundHandlerAdapter {

    private FileInfo fileInfo;


    public FilesWriteHandler(FileInfo fileInfo) {
        this.fileInfo = fileInfo;

    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object chunkedFile) throws Exception {

        PipelineEditor pipelineEditor = Factory.getPipelineEditor();

        ByteBuf byteBuf = (ByteBuf) chunkedFile;

        writeChunkFile(byteBuf);

        if (Files.size(Paths.get(fileInfo.getFuturePath())) == fileInfo.getSize()) {
            channelHandlerContext.writeAndFlush(new Command(TypeCommand.UNLOADING_END));
            pipelineEditor.clear(channelHandlerContext);
            pipelineEditor.switchToCommand(channelHandlerContext);
        }
    }

    private void writeChunkFile(ByteBuf byteBuf) {
        try (OutputStream os = new BufferedOutputStream(new FileOutputStream(fileInfo.getFuturePath(), true))) {
            while (byteBuf.isReadable()) {
                os.write(byteBuf.readByte());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            byteBuf.release();
        }
    }

}
