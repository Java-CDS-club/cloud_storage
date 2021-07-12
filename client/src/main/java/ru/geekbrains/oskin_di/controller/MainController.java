package ru.geekbrains.oskin_di.controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import ru.geekbrains.oskin_di.FileInfo;
import ru.geekbrains.oskin_di.FileType;
import ru.geekbrains.oskin_di.MainWindow;
import ru.geekbrains.oskin_di.command.Command;
import ru.geekbrains.oskin_di.command.TypeCommand;
import ru.geekbrains.oskin_di.service.NetworkService;
import ru.geekbrains.oskin_di.util.FieldType;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;


public class MainController implements Initializable {

    private static String cloudPath = LoginController.getCloudPath();

    private static NetworkService networkService = LoginController.getNetworkService();

    @FXML
    private AnchorPane window;

    @FXML
    private TableView<FileInfo> tableLocal;

    @FXML
    private TableView<FileInfo> tableCloud;

    @FXML
    private ComboBox<String> disksBox;

    @FXML
    private ComboBox<String> cloudBox;

    @FXML
    private TextField pathLocalField;

    @FXML
    private TextField pathCloudField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        addColumnNamesTable(tableLocal);
        addColumnNamesTable(tableCloud);

        pathLocalField.setText(System.getProperty("user.dir"));
        pathCloudField.setText(cloudPath);

        updateLocalTable();
        updateTableCloud();

        createDiskBox();

        cloudBox.getItems().clear();
        cloudBox.getItems().add(cloudPath);


