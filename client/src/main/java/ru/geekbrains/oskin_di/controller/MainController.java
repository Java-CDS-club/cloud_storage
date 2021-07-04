package ru.geekbrains.oskin_di.controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import ru.geekbrains.oskin_di.FileInfo;
import ru.geekbrains.oskin_di.command.Command;
import ru.geekbrains.oskin_di.command.TypeCommand;
import ru.geekbrains.oskin_di.service.NetworkService;

import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MainController implements Initializable {

    private static NetworkService networkService;

    @FXML
    AnchorPane window;

    @FXML
    TableView<FileInfo> tablePC;

    @FXML
    TableView<FileInfo> tableCloud;

    @FXML
    ComboBox<String> disksBox;

    @FXML
    TextField pathField;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        networkService = LoginController.getNetworkService();
//        networkService.sendCommand(new Command(TypeCommand.UPDATE_CLOUD_TABLE,""),);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        TableColumn<FileInfo, String> filenameColumn = new TableColumn<>("Имя");
        filenameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getFilename()));
        filenameColumn.setPrefWidth(120);

        TableColumn<FileInfo, String> fileTypeColumn = new TableColumn<>("Тип");
        fileTypeColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getType().getName()));
        fileTypeColumn.setPrefWidth(120);

        TableColumn<FileInfo, Long> fileSizeColumn = new TableColumn<>("Размер");
        fileSizeColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getSize()));
        fileSizeColumn.setCellFactory(column -> {
            return new TableCell<FileInfo, Long>() {
                @Override
                protected void updateItem(Long item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        String text = formatItemSize(item);
                        if (item == -1L) {
                            text = "";
                        }
                        setText(text);
                    }
                }
            };
        });
        fileSizeColumn.setPrefWidth(120);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        TableColumn<FileInfo, String> fileDateColumn = new TableColumn<>("Дата изменения");
        fileDateColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getLastModified().format(dtf)));
        fileDateColumn.setPrefWidth(120);

        tablePC.getColumns().addAll(filenameColumn, fileTypeColumn, fileSizeColumn, fileDateColumn);
        tablePC.getSortOrder().add(filenameColumn);

        tableCloud.getColumns().addAll(filenameColumn, fileTypeColumn, fileSizeColumn, fileDateColumn);
        tableCloud.getSortOrder().add(filenameColumn);


        disksBox.getItems().clear();
        for (Path p : FileSystems.getDefault().getRootDirectories()) {
            disksBox.getItems().add(p.toString());
        }
        disksBox.getSelectionModel().select(0);


        ContextMenu contextMenu = new ContextMenu();
        MenuItem openItem = new MenuItem("Открыть");
        openItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

            }
        });
        contextMenu.getItems().add(openItem);
        MenuItem copyItem = new MenuItem("Копировать");
        contextMenu.getItems().add(copyItem);
        MenuItem moveItem = new MenuItem("Переместить");
        contextMenu.getItems().add(moveItem);
        MenuItem deleteItem = new MenuItem("Удалить");
        contextMenu.getItems().add(deleteItem);


        tablePC.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2) {
                    Path path = Paths.get(pathField.getText()).resolve(tablePC.getSelectionModel().getSelectedItem().getFilename());
                    if (Files.isDirectory(path)) {
                        updateList(path);
                    }
                }
                if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                    contextMenu.show(tablePC, mouseEvent.getScreenX(), mouseEvent.getScreenY());
                }
            }
        });

        updateList(Paths.get("."));
//        Command command = CommandInboundHandler.getResultCommand();
//        if(command.getTypeCommand() == TypeCommand.NEW_CLOUD_TABLE){
//            updateTableCloud(command.getFileInfo());
//        }
    }

    private void updateTableCloud(FileInfo cloudFileInfo) {
        System.out.println(cloudFileInfo);
        tableCloud.getItems().clear();
        tableCloud.getItems().addAll(cloudFileInfo.getSuccessor());
        tableCloud.sort();
    }

    public void btnChangeUserAction(ActionEvent actionEvent) {
    }

    public void btnExitAction(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void selectDiskAction(ActionEvent actionEvent) {
    }

    public void btnPathUpAction(ActionEvent actionEvent) {
    }

    public void btnCopyAction(ActionEvent actionEvent) {
    }

    public void btnMoveAction(ActionEvent actionEvent) {
    }

    public void btnDeleteAction(ActionEvent actionEvent) {
    }

    public void updateList(Path path) {
        FileInfo fileInfo = new FileInfo(path);
        tablePC.getItems().clear();
        tablePC.getItems().addAll(fileInfo.getSuccessor());
        tablePC.sort();
    }

    private String formatItemSize(Long item) {
        StringBuilder format = new StringBuilder();
        if (item >= 1048576) {
            format.append(item / 1048576);
            format.append(" MB");
            return format.toString();
        } else if (item >= 1024) {
            format.append(item / 1024);
            format.append(" KB");
            return format.toString();
        } else if (item > 0) {
            format.append("1 KB");
            return format.toString();
        } else {
            return "0 KB";
        }
    }

    private void openFile() {

    }


}
