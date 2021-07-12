package ru.geekbrains.oskin_di.core.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
import ru.geekbrains.oskin_di.FileInfo;
import ru.geekbrains.oskin_di.command.Command;
import ru.geekbrains.oskin_di.command.TypeCommand;
import ru.geekbrains.oskin_di.core.pipeline.PipelineEditor;
import ru.geekbrains.oskin_di.factory.Factory;
import ru.geekbrains.oskin_di.service.Callback;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FilesWriteHandler extends ChannelInboundHandlerAdapter {

    private final SocketChannel channel;
    private final FileInfo fileInfo;
    private final Callback resultCallback;


    public FilesWriteHandler(SocketChannel channel, FileInfo fileInfo, Callback callback) {
        this.channel = channel;
        this.fileInfo = fileInfo;
        this.resultCallback = callback;

    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object chunkedFile) throws Exception {

        PipelineEditor pipelineEditor = Factory.getPipelineEditor ();

        ByteBuf byteBuf = (ByteBuf) chunkedFile;

        writeChunkFile (byteBuf);



        if (Files.size(Paths.get(fileInfo.getFuturePath ())) == fileInfo.getSize ()){
            resultCallback.callback(new Command(TypeCommand.LOADING_END));
            pipelineEditor.clear(channel);
            pipelineEditor.switchToCommand(channelHandlerContext);
        }
    }

    private void writeChunkFile(ByteBuf byteBuf) {
        try (OutputStream os = new BufferedOutputStream(new FileOutputStream(fileInfo.getFuturePath (), true))) {
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
