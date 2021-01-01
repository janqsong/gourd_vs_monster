package com.ttf.gourd.stage;

import com.ttf.gourd.client.GameClient;
import com.ttf.gourd.localplayback.LoadPlayBackFiles;
import com.ttf.gourd.server.GameServer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.regex.Pattern;

import javafx.stage.Stage;


public class SceneController {
    @FXML
    private Pane basePane;
    @FXML
    private Pane startScene;
    @FXML
    private Pane connectScene;
    @FXML
    private Pane fightScene;
    @FXML
    private Pane mapPane;
    @FXML
    private TextField ServerPortText2;
    @FXML
    private TextField ServerIpText;
    @FXML
    private TextField ServerPortText1;

    @FXML
    void AboutUsMouseClickEvent(MouseEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("关于游戏");
        alert.setContentText("参见https://github.com/JansonSong/gourd_vs_monster\n\n" +
                "作者：Xiang-Xiaoyu 联系方式：xiaoyu-xiang@qq.com\n" +
                "作者：JansonSong 联系方式：1149602149@qq.com\n\n" +
                "此项目为2020年秋季NJU JAVA大作业，后期可能会进行后续的修改bug以及功能的添加。\n\n");
        alert.show();
    }

    @FXML
    void ExitMouseClickEvent(MouseEvent event) {
        Platform.exit();
    }

    @FXML
    void LocalPlaybackMouseClickEvent(MouseEvent event) {
        Stage stage = (Stage) basePane.getScene().getWindow();
        startScene.setVisible(false);
        startScene.setDisable(true);
        fightScene.setVisible(true);
        fightScene.setDisable(false);
        mapPane.setVisible(true);
        mapPane.setDisable(false);
        new LoadPlayBackFiles(stage, this).loadFiles();
    }

    @FXML
    void NetPlayMouseClickEvent(MouseEvent event) {
        startScene.setVisible(false);
        startScene.setDisable(true);
        connectScene.setVisible(true);
        connectScene.setDisable(false);
    }

    @FXML
    void CreateServerMouseClick(MouseEvent event) {
        String text = ServerPortText2.getText();
        String portPattern = "\\d{4,5}";

        boolean isMatch = Pattern.matches(portPattern, text);
        if (!isMatch) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("创建服务器失败");
            alert.setContentText("创建服务器端口不符合规范，请重新输入");
            alert.show();
            return;
        }
        int port = Integer.parseInt(text);
        if (port < 5001 || port > 65535) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("创建服务器失败");
            alert.setContentText("创建服务器端口不符合规范，请重新输入");
            alert.show();
            return;
        }

        try {
//            System.out.println("port: " + port);
            Thread serverThread = new GameServer(port);
            serverThread.start();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setHeaderText("创建服务器成功");
            alert.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("创建服务器失败");
            alert.setContentText("该端口已经被使用");
            alert.show();
        }
    }

    @FXML
    void ConnectServerMouseClick(MouseEvent event) {
        String ipString = ServerIpText.getText();
        String portString = ServerPortText1.getText();

        String ipPattern = "\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3}";
        String portPattern = "\\d{4,5}";

        boolean ipMatch = Pattern.matches(ipPattern, ipString);
        boolean portMatch = Pattern.matches(portPattern, portString);

        if (!ipMatch && !portMatch) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("连接服务器失败");
            alert.setContentText("请输入规范的ip和port地址");
            alert.show();
            return;
        }
        try {
            connectScene.setVisible(false);
            connectScene.setDisable(true);
            fightScene.setVisible(true);
            fightScene.setDisable(false);
//            System.out.println("ipString: " + ipString + " " + "portString: " + portString);
            new GameClient(ipString, Integer.parseInt(portString), this).run();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setHeaderText("连接服务器成功");
            alert.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setHeaderText("连接服务器失败");
//            alert.setContentText("请检查服务器ip和port是否输入错误，或者该服务器还未建立");
            alert.show();
        }
    }

    public void backToConnectScene() {

    }

    public void addImageViewToMapPane(ImageView tempImageView) {
        mapPane.getChildren().add(tempImageView);
    }

    public Pane getMapPane() {
        return mapPane;
    }

    public Pane getFightScene() {return fightScene;}

    public Pane getConnectScene() {
        return connectScene;
    }
}
