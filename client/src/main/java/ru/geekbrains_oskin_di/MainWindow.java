package ru.geekbrains_oskin_di;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainWindow{


    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("/view/main_panel.fxml"));
        primaryStage.getIcons().add(new Image("/view/icon.png"));
        primaryStage.setTitle("Portfile");
        primaryStage.setScene(new Scene(root, 1012, 600));
        primaryStage.show();
        primaryStage.setResizable(false);
    }


}