        tableLocal.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2) {
                    openFile();
                }
                if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                    createContextMenu().show(tableLocal, mouseEvent.getScreenX(), mouseEvent.getScreenY());
                }
            }
        });


    }

    private void addColumnNamesTable(TableView<FileInfo> tableView) {
        tableView.getColumns().addAll(createColumn(FieldType.NAME), createColumn(FieldType.TYPE), createColumn(FieldType.SIZE), createColumn(FieldType.DATE));
    }

    private TableColumn<FileInfo, String> createColumn(FieldType fieldType) {
        TableColumn<FileInfo, String> fileColumn = new TableColumn<>();

        switch (fieldType) {
            case DATE -> fileColumn.setCellValueFactory(this::callTime);
            case NAME -> fileColumn.setCellValueFactory(this::callFilename);
            case SIZE -> fileColumn.setCellValueFactory(this::callSize);
            case TYPE -> fileColumn.setCellValueFactory(this::callType);
        }

        fileColumn.setText(fieldType.getInfo());
        fileColumn.setPrefWidth(120);

        return fileColumn;
    }

    private ObservableValue<String> callFilename(TableColumn.CellDataFeatures<FileInfo, String> param) {
        return new SimpleStringProperty(param.getValue().getFilename());
    }

    private ObservableValue<String> callType(TableColumn.CellDataFeatures<FileInfo, String> param) {
        return new SimpleStringProperty(param.getValue().getType().getName());
    }

    private ObservableValue<String> callSize(TableColumn.CellDataFeatures<FileInfo, String> param) {
        return new SimpleObjectProperty(param.getValue().getStringSize());
    }

    private ObservableValue<String> callTime(TableColumn.CellDataFeatures<FileInfo, String> param) {
        return new SimpleObjectProperty(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(param.getValue().getLastModified()));
    }

    private void autoFocusTable(TableView<FileInfo> tableView) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                tableView.requestFocus();
                tableView.getSelectionModel().select(0);
                tableView.getFocusModel().focus(0);
            }
        });
    }

    private void updateTableCloud() {
        networkService.sendCommand(new Command(TypeCommand.UPDATE_CLOUD_TABLE, cloudPath), resultCommand -> {
            Platform.runLater(() -> {
                if (resultCommand.getTypeCommand() == TypeCommand.NEW_CLOUD_TABLE) {
                    pathCloudField.setText(resultCommand.getFileInfo().getStringPath());
                    fillTable(tableCloud, resultCommand.getFileInfo());
                }
            });
        });
        autoFocusTable(tableCloud);
    }

    private void updateLocalTable() {
        FileInfo fileInfo = new FileInfo(Paths.get(getCurrentPath(pathLocalField)));
        fileInfo.writeSuccessor();
        pathLocalField.setText(fileInfo.getStringPath());
        fillTable(tableLocal, fileInfo);
        autoFocusTable(tableLocal);
    }


    private void fillTable(TableView<FileInfo> tableView, FileInfo fileInfo) {
        tableView.getItems().clear();
        tableView.getItems().addAll(fileInfo.getSuccessor());
        tableView.sort();
    }

    private void openFile() {
        Path path = Paths.get(getCurrentPath(pathLocalField)).resolve(tableLocal.getSelectionModel().getSelectedItem().getFilename());
        if (Files.isDirectory(path)) {
            pathLocalField.setText(path.toString());
            updateLocalTable();
        }
    }

    private ContextMenu createContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem openItem = new MenuItem("Открыть");
        openItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                openFile();
            }
        });
        contextMenu.getItems().add(openItem);
        MenuItem copyItem = new MenuItem("Копировать");
        copyItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                unloadFile();
            }
        });
        contextMenu.getItems().add(copyItem);
        MenuItem moveItem = new MenuItem("Переместить");
        moveItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
            }
        });
        contextMenu.getItems().add(moveItem);
        MenuItem deleteItem = new MenuItem("Удалить");
        deleteItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
            }
        });
        contextMenu.getItems().add(deleteItem);
        return contextMenu;
    }

    private void createDiskBox() {
        disksBox.getItems().clear();
        for (Path p : FileSystems.getDefault().getRootDirectories()) {
            disksBox.getItems().add(p.toString());
        }
        disksBox.getSelectionModel().select(0);
    }


    private void unloadFile() {
        Path path = Paths.get(pathLocalField.getText(), getSelectedFilename(tableLocal));
        File file = new File(path.toString());

        if (file.isDirectory()) {
            return;
        }

        FileInfo fileInfo = new FileInfo(path);
        String cloudPath = Paths.get(pathCloudField.getText(), getSelectedFilename(tableLocal)).toString();
        fileInfo.setFuturePath(cloudPath);
        Command command = new Command(TypeCommand.UNLOADING, fileInfo);

        networkService.sendCommand(command, (resultCommand) -> {
            if (resultCommand.getTypeCommand() == TypeCommand.UNLOADING_START) {
                networkService.unloadFile(command, (result) -> {
                    if (result.getTypeCommand() == TypeCommand.UNLOADING_END) {
                        updateTableCloud();
                    }
                });
            }
        });
    }

    private void loadFile() {
        Path path = Paths.get(pathCloudField.getText(), getSelectedFilename(tableCloud));
        FileType fileType = tableCloud.getSelectionModel().getSelectedItem().getType();
        String localPath = Paths.get(pathLocalField.getText(), getSelectedFilename(tableCloud)).toString();
        FileInfo fileInfo = tableCloud.getSelectionModel().getSelectedItem();
        fileInfo.setFuturePath(localPath);
        Command command = new Command(TypeCommand.LOADING, fileInfo);

        networkService.sendCommand(command, (resultCommand) -> {
            if (resultCommand.getTypeCommand() == TypeCommand.LOADING_START) {
                networkService.loadFile(new Command(TypeCommand.LOADING_START, command.getFileInfo()), (result) -> {
                    if (result.getTypeCommand() == TypeCommand.LOADING_END) {
                        updateLocalTable();
                    }
                });
            }
        });
    }

    private void deleteFileLocal() {
        Path path = Paths.get(pathLocalField.getText(), getSelectedFilename(tableLocal));
        File file = new File(path.toString());

        if (file.isDirectory()) {
            return;
        }
        try {
            Files.delete(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        updateLocalTable();
    }

    private void deleteFileCloud() {
        Path path = Paths.get(pathCloudField.getText(), getSelectedFilename(tableCloud));
        FileType fileType = tableCloud.getSelectionModel().getSelectedItem().getType();

        if (fileType == FileType.DIRECTORY) {
            return;
        }

        FileInfo fileInfo = tableCloud.getSelectionModel().getSelectedItem();
        Command command = new Command(TypeCommand.DELETION, fileInfo);
        networkService.sendCommand(command, (resultCommand) -> {
            if (resultCommand.getTypeCommand() == TypeCommand.DELETION_END) {
                updateTableCloud();
            }
        });
    }

    public void btnExitAction(ActionEvent actionEvent) {
        Platform.exit();
        networkService.closeConnection();
    }

    public void selectDiskAction(ActionEvent actionEvent) {
        ComboBox<String> element = (ComboBox<String>) actionEvent.getSource();
        pathLocalField.setText(element.getSelectionModel().getSelectedItem());
        updateLocalTable();
    }

    public void btnPathUpAction(ActionEvent actionEvent) {
        Path upperPath = Paths.get(getCurrentPath(pathLocalField)).getParent();
        pathLocalField.setText(upperPath.toString());
        if (upperPath != null) {
            updateLocalTable();
        }
    }

    public void btnCopyAction(ActionEvent actionEvent) {
        if (tableLocal.isFocused()) {
            unloadFile();
        }
        if (tableCloud.isFocused()) {
            loadFile();
        }
    }


    public void btnDeleteAction(ActionEvent actionEvent) {
        if (tableLocal.isFocused()) {
            deleteFileLocal();
        }
        if (tableCloud.isFocused()) {
            deleteFileCloud();
        }
    }

    public void btnCopyFromCloud(ActionEvent actionEvent) {
        loadFile();
    }

    public void btnCopyFromLocal(ActionEvent actionEvent) {
        unloadFile();
    }

    private String getSelectedFilename(TableView<FileInfo> tableView) {
        return tableView.getSelectionModel().getSelectedItem().getFilename();
    }

    private String getCurrentPath(TextField textField) {
        return textField.getText();
    }


}
