package com.sjq.gourd.stage;

import com.sjq.gourd.client.GameClient;
import com.sjq.gourd.client.MsgController;
import com.sjq.gourd.constant.ImageUrl;
import com.sjq.gourd.creature.GourdClass;
import com.sjq.gourd.creature.MonsterClass;
import com.sjq.gourd.protocol.Msg;
import com.sjq.gourd.protocol.NoParseMsg;
import com.sjq.gourd.protocol.PositionNotifyMsg;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.*;

import com.sjq.gourd.server.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

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

    public Text notificationText;

    private DataInputStream in;
    private DataOutputStream out;
    private MsgController msgController;
    private String campType;
    private HashMap<Integer, GourdClass> gourdFamily = new HashMap<Integer, GourdClass>();
    private HashMap<Integer, MonsterClass> monsterFamily = new HashMap<Integer, MonsterClass>();

    private ImageView selectOwnCampCreatureImage;
    private class PositionXY {
        public double X;
        public double Y;
        public PositionXY(double x, double y) {
            X = x;
            Y = y;
        }
        public void setPosition(double x, double y) {
            X = x;
            Y = y;
        }
    };
    PositionXY beginPosition = new PositionXY(0, 0);

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
        System.out.println("mouseevent");
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

    public void initGameSceneController(DataInputStream in, DataOutputStream out, String campType,
                                        HashMap<Integer, GourdClass> gourdFamily,
                                        HashMap<Integer, MonsterClass> monsterFamily) {
        this.in = in;
        this.out = out;
        this.campType = campType;
        this.gourdFamily = gourdFamily;
        this.monsterFamily = monsterFamily;
        msgController = new MsgController(gourdFamily, monsterFamily);
        notificationText = new Text();
        notificationText.setText("等待其他玩家加入");
        notificationText.setFont(Font.font("FangSong", 30));
        notificationText.setWrappingWidth(240);
        notificationText.setTextAlignment(TextAlignment.CENTER);
        double width = notificationText.getWrappingWidth();
        notificationText.setLayoutX(600 - width / 2);
        notificationText.setLayoutY(260);
        fightScene.getChildren().add(notificationText);
    }

    public void gourdStartGame() {
        waitForAnother();
    }

    public void waitForAnother() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        int msgType = in.readInt();
                        if(msgType == Msg.PREPARE_GAME_MSG) break;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                gourdPrepareForGame();
            }
        }).start();
    }

    public void gourdPrepareForGame() {
        for(GourdClass gourdMember : gourdFamily.values()) {
            gourdMember.getCreatureImageView().setVisible(true);
            gourdMember.getCreatureImageView().setDisable(false);
            gourdMember.getCreatureImageView().setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    beginPosition.setPosition(event.getX(), event.getY());
                    gourdMember.setSelectCreatureImageView();
                    selectOwnCampCreatureImage = gourdMember.getCreatureImageView();
                }
            });
            gourdMember.getCreatureImageView().setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    PositionXY currentPosition = new PositionXY(event.getSceneX(), event.getSceneY());
                    double deltaX = currentPosition.X - beginPosition.X;
                    double deltaY = currentPosition.Y - beginPosition.Y;
                    deltaX = (deltaX > 520) ? 520 : deltaX;
                    deltaX = (deltaX < 0) ? 0 : deltaX;
                    deltaY = (deltaY < 0) ? 0 : deltaY;
                    deltaY = (deltaY > 600) ? 600 : deltaY;
                    gourdMember.getCreatureImageView().setLayoutX(deltaX);
                    gourdMember.getCreatureImageView().setLayoutY(deltaY);
                }
            });
        }
        while(true) {
            try {
                int msgType = in.readInt();
                System.out.println(msgType);
                if (msgType == Msg.COUNT_DOWN_MSG) {
                    msgController.getMsgClass(msgType, in);
                    notificationText.setText(String.valueOf(msgController.getTimeRemaining()));
                } else if (msgType == Msg.START_GAME_MSG) {
                    if(campType.equals("Gourd")) {
                        for(Map.Entry<Integer, GourdClass> entry : gourdFamily.entrySet()) {
                            int creatureId = entry.getKey();
                            GourdClass gourdMember = entry.getValue();
                            ImageView tempImageView = gourdMember.getCreatureImageView();
                            new PositionNotifyMsg("Gourd", creatureId,
                                    tempImageView.getLayoutX(), tempImageView.getLayoutY()).sendMsg(out);
                        }
                    } else if(campType.equals("Monster")) {
                        for(Map.Entry<Integer, MonsterClass> entry : monsterFamily.entrySet()) {
                            int creatureId = entry.getKey();
                            MonsterClass monsterMember = entry.getValue();
                            ImageView tempImageView = monsterMember.getCreatureImageView();
                            new PositionNotifyMsg("Monster", creatureId,
                                    tempImageView.getLayoutX(), tempImageView.getLayoutY()).sendMsg(out);
                        }
                    }
                    new NoParseMsg(Msg.FINISH_FLAG_MSG).sendMsg(out);
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        while(true) {
            try {
                int msgType = in.readInt();
                System.out.println(msgType);
                if(msgType == Msg.POSITION_NOTIFY_MSG) {
                    msgController.getMsgClass(msgType, in);
                } else if(msgType == Msg.START_GAME_MSG) {
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("fight");
    }


    public void monsterStartGame() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                monsterPrepareForGame();
            }
        }).start();
    }

    public void monsterPrepareForGame() {
        for(MonsterClass monsterMember : monsterFamily.values()) {
            monsterMember.getCreatureImageView().setVisible(true);
            monsterMember.getCreatureImageView().setDisable(false);
            monsterMember.getCreatureImageView().setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    beginPosition.setPosition(event.getX(), event.getY());
                    monsterMember.setSelectCreatureImageView();
                    selectOwnCampCreatureImage = monsterMember.getCreatureImageView();
                }
            });
            monsterMember.getCreatureImageView().setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    PositionXY currentPosition = new PositionXY(event.getSceneX(), event.getSceneY());
                    double deltaX = currentPosition.X - beginPosition.X;
                    double deltaY = currentPosition.Y - beginPosition.Y;
                    deltaX = (deltaX > 520) ? 520 : deltaX;
                    deltaX = (deltaX < 0) ? 0 : deltaX;
                    deltaY = (deltaY < 0) ? 0 : deltaY;
                    deltaY = (deltaY > 600) ? 600 : deltaY;
                    monsterMember.getCreatureImageView().setLayoutX(deltaX);
                    monsterMember.getCreatureImageView().setLayoutY(deltaY);
                }
            });
        }
        while(true) {
            try {
                int msgType = in.readInt();
                System.out.println(msgType);
                if (msgType == Msg.COUNT_DOWN_MSG) {
                    msgController.getMsgClass(msgType, in);
                    System.out.println(notificationText);
                    notificationText.setText(String.valueOf(msgController.getTimeRemaining()));
                } else if (msgType == Msg.START_GAME_MSG) {
                    if(campType.equals("Gourd")) {
                        for(Map.Entry<Integer, GourdClass> entry : gourdFamily.entrySet()) {
                            int creatureId = entry.getKey();
                            GourdClass gourdMember = entry.getValue();
                            ImageView tempImageView = gourdMember.getCreatureImageView();
                            new PositionNotifyMsg("Gourd", creatureId,
                                    tempImageView.getLayoutX(), tempImageView.getLayoutY()).sendMsg(out);
                        }
                    } else if(campType.equals("Monster")) {
                        for(Map.Entry<Integer, MonsterClass> entry : monsterFamily.entrySet()) {
                            int creatureId = entry.getKey();
                            MonsterClass monsterMember = entry.getValue();
                            ImageView tempImageView = monsterMember.getCreatureImageView();
                            new PositionNotifyMsg("Monster", creatureId,
                                    tempImageView.getLayoutX(), tempImageView.getLayoutY()).sendMsg(out);
                        }
                    }
                    new NoParseMsg(Msg.FINISH_FLAG_MSG).sendMsg(out);
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        while(true) {
            try {
                int msgType = in.readInt();
                if(msgType == Msg.POSITION_NOTIFY_MSG) {
                    msgController.getMsgClass(msgType, in);
                } else if(msgType == Msg.START_GAME_MSG) {
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("fight");
    }

    public void addImageView(ImageView tempImageView) {
        fightScene.getChildren().add(tempImageView);
    }

    public void startGame() {
        System.out.println("startGame");
    }

}
