package ru.geekbrains.oskin_di.util;

public enum FieldType {
    NAME("Имя"),
    DATE("Дата создания"),
    TYPE("Тип"),
    SIZE("Размер");

    private String info;

    FieldType(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }
}
