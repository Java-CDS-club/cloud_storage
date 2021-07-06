package ru.geekbrains.oskin_di.controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import ru.geekbrains.oskin_di.FileInfo;
import ru.geekbrains.oskin_di.MainWindow;
import ru.geekbrains.oskin_di.command.Command;
import ru.geekbrains.oskin_di.command.TypeCommand;
import ru.geekbrains.oskin_di.service.NetworkService;
import ru.geekbrains.oskin_di.util.FieldType;

import java.io.File;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;


public class MainController implements Initializable {

    private static String cloudPath = MainWindow.getCloudPath();

    private static NetworkService networkService = MainWindow.getNetworkService();

    @FXML
    AnchorPane window;

    @FXML
    TableView<FileInfo> tableLocal;

    @FXML
    TableView<FileInfo> tableCloud;

    @FXML
    ComboBox<String> disksBox;

    @FXML
    ComboBox<String> cloudBox;

    @FXML
    TextField pathLocalField;

    @FXML
    TextField pathCloudField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        addColumnNamesTable (tableLocal);
        addColumnNamesTable (tableCloud);

        pathLocalField.setText(System.getProperty("user.dir"));
        pathCloudField.setText (cloudPath);

        updateLocalTable();
        updateTableCloud();

        disksBox.getItems().clear();
        for (Path p : FileSystems.getDefault().getRootDirectories()) {
            disksBox.getItems().add(p.toString());
        }
        disksBox.getSelectionModel().select(0);

        cloudBox.getItems ().clear ();
        cloudBox.getItems ().add (cloudPath);


//        ContextMenu contextMenu = new ContextMenu();
//        MenuItem openItem = new MenuItem("Открыть");
//        openItem.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent actionEvent) {
//
//            }
//        });
//        contextMenu.getItems().add(openItem);
//        MenuItem copyItem = new MenuItem("Копировать");
//        contextMenu.getItems().add(copyItem);
//        MenuItem moveItem = new MenuItem("Переместить");
//        contextMenu.getItems().add(moveItem);
//        MenuItem deleteItem = new MenuItem("Удалить");
//        contextMenu.getItems().add(deleteItem);


//        tablePC.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent mouseEvent) {
//                if (mouseEvent.getClickCount() == 2) {
//                    Path path = Paths.get(pathField.getText()).resolve(tablePC.getSelectionModel().getSelectedItem().getFilename());
//                    if (Files.isDirectory(path)) {
//                        updateList(path);
//                    }
//                }
//                if (mouseEvent.getButton() == MouseButton.SECONDARY) {
//                    contextMenu.show(tablePC, mouseEvent.getScreenX(), mouseEvent.getScreenY());
//                }
//            }
//        });


    }

    private void addColumnNamesTable(TableView<FileInfo> tableView){
        tableView.getColumns().addAll(createColumn (FieldType.NAME), createColumn (FieldType.TYPE), createColumn (FieldType.SIZE), createColumn (FieldType.DATE));
    }

    private TableColumn<FileInfo,String> createColumn(FieldType fieldType){
        TableColumn<FileInfo, String> fileColumn = new TableColumn<>();

        switch (fieldType){
            case DATE -> fileColumn.setCellValueFactory(this::callTime);
            case NAME -> fileColumn.setCellValueFactory(this::callFilename);
            case SIZE -> fileColumn.setCellValueFactory (this::callSize);
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

    private ObservableValue<String> callSize(TableColumn.CellDataFeatures<FileInfo,String> param) {
        return new SimpleObjectProperty(param.getValue().getStringSize());
    }

    private ObservableValue<String> callTime(TableColumn.CellDataFeatures<FileInfo, String> param) {
        return new SimpleObjectProperty(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(param.getValue().getLastModified()));
    }

    private void updateTableCloud(){
        networkService.sendCommand(new Command(TypeCommand.UPDATE_CLOUD_TABLE, cloudPath), resultCommand -> {
            Platform.runLater(() -> {
                if (resultCommand.getTypeCommand() == TypeCommand.NEW_CLOUD_TABLE) {
                    pathCloudField.setText (resultCommand.getFileInfo ().getStringPath ());
                    fillTable (tableCloud,resultCommand.getFileInfo ());
                }
            });
        });
    }

    private void updateLocalTable(){
        FileInfo fileInfo = new FileInfo (Paths.get (getCurrentPath (pathLocalField)));
        fileInfo.writeSuccessor ();
        pathLocalField.setText (fileInfo.getStringPath ());
        fillTable (tableLocal,fileInfo);
    }


    private void fillTable(TableView<FileInfo> tableView, FileInfo fileInfo){
        tableView.getItems().clear();
        tableView.getItems().addAll(fileInfo.getSuccessor());
        tableView.sort();
    }


    public void btnUploadCommand() {
        Path path = Paths.get (pathLocalField.getText (), getSelectedFilename (tableLocal));
        File file = new File(path.toString());
        if (file.isDirectory()) {
            return;
        }
        Command command = new Command(TypeCommand.UNLOADING, new FileInfo (path));
        networkService.sendCommand(command, (resultCommand) -> {
            if (resultCommand.getTypeCommand() == TypeCommand.UNLOADING_START) {
                networkService.sendFile(command, (res) -> {
                    if (res.getCommandName() == CommandType.UPLOAD_COMPLETE) {
                        updateTableCloud();
                    }
                });
            }
        });
    }

    public void btnDownloadCommand() {
        String path = getCurrentPath(cloudPathField) + File.separator + getSelectedFilename(cloudFiles);
        FileType type = cloudFiles.getSelectionModel().getSelectedItem().getType();
        if (type == FileType.DIRECTORY) {
            return;
        }
        File file = new File(path);
        Command command = new Command(CommandType.DOWNLOAD, "", new Object[]{});
        networkService.sendCommand(command, (result) -> {
            if (result.getCommandName() == CommandType.DOWNLOAD_READY) {
                networkService.sendDownloadCommand(new Command(CommandType.DOWNLOAD_READY, path, new Object[]{getSelectedFilename(cloudFiles), file.length(), getCurrentPath(localPathField)}), (res) -> {
                    if (res.getCommandName() == CommandType.DOWNLOAD_COMPLETE) {
                        refreshLocalDirs();
                    }
                });
            }
        });
    }













    public void btnChangeUserAction(ActionEvent actionEvent) {
    }

    public void btnExitAction(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void selectDiskAction(ActionEvent actionEvent) {
        ComboBox<String> element = (ComboBox<String>) actionEvent.getSource();
        pathLocalField.setText (element.getSelectionModel().getSelectedItem());
        updateLocalTable();
    }

    public void btnPathUpAction(ActionEvent actionEvent) {
        Path upperPath = Paths.get(getCurrentPath (pathLocalField)).getParent();
        pathLocalField.setText (upperPath.toString ());
        if (upperPath != null) {
            updateLocalTable();
        }
    }

    public void btnCopyAction(ActionEvent actionEvent) {
    }

    public void btnMoveAction(ActionEvent actionEvent) {
    }

    public void btnDeleteAction(ActionEvent actionEvent) {
    }

    private String getSelectedFilename(TableView<FileInfo> tableView) {
        return tableView.getSelectionModel().getSelectedItem().getFilename ();
    }

    private String getCurrentPath(TextField textField){
        return textField.getText ();
    }



}
