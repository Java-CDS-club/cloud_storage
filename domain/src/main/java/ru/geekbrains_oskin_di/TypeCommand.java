package ru.geekbrains_oskin_di;

import java.io.Serializable;

public enum TypeCommand implements Serializable {
    AUTHORIZATION("Авторизация"),
    REGISTRATION("Регистрация"),
    CORRECT_LOGIN_AND_PASSWORD("Корректный логин и пароль"),
    INCORRECT_LOGIN_OR_PASSWORD("Некорректный логин или пароль"),
    REGISTRATION_SUCCESSFULLY("Регистрация прошла успешно"),
    UPDATE_CLOUD_TABLE("Дай список файлов"),
    NEW_CLOUD_TABLE("Обновленный список файлов");
    private String info;

    TypeCommand(String info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "TypeCommand{" +
                "info='" + info + '\'' +
                '}';
    }
}
