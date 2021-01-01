package com.ttf.gourd.client;

import java.io.*;
import java.net.Socket;
import java.util.*;

import com.ttf.gourd.constant.Constant;
import com.ttf.gourd.creature.Creature;
import com.ttf.gourd.equipment.EquipmentFactory;
import com.ttf.gourd.protocol.Msg;
import com.ttf.gourd.protocol.NoParseMsg;
import com.ttf.gourd.stage.SceneController;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class Camp {
    protected HashMap<Integer, Creature> gourdFamily = new HashMap<>();
    protected HashMap<Integer, Creature> monsterFamily = new HashMap<>();

    protected SceneController sceneController;
    protected ObjectInputStream in;
    protected ObjectOutputStream out;
    protected Socket socket;

    protected EquipmentFactory equipmentFactory = null;
    MsgController msgController = null;

    protected final Random randomNum = new Random(System.currentTimeMillis());

    public Camp(Socket socket, SceneController sceneController,
                ObjectInputStream in, ObjectOutputStream out) {
        this.socket = socket;
        this.sceneController = sceneController;
        this.in = in;
        this.out = out;
        ArrayList<ImageView> imageViews = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            ImageView imageView = new ImageView();
            imageView.setVisible(false);
            imageView.setDisable(true);
            imageViews.add(imageView);
            sceneController.addImageViewToMapPane(imageView);
        }
        equipmentFactory = new EquipmentFactory(imageViews);
    }

    public void prepareForGame() {
        Button button = new Button();
        button.setPrefHeight(10);
        button.setPrefHeight(30);
        button.setText("准备开始");
        button.setLayoutX(Constant.FIGHT_PANE_WIDTH / 2 - 15);
        button.setLayoutY(Constant.FIGHT_PANE_HEIGHT / 2);
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    new NoParseMsg(Msg.FINISH_FLAG_MSG).sendMsg(out);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                button.setVisible(false);
                button.setDisable(true);
                prepareCountDown();
            }
        });
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                sceneController.getMapPane().getChildren().add(button);
            }
        });
    }

    public void prepareCountDown() {
        Text text = new Text();
        text.setFont(Font.font("FangSong", 30));
        text.setTextAlignment(TextAlignment.CENTER);
        text.setLayoutX(Constant.FIGHT_PANE_WIDTH / 2 - 15);
        text.setLayoutY(Constant.FIGHT_PANE_HEIGHT / 2);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                sceneController.getMapPane().getChildren().add(text);
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                bindDragEvent();
                while (true) {
                    try {
                        int msgType = in.readInt();
                        if (msgType == Msg.COUNT_DOWN_MSG) {
                            msgController.getMsgClass(msgType, in);
                            int timeRemaining = msgController.getTimeRemaining();
                            text.setText(String.valueOf(timeRemaining));
                        } else if (msgType == Msg.START_GAME_MSG) {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    text.setVisible(false);
                                    text.setDisable(true);
                                }
                            });
                            notifyServerImagePosition();
                            break;
                        }
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void bindDragEvent() {
        System.out.println("Camp Event");
    }

    public void notifyServerImagePosition() throws IOException {
        System.out.println("notify server image position");
    }
}