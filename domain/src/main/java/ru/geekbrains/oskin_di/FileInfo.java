package ru.geekbrains.oskin_di;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Log4j2
public class FileInfo implements Serializable {

    private List<FileInfo> successor = new ArrayList<>();
    private String filename;
    private String stringPath;
    private FileType type;
    private long size;
    private String stringSize;
    private LocalDateTime lastModified;
    private String futurePath;

    public FileInfo(Path path) {
        try {
            this.filename = path.getFileName().toString();
            this.stringPath = path.toFile().getPath();
            this.size = Files.isDirectory(path) ? -1L : Files.size(path);
            this.stringSize = formatItemSize(size);
            this.type = Files.isDirectory(path) ? FileType.DIRECTORY : FileType.FILE;
            this.lastModified = LocalDateTime.ofInstant(Files.getLastModifiedTime(path).toInstant(), ZoneOffset.ofHours(0));

        } catch (IOException e) {
            log.error("Нельзя получить информацию о файле");
        }
    }

    public void writeSuccessor() {
        if (type == FileType.DIRECTORY) {
            try {
                this.successor.addAll(Files.list(Paths.get(stringPath)).map(FileInfo::new).collect(Collectors.toList()));
            } catch (IOException e) {
                log.error("Нельзя получить вложенный файлы");
            }
        }
    }

    private String formatItemSize(long bytes) {
        if (-1000 < bytes && bytes < 1000) {
            return bytes + " B";
        }
        CharacterIterator characterIterator = new StringCharacterIterator("kMGTPE");
        while (bytes <= -999_950 || bytes >= 999_950) {
            bytes /= 1000;
            characterIterator.next();
        }
        return String.format("%.1f %cB", bytes / 1000.0, characterIterator.current());
    }
}

