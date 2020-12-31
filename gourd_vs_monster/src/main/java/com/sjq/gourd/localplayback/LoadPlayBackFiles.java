package com.sjq.gourd.localplayback;

import com.sjq.gourd.stage.SceneController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

public class LoadPlayBackFiles {

    SceneController sceneController = null;
    Stage stage = null;
    TableView<PlayBackFile> tableView = null;
    Pane playbackPane = new Pane();

    public LoadPlayBackFiles(Stage stage, SceneController sceneController) {
        this.sceneController = sceneController;
        this.stage = stage;
        playbackPane.setVisible(true);
        playbackPane.setDisable(false);
        playbackPane.setPrefSize(700, 700);
        playbackPane.setLayoutX(250);
        playbackPane.setLayoutY(50);
        playbackPane.setStyle("-fx-background:transparent;");
        this.sceneController.getMapPane().getChildren().add(playbackPane);
    }

    public void startPlayBackGame(File file) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open PlayBack File");
        new GamePlayBack(file, sceneController, this).playBackGame();
    }

    public void backToSelectFile() {
        playbackPane.setVisible(true);
        playbackPane.setDisable(false);
        this.sceneController.getMapPane().getChildren().add(playbackPane);
    }

    public ArrayList<PlayBackFile> getPlaybackFileList(File dirFile) {
        ArrayList<PlayBackFile> fileList = new ArrayList<>();
        File[] listFiles = dirFile.listFiles();
        String filePattern = ".+(.back)";

        if(listFiles == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("导入失败");
            alert.setContentText("导入文件夹不存在");
            alert.show();
        } else {
            for (File listFile : listFiles) {
                if (listFile.isFile()) {
                    boolean isMatch = Pattern.matches(filePattern, listFile.getName());
                    if (isMatch) {
                        try {
                            FileTime time = Files.readAttributes(Paths.get(listFile.getPath()),
                                    BasicFileAttributes.class).creationTime();
                            Date date = new Date(time.toMillis());
                            fileList.add(new PlayBackFile(listFile.getName(), date, listFile.getPath()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return fileList;
    }

    public void loadFiles() {
        initScene();
        ArrayList<PlayBackFile> fileList = getPlaybackFileList(new File("C:/Users/Dlee/Desktop/playbackFiles"));
        ObservableList<PlayBackFile> obList = FXCollections.observableArrayList(fileList);

        tableView = new TableView<>(obList);
        tableView.setPrefSize(700, 500);
        tableView.setLayoutX(0);
        tableView.setLayoutY(30);
        tableView.setStyle("/style.css");

        TableColumn idColumn = new TableColumn<>("序号");
        idColumn.setMinWidth(68);
        idColumn.setCellFactory(col -> {
            return new TableCell<PlayBackFile, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                        int rowIndex = this.getIndex() + 1;
                        this.setText(String.valueOf(rowIndex));
                    }
                }
            };
        });

        TableColumn nameColumn = new TableColumn("回放文件名");
        nameColumn.setMinWidth(230);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("fileName"));

        TableColumn timeColumn = new TableColumn("创建时间");
        timeColumn.setMinWidth(400);
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("fileCreateDate"));
        tableView.getColumns().addAll(idColumn, nameColumn, timeColumn);

        tableView.setRowFactory(new Callback<TableView<PlayBackFile>, TableRow<PlayBackFile>>() {
            @Override
            public TableRow<PlayBackFile> call(TableView<PlayBackFile> param) {
                TableRow<PlayBackFile> row = new TableRow<PlayBackFile>();
                row.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                        PlayBackFile playBackFile = row.getItem();
                        try {
                            File file = new File(playBackFile.getFilePath());
                            playbackPane.setVisible(false);
                            playbackPane.setDisable(true);
                            startPlayBackGame(file);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                return row;
            }
        });
        playbackPane.getChildren().addAll(tableView);
    }

    public void initScene() {
        Button button = new Button();
        button.setVisible(true);
        button.setDisable(false);
        button.setText("添加回放文件");
        button.setLayoutX(0);
        button.setLayoutY(560);
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open PlayBack File");
                File file = fileChooser.showOpenDialog(stage);
                if(file == null) return;;
                String filePattern = ".+(.back)";
                boolean isMatch = Pattern.matches(filePattern, file.getName());
                if (!isMatch) return;
                try {
                    for(PlayBackFile playBackFile : tableView.getItems()) {
                        File itemFile = new File(playBackFile.getFilePath());
                        if(itemFile.getAbsolutePath().equals(file.getAbsolutePath())) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setHeaderText("导入失败");
                            alert.setContentText("导入文件已经存在目录中");
                            alert.show();
                            return;
                        }
                    }
                    FileTime time = Files.readAttributes(Paths.get(file.getPath()),
                            BasicFileAttributes.class).creationTime();
                    Date date = new Date(time.toMillis());
                    PlayBackFile playBackFile = new PlayBackFile(file.getName(), date, file.getPath());
                    tableView.getItems().add(playBackFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Button inputButton = new Button();
        inputButton.setVisible(true);
        inputButton.setDisable(false);
        inputButton.setText("批量导入");
        inputButton.setLayoutX(100);
        inputButton.setLayoutY(560);
        inputButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DirectoryChooser directoryChooser=new DirectoryChooser();
                File file = directoryChooser.showDialog(stage);
                if(file == null || !file.isDirectory()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("导入失败");
                    alert.setContentText("导入文件夹未找到");
                    alert.show();
                    return;
                }
                ArrayList<PlayBackFile> fileList = getPlaybackFileList(file);
                ObservableList<PlayBackFile> obList = FXCollections.observableArrayList(fileList);
                int totalCount = 0;
                int successCount = 0;
                boolean flag = false;
                for(PlayBackFile playBackFile : obList) {
                    File tempFile = playBackFile.getFile();
                    for(PlayBackFile itemPlayBackFile : tableView.getItems()) {
                        File itemFile = itemPlayBackFile.getFile();
                        if(tempFile.getAbsolutePath().equals(itemFile.getAbsolutePath())) {
                            flag = true;
                        }
                    }
                    if(!flag) {
                        successCount++;
                        tableView.getItems().add(playBackFile);
                    }
                    totalCount++;
                    flag = false;
                }
                if(successCount == 0) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("导入失败");
                    alert.setContentText("\n错误原因：\n文件已存在或文件夹中不存在指定后缀为.back文件！");
                    alert.show();
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("导入成功");
                    alert.setContentText("共导入" + totalCount + "个文件，成功导入" + successCount + "个文件，错误导入"
                            + (totalCount - successCount) + "\n错误原因：\n文件已存在！");
                    alert.show();
                }
            }
        });

        Button deleteButton = new Button();
        deleteButton.setVisible(true);
        deleteButton.setDisable(false);
        deleteButton.setText("删除");
        deleteButton.setLayoutX(650);
        deleteButton.setLayoutY(0);
        deleteButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int index = tableView.getSelectionModel().getFocusedIndex();
                tableView.getItems().remove(index);
            }
        });

        Button selectButton = new Button();
        selectButton.setVisible(true);
        selectButton.setDisable(false);
        selectButton.setText("播放");
        selectButton.setLayoutX(650);
        selectButton.setLayoutY(560);
        selectButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    File file = new File(tableView.getSelectionModel().getSelectedItem().getFilePath());
                    playbackPane.setVisible(false);
                    playbackPane.setDisable(true);
                    startPlayBackGame(file);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        Text myCreatureText = new Text();
        myCreatureText.setVisible(true);
        myCreatureText.setText("回放文件目录");
        myCreatureText.setLayoutX(0);
        myCreatureText.setLayoutY(0);
        myCreatureText.setFont(Font.font ("Microsoft YaHei ", FontWeight.BOLD, FontPosture.ITALIC, 30));
        myCreatureText.setFill(Color.RED);

        playbackPane.getChildren().addAll(button, inputButton, deleteButton, selectButton, myCreatureText);
    }
}
