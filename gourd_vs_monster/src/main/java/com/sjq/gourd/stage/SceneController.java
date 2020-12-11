package com.sjq.gourd.stage;

import com.sjq.gourd.client.GameClient;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.regex.*;

import com.sjq.gourd.server.*;

public class SceneController {

    @FXML
    private Pane StartScene;

    @FXML
    private Pane ConnectScene;

    @FXML
    private Pane fightScene;

    @FXML
    private TextField ServerPortText2;

    @FXML
    private TextField ServerIpText;

    @FXML
    private TextField ServerPortText1;

    @FXML
    void AboutUsMouseClickEvent(MouseEvent event) {
        System.out.println("about");
    }

    @FXML
    void ExitMouseClickEvent(MouseEvent event) {
        System.out.println("exit");
    }

    @FXML
    void LocalPlaybackMouseClickEvent(MouseEvent event) {
        System.out.println("localplay");
    }

    @FXML
    void NetPlayMouseClickEvent(MouseEvent event) {
        StartScene.setVisible(false);
        StartScene.setDisable(true);
        ConnectScene.setVisible(true);
        ConnectScene.setDisable(false);
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
            System.out.println("请输入规范的ip和port地址");
        }
        try {
            ConnectScene.setVisible(false);
            ConnectScene.setDisable(true);
            fightScene.setVisible(true);
            fightScene.setDisable(false);
            new GameClient(ipString, Integer.parseInt(portString), this).run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void CreateServerMouseClick(MouseEvent event) {
        String text = ServerPortText2.getText();
        String portPattern = "\\d{4,5}";

        boolean isMatch = Pattern.matches(portPattern, text);
        if (!isMatch) {
            System.out.println("创建服务器端口不符合规范，请重新输入");
            return;
        }
        int port = Integer.parseInt(text);
        if (port < 5001 || port > 65535) {
            System.out.println("创建服务器端口不符合规范，请重新输入");
            return;
        }

        try {
            Thread serverThread = new GameServer(port);
            serverThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addImageView(ImageView tempImageView) {
        fightScene.getChildren().add(tempImageView);
    }

    public void startGame() {
        System.out.println("startGame");
    }

}
