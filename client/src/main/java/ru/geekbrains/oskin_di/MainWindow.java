package ru.geekbrains.oskin_di;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import ru.geekbrains.oskin_di.service.NetworkService;

public class MainWindow {

    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/main_panel.fxml"));
        Parent root = loader.load();
        primaryStage.getIcons().add(new Image("/image/icon.png"));
        primaryStage.setTitle("Port_file");
        primaryStage.setScene(new Scene(root, 1012, 600));
        primaryStage.show();
        primaryStage.setResizable(false);

    }
}
