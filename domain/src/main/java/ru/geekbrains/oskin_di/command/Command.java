package ru.geekbrains.oskin_di.command;

import lombok.Getter;
import lombok.Setter;
import ru.geekbrains.oskin_di.FileInfo;

import java.io.Serializable;

@Getter
@Setter
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


    public Command(TypeCommand typeCommand, FileInfo fileInfo) {
        this.typeCommand = typeCommand;
        this.fileInfo = fileInfo;
    }


}

