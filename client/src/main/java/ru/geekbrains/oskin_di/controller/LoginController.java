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
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import ru.geekbrains.oskin_di.MainWindow;
import ru.geekbrains.oskin_di.command.TypeCommand;
import ru.geekbrains.oskin_di.factory.Factory;
import ru.geekbrains.oskin_di.service.NetworkService;
import ru.geekbrains.oskin_di.util.Config;
import ru.geekbrains.oskin_di.command.Command;


import java.net.URL;
import java.util.ResourceBundle;

@Log4j2
public class LoginController implements Initializable {
    @Getter
    private static NetworkService networkService;
    @Getter
    private static String cloudPath;

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passField;

    @FXML
    private Button entrance;

    @FXML
    private Button registration;

    @FXML
    private Label statusLabel;

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
                    case CORRECT_LOGIN_AND_PASSWORD -> openMainWindow(actionEvent, resultCommand);
                }
            });
        });
    }

    private void openMainWindow(ActionEvent actionEvent, Command command) {
        cloudPath = command.getContext();
        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
        Stage stage = new Stage();
        MainWindow mainWindow = new MainWindow();
        startMainWindow(mainWindow, stage);
    }

    private void startMainWindow(MainWindow mainWindow, Stage stage) {
        try {
            mainWindow.start(stage);
        } catch (Exception e) {
            log.error("Невозможно создать новое окно");
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
}
