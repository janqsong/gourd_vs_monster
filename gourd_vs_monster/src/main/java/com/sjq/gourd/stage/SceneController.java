package com.sjq.gourd.stage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.regex.*;

import com.sjq.gourd.communication.*;

public class SceneController {

    @FXML
    private Pane start_scene;

    @FXML
    private Pane fight_scene;

    @FXML
    private Button connectButton;

    @FXML
    private Button playbackButton;

    @FXML
    private Button descriptionButton;

    @FXML
    private Button aboutButton;

    @FXML
    private Button createButton;

    @FXML
    private Button connectserverButton;

    @FXML
    private TextField serveripText;

    @FXML
    private TextField serverportText2;
    
    @FXML
    private Label serveripLabel;

    @FXML
    private Label serverportLabel2;

    @FXML
    private TextField serverportText;

    @FXML
    private Label serverportLabel;

    GameServer server;
    GameClient client;

    @FXML
    void about_us(ActionEvent event) {
        start_scene.setVisible(false);
        start_scene.setDisable(true);
        fight_scene.setVisible(true);
        fight_scene.setDisable(false);
    }

    @FXML
    void connect_server(ActionEvent event) {
        String ipString = serveripText.getText();
        String portString = serverportText2.getText();

        String ipPattern = "\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3}";
        String portPattern = "\\d{4,5}";

        boolean ipisMatch = Pattern.matches(ipPattern, ipString);
        boolean portisMatch = Pattern.matches(portPattern, portString);

        if(ipisMatch == false && portisMatch == false) {
            System.out.println("请输入规范的ip和port地址");
        }
        try {
            Thread clientthread = new GameClient(ipString, Integer.parseInt(portString));
            clientthread.start();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void create_server(ActionEvent event) {
        String text = serverportText.getText();
        String portpattern = "\\d{4,5}";

        boolean isMatch = Pattern.matches(portpattern, text);
        if(isMatch == false) {
            System.out.println("创建服务器端口不符合规范，请重新输入");
            return;
        }
        int port = Integer.parseInt(text);
        if(port < 5001 && port > 65535) {
            System.out.println("创建服务器端口不符合规范，请重新输入");
            return;
        }

        try {
            Thread serverthread = new GameServer(port);
            serverthread.start();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void game_discription(ActionEvent event) {

    }

    @FXML
    void local_playback(ActionEvent event) {

    }

    @FXML
    void start_network_fight(ActionEvent event) {
        connectButton.setVisible(false);
        playbackButton.setVisible(false);
        descriptionButton.setVisible(false);
        aboutButton.setVisible(false);
        createButton.setVisible(true);
        connectserverButton.setVisible(true);
        serveripText.setVisible(true);
        serverportText.setVisible(true);
        serverportText2.setVisible(true);
        serveripLabel.setVisible(true);
        serverportLabel.setVisible(true);
        serverportLabel2.setVisible(true);
    }

}
