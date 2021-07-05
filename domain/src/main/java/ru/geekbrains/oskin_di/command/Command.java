package ru.geekbrains.oskin_di.command;

import ru.geekbrains.oskin_di.FileInfo;

import java.io.Serializable;

public class Command implements Serializable {

    private TypeCommand typeCommand;

    private String context;

    private FileInfo fileInfo;

    public Command(TypeCommand typeCommand) {
        this.typeCommand = typeCommand;
    }

    public Command(TypeCommand typeCommand, String context) {
        this.typeCommand = typeCommand;
        this.context = context;
    }

    public Command(TypeCommand typeCommand, String context, FileInfo fileInfo) {
        this.typeCommand = typeCommand;
        this.context = context;
        this.fileInfo = fileInfo;
    }

    public String getContext() {
        return context;
    }

    public void setFileInfo(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public Command(TypeCommand typeCommand, FileInfo fileInfo) {
        this.typeCommand = typeCommand;
        this.fileInfo = fileInfo;
    }

    public TypeCommand getTypeCommand() {
        return typeCommand;
    }

    public void setTypeCommand(TypeCommand typeCommand) {
        this.typeCommand = typeCommand;
    }

    public FileInfo getFileInfo() {
        return fileInfo;
    }
}

