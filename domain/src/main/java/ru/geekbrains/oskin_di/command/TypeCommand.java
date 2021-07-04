package ru.geekbrains.oskin_di.command;

import java.io.Serializable;

public enum TypeCommand implements Serializable {
    AUTHORIZATION("Авторизация"),
    REGISTRATION("Регистрация"),
    CORRECT_LOGIN_AND_PASSWORD("Корректный логин и пароль"),
    INCORRECT_LOGIN_OR_PASSWORD("Некорректный логин или пароль"),
    REGISTRATION_SUCCESSFULLY("Регистрация прошла успешно"),
    REGISTRATION_FAILURE("Регистрация провалилась"),
    UPDATE_CLOUD_TABLE("Дай список файлов"),
    NEW_CLOUD_TABLE("Обновленный список файлов"),
    INVALID_COMMAND("Некорректная команда");
    private String info;

    TypeCommand(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    @Override
    public String toString() {
        return "TypeCommand{" +
                "info='" + info + '\'' +
                '}';
    }
}
