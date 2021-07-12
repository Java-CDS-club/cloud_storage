package ru.geekbrains.oskin_di;

import lombok.Getter;

public enum FileType {
    FILE("Файл"), DIRECTORY("Папка с файлами");

    @Getter
    private String name;

    FileType(String name) {
        this.name = name;
    }
}
