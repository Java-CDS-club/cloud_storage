package ru.geekbrains_oskin_di.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.geekbrains_oskin_di.*;
import ru.geekbrains_oskin_di.core.Network;
import ru.geekbrains_oskin_di.handlers.CommandInboundHandler;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    private static Network network;


    @FXML
    TextField loginField;

    @FXML
    PasswordField passField;

    @FXML
    Button entrance;

    @FXML
    Button registration;

    @FXML
    Label statusLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        network = new Network();
        network.startServer();
    }

    public void btnEntrance(ActionEvent actionEvent) throws Exception {
        String str = loginField.getText() + "_" + passField.getText();
        network.getChannel().writeAndFlush(new Command(TypeCommand.AUTHORIZATION,str.toUpperCase()));
        Thread.sleep(4000);


        if (CommandInboundHandler.getResultCommand().getTypeCommand() == TypeCommand.CORRECT_LOGIN_AND_PASSWORD){
            ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
            Stage stage = new Stage();
            MainWindow mainWindow = new MainWindow();
            mainWindow.start(stage);
        }
    }

    public void btnRegistration(ActionEvent actionEvent) {
        String str = loginField.getText() + "_" + passField.getText();
        network.getChannel().writeAndFlush(new Command(TypeCommand.REGISTRATION,str.toUpperCase()));
    }

    public static Network getNetwork() {
        return network;
    }
}
