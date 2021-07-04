package ru.geekbrains.oskin_di;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FileInfo implements Serializable {

    public enum FileType {
        FILE("Файл"), DIRECTORY("Папка с файлами");

        private String name;

        public String getName() {
            return name;
        }

        FileType(String name) {
            this.name = name;
        }
    }

    private List<FileInfo> successor = new ArrayList<>();
    private String filename;
    private String stringPath;
    private FileType type;
    private long size;
    private LocalDateTime lastModified;


    public FileInfo(Path path) {
        try {
            this.filename = path.getFileName().toString();
            this.stringPath = path.toFile().getPath();
            this.size = Files.isDirectory(path) ? -1L : Files.size(path);
            this.type = Files.isDirectory(path) ? FileType.DIRECTORY : FileType.FILE;
            this.lastModified = LocalDateTime.ofInstant(Files.getLastModifiedTime(path).toInstant(), ZoneOffset.ofHours(0));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeSuccessor(){
        if (type == FileType.DIRECTORY) {
            try {
                this.successor.addAll(Files.list(Paths.get(stringPath)).map(FileInfo::new).collect(Collectors.toList()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<FileInfo> getSuccessor() {
        return successor;
    }

    public void setSuccessor(List<FileInfo> successor) {
        this.successor = successor;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getStringPath() {
        return stringPath;
    }

    public void setStringPath(String stringPath) {
        this.stringPath = stringPath;
    }

    public FileType getType() {
        return type;
    }

    public void setType(FileType type) {
        this.type = type;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "successor=" + successor +
                ", filename='" + filename + '\'' +
                ", stringPath='" + stringPath + '\'' +
                ", type=" + type +
                ", size=" + size +
                ", lastModified=" + lastModified +
                '}';
    }
}
