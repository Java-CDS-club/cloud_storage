package ru.geekbrains.oskin_di.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.geekbrains.oskin_di.MainWindow;
import ru.geekbrains.oskin_di.command.TypeCommand;
import ru.geekbrains.oskin_di.factory.Factory;
import ru.geekbrains.oskin_di.service.NetworkService;
import ru.geekbrains.oskin_di.util.Config;
import ru.geekbrains.oskin_di.command.Command;


import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    private static NetworkService networkService;

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
        Config.considerProperties();
        if (networkService == null) {
            networkService = Factory.getNetworkService();
        }
        networkService.openConnection();
    }

    public void btnEntrance(ActionEvent actionEvent) {
        statusLabel.setText("");
        String login_and_pass = loginField.getText() + "_" + passField.getText();
        networkService.sendCommand(new Command(TypeCommand.AUTHORIZATION, login_and_pass), resultCommand -> {
            Platform.runLater(() -> {
                switch (resultCommand.getTypeCommand()) {
                    case INCORRECT_LOGIN_OR_PASSWORD -> statusLabel.setText(TypeCommand.INCORRECT_LOGIN_OR_PASSWORD.getInfo());
                    case CORRECT_LOGIN_AND_PASSWORD -> openMainWindow(actionEvent,resultCommand);
                }
            });
        });
    }

    private void openMainWindow(ActionEvent actionEvent, Command command) {
        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
        Stage stage = new Stage();
        MainWindow mainWindow = new MainWindow();
        MainWindow.setCloudPath(command.getContext());
        MainWindow.setNetworkService(networkService);
        try {
            mainWindow.start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void btnRegistration(ActionEvent actionEvent) {
        statusLabel.setText("");
        String login_and_pass = loginField.getText() + "_" + passField.getText();
        networkService.sendCommand(new Command(TypeCommand.REGISTRATION, login_and_pass), resultCommand -> {
            Platform.runLater(() -> {
                switch (resultCommand.getTypeCommand()) {
                    case REGISTRATION_FAILURE -> statusLabel.setText(TypeCommand.REGISTRATION_FAILURE.getInfo());
                    case REGISTRATION_SUCCESSFULLY -> statusLabel.setText(TypeCommand.REGISTRATION_SUCCESSFULLY.getInfo());
                }
            });
        });
    }

    public static NetworkService getNetworkService() {
        return networkService;
    }
}
