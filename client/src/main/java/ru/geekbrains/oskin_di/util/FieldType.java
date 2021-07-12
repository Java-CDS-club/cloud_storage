package ru.geekbrains.oskin_di.util;

import lombok.Getter;

public enum FieldType {
    NAME("Имя"),
    DATE("Дата создания"),
    TYPE("Тип"),
    SIZE("Размер");

    @Getter
    private String info;

    FieldType(String info) {
        this.info = info;
    }
}
