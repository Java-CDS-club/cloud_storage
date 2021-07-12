package ru.geekbrains.oskin_di;

public enum FileType {
    FILE ("Файл"), DIRECTORY ("Папка с файлами");

    private String name;

    public String getName() {
        return name;
    }

    FileType(String name) {
        this.name = name;
    }
}
