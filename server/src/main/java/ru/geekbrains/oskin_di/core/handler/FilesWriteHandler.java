package ru.geekbrains.oskin_di.core.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.geekbrains.oskin_di.FileInfo;

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
    public void channelRead(ChannelHandlerContext ctx, Object chunkedFile) throws Exception {

        ByteBuf byteBuf = (ByteBuf) chunkedFile;

        Long fileSizeCount = 0L;
        try (OutputStream os = new BufferedOutputStream(new FileOutputStream(fileInfo.getStringPath(), true))) {
            while (byteBuf.isReadable()) {
                os.write(byteBuf.readByte());
                fileSizeCount++;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            byteBuf.release();
        }

    }

    private void checkFileIntegrity(Long fileSizeCount, ChannelHandlerContext ctx) {
        if (fileSizeCount == fileInfo.getSize()) {

        } else {

        }

    }

    private void writeFile(ByteBuf byteBuf) {

    }
}
