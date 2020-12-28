package com.sjq.gourd.stage;

import com.sjq.gourd.bullet.Bullet;
import com.sjq.gourd.client.GameClient;
import com.sjq.gourd.server.GameServer;
import com.sjq.gourd.creature.Creature;
import com.sjq.gourd.localtest.GameStart;
import com.sjq.gourd.tool.PositionXY;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.regex.Pattern;

import javafx.scene.shape.Shape;
import javafx.scene.text.Text;


public class SceneController {
    @FXML
    private Pane StartScene;
    @FXML
    private Pane ConnectScene;
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
            System.out.println("port: " + port);
            Thread serverThread = new GameServer(port);
            serverThread.start();
        } catch (Exception e) {
            e.printStackTrace();
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
            System.out.println("请输入规范的ip和port地址");
        }
        try {
            ConnectScene.setVisible(false);
            ConnectScene.setDisable(true);
            fightScene.setVisible(true);
            fightScene.setDisable(false);
            System.out.println("ipString: " + ipString + " " + "portString: " + portString);
            new GameClient(ipString, Integer.parseInt(portString), this).run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addImageViewToFightMapPane(ImageView tempImageView) {
        fightScene.getChildren().add(tempImageView);
    }

    public void addImageViewToMapPane(ImageView tempImageView) {
        mapPane.getChildren().add(tempImageView);
    }

    public void addProgressBarToMapPane(ProgressBar progressBar) {
        mapPane.getChildren().add(progressBar);
    }

    public void addShapeToMapPane(Shape shape){
        mapPane.getChildren().add(shape);
    }

    public Pane getMapPane() {
        return mapPane;
    }

    public Pane getFightScene() {return fightScene;}
}
