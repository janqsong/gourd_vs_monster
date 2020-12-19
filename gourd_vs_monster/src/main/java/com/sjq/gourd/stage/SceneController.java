package com.sjq.gourd.stage;

import com.sjq.gourd.bullet.Bullet;
//import com.sjq.gourd.client.GameClient;
//import com.sjq.gourd.client.GourdCamp;
//import com.sjq.gourd.client.MonsterCamp;
//import com.sjq.gourd.client.MsgController;
import com.sjq.gourd.creature.Creature;
import com.sjq.gourd.localtest.GameStart;
//import com.sjq.gourd.protocol.Msg;
//import com.sjq.gourd.protocol.NoParseMsg;
//import com.sjq.gourd.protocol.PositionNotifyMsg;
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

//import com.sjq.gourd.server.*;
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

    private final Random randomNum = new Random(System.currentTimeMillis());
    private Text notificationMidText;
    private DataInputStream in;
    private DataOutputStream out;
//    private MsgController msgController;
    private String campType;
    private HashMap<Integer, Creature> gourdFamily = new HashMap<Integer, Creature>();
    private HashMap<Integer, Creature> monsterFamily = new HashMap<Integer, Creature>();

    private Creature selectOwnCampCreature;
    PositionXY beginPosition = new PositionXY(0, 0);

    ArrayList<Bullet> bulletList = new ArrayList<>();

    @FXML
    void gourdStartGameButton(ActionEvent event) {
        //TODO 这里是从主屏幕进入本地游戏的接口，其他的函数能不动就不动，可以多加函数，如果多加了，尽量写一些标识符。
        System.out.println("startGame");
        ConnectScene.setVisible(false);
        ConnectScene.setDisable(true);
        fightScene.setVisible(true);
        fightScene.setDisable(false);
        mapPane.setVisible(true);
        mapPane.setDisable(false);
        new GameStart(gourdFamily, monsterFamily, this).startGame();
    }

    @FXML
    void monsterStartGameButton(ActionEvent event) {
//        MonsterCamp monsterCamp = new MonsterCamp(this, in, out);
//        monsterCamp.startGame();
    }

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
//        String ipString = ServerIpText.getText();
//        String portString = ServerPortText1.getText();
//
//        String ipPattern = "\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3}";
//        String portPattern = "\\d{4,5}";
//
//        boolean ipMatch = Pattern.matches(ipPattern, ipString);
//        boolean portMatch = Pattern.matches(portPattern, portString);
//
//        if (!ipMatch && !portMatch) {
//            System.out.println("请输入规范的ip和port地址");
//        }
//        try {
//            ConnectScene.setVisible(false);
//            ConnectScene.setDisable(true);
//            fightScene.setVisible(true);
//            fightScene.setDisable(false);
//            new GameClient(ipString, Integer.parseInt(portString), this).run();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @FXML
    void CreateServerMouseClick(MouseEvent event) {
//        String text = ServerPortText2.getText();
//        String portPattern = "\\d{4,5}";
//
//        boolean isMatch = Pattern.matches(portPattern, text);
//        if (!isMatch) {
//            System.out.println("创建服务器端口不符合规范，请重新输入");
//            return;
//        }
//        int port = Integer.parseInt(text);
//        if (port < 5001 || port > 65535) {
//            System.out.println("创建服务器端口不符合规范，请重新输入");
//            return;
//        }
//
//        try {
//            Thread serverThread = new GameServer(port);
//            serverThread.start();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

//    public void initGameSceneController(DataInputStream in, DataOutputStream out, String campType,
//                                        HashMap<Integer, CreatureClass> gourdFamily,
//                                        HashMap<Integer, CreatureClass> monsterFamily) {
//        this.in = in;
//        this.out = out;
//        this.campType = campType;
//        this.gourdFamily = gourdFamily;
//        this.monsterFamily = monsterFamily;
//        msgController = new MsgController(gourdFamily, monsterFamily);
//        notificationMidText = new Text();
//        notificationMidText.setText("等待其他玩家加入");
//        notificationMidText.setFont(Font.font("FangSong", 30));
//        notificationMidText.setTextAlignment(TextAlignment.CENTER);
//        double width = notificationMidText.getBoundsInLocal().getWidth();
//        System.out.println(width);
//        notificationMidText.setLayoutX(600 - width / 2);
//        notificationMidText.setLayoutY(260);
//        mapPane.getChildren().add(notificationMidText);
//    }
//
//    public void gourdStartGame() {
//        mapPane.setVisible(true);
//        mapPane.setDisable(false);
//        waitForAnother();
//    }

//    public void waitForAnother() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (true) {
//                    try {
//                        int msgType = in.readInt();
//                        if (msgType == Msg.PREPARE_GAME_MSG) break;
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//                gourdPrepareForGame();
//            }
//        }).start();
//    }

//    public void gourdPrepareForGame() {
//        for (CreatureClass gourdMember : gourdFamily.values()) {
//            gourdMember.getCreatureImageView().setVisible(true);
//            gourdMember.getCreatureImageView().setDisable(false);
//            gourdMember.getCreatureImageView().setOnMousePressed(new EventHandler<MouseEvent>() {
//                @Override
//                public void handle(MouseEvent event) {
//                    if (selectOwnCampCreature != null) {
//                        selectOwnCampCreature.setCreatureImageView();
//                    }
//                    beginPosition.setPosition(event.getX(), event.getY());
//                    gourdMember.setCreatureImageView();
//                    selectOwnCampCreature = gourdMember;
//                }
//            });
//            gourdMember.getCreatureImageView().setOnMouseDragged(new EventHandler<MouseEvent>() {
//                @Override
//                public void handle(MouseEvent event) {
//                    PositionXY currentPosition = new PositionXY(event.getSceneX(), event.getSceneY());
//                    double deltaX = currentPosition.X - beginPosition.X;
//                    double deltaY = currentPosition.Y - beginPosition.Y;
//                    if(deltaX > Constant.FIGHT_PANE_WIDTH / 2 - Constant.CREATURE_IMAGE_WIDTH)
//                        deltaX = Constant.FIGHT_PANE_WIDTH / 2 - Constant.CREATURE_IMAGE_WIDTH;
//                    deltaX = Math.max(0, deltaX);
//                    deltaY = Math.max(0, deltaY);
//                    if(deltaY > Constant.FIGHT_PANE_HEIGHT - gourdMember.getImageHeight())
//                        deltaY = Constant.FIGHT_PANE_HEIGHT - gourdMember.getImageHeight();
//                    gourdMember.getCreatureImageView().setLayoutX(deltaX);
//                    gourdMember.getCreatureImageView().setLayoutY(deltaY);
//                    if(gourdMember.getCreatureImageView().getParent() == fightScene) {
//                        if(deltaX >= 140) {
//                            gourdMember.setCreatureImagePos(deltaX - 140.0, deltaY - 20.0);
//                            fightScene.getChildren().remove(gourdMember.getCreatureImageView());
//                            mapPane.getChildren().add(gourdMember.getCreatureImageView());
//                        }
//                    }
//                }
//            });
//        }
//        while (true) {
//            try {
//                int msgType = in.readInt();
//                if (msgType == Msg.COUNT_DOWN_MSG) {
//                    msgController.getMsgClass(msgType, in);
//                    notificationMidText.setText(String.valueOf(msgController.getTimeRemaining()));
//                    double width = notificationMidText.getBoundsInLocal().getWidth();
//                    System.out.println(width);
//                    notificationMidText.setLayoutX(600 - width / 2);
//                } else if (msgType == Msg.START_GAME_MSG) {
//                    for (Map.Entry<Integer, CreatureClass> entry : gourdFamily.entrySet()) {
//                        int creatureId = entry.getKey();
//                        CreatureClass gourdMember = entry.getValue();
//                        ImageView tempImageView = gourdMember.getCreatureImageView();
//                        tempImageView.setOnMouseDragged(null);
//                        if(tempImageView.getParent() == fightScene) {
//                            Platform.runLater(new Runnable() {
//                                @Override
//                                public void run() {
//                                    fightScene.getChildren().remove(tempImageView);
//                                    mapPane.getChildren().add(tempImageView);
//                                }
//                            });
//                            gourdMember.setCreatureImagePos(
//                                    randomNum.nextDouble() * (Constant.FIGHT_PANE_WIDTH / 2 - tempImageView.getFitWidth()),
//                                    randomNum.nextDouble() * (Constant.FIGHT_PANE_HEIGHT - 100));
//                        }
//                        new PositionNotifyMsg("Gourd", creatureId,
//                                tempImageView.getLayoutX(), tempImageView.getLayoutY()).sendMsg(out);
//                    }
//                    new NoParseMsg(Msg.FINISH_FLAG_MSG).sendMsg(out);
//                    break;
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        while (true) {
//            try {
//                int msgType = in.readInt();
//                if (msgType == Msg.POSITION_NOTIFY_MSG) {
//                    msgController.getMsgClass(msgType, in);
//                } else if (msgType == Msg.START_GAME_MSG) {
//                    break;
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        gourdStartFight();
//    }

    public void gourdStartFight() {
        //TODO
        while(true) {
//            try {
//                for(GourdClass gourdMember : gourdFamily.values()) {
//                    gourdMember.randomMove();
//                    gourdMember.draw();
//                    Bullet bulletAttack = gourdMember.aiAttack();
//                    if(bulletAttack != null) {
//                        for(Bullet bullet : bulletList) {
//                            if(!bullet.isValid()) {
//                                bullet.changeBullet(bulletAttack);
//                                break;
//                            }
//                        }
//                    }
//                    for (Bullet bullet : bulletList) {
//                        if (bullet.isValid()) {
//                            Collision collision = bullet.move();
//                            bullet.draw();
//                            if (collision != null)
//                                collision.collisionEvent();
//                        }
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }
    }


//    public void monsterStartGame() {
//        mapPane.setVisible(true);
//        mapPane.setDisable(false);
//        mapPane.setLayoutX(20);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                monsterPrepareForGame();
//            }
//        }).start();
//    }
//
//    public void monsterPrepareForGame() {
//        for (CreatureClass monsterMember : monsterFamily.values()) {
//            monsterMember.getCreatureImageView().setVisible(true);
//            monsterMember.getCreatureImageView().setDisable(false);
//            monsterMember.getCreatureImageView().setOnMousePressed(new EventHandler<MouseEvent>() {
//                @Override
//                public void handle(MouseEvent event) {
//                    if (selectOwnCampCreature != null) {
//                        selectOwnCampCreature.setCreatureImageView();
//                    }
//                    beginPosition.setPosition(event.getX(), event.getY());
//                    monsterMember.setCreatureImageView();
//                    selectOwnCampCreature = monsterMember;
//                }
//            });
//            monsterMember.getCreatureImageView().setOnMouseDragged(new EventHandler<MouseEvent>() {
//                @Override
//                public void handle(MouseEvent event) {
//                    PositionXY currentPosition = new PositionXY(event.getSceneX(), event.getSceneY());
//                    double deltaX = currentPosition.X - beginPosition.X;
//                    double deltaY = currentPosition.Y - beginPosition.Y;
//                    if(deltaX > Constant.FIGHT_PANE_WIDTH - Constant.CREATURE_IMAGE_WIDTH)
//                        deltaX = Constant.FIGHT_PANE_WIDTH - Constant.CREATURE_IMAGE_WIDTH;
//                    deltaX = Math.max(Constant.FIGHT_PANE_WIDTH / 2, deltaX);
//                    deltaY = Math.max(0, deltaY);
//                    if(deltaY > Constant.FIGHT_PANE_HEIGHT - monsterMember.getImageHeight())
//                        deltaY = Constant.FIGHT_PANE_HEIGHT - monsterMember.getImageHeight();
//                    monsterMember.getCreatureImageView().setLayoutX(deltaX);
//                    monsterMember.getCreatureImageView().setLayoutY(deltaY);
//                }
//            });
//        }
//        while (true) {
//            try {
//                int msgType = in.readInt();
//                System.out.println(msgType);
//                if (msgType == Msg.COUNT_DOWN_MSG) {
//                    msgController.getMsgClass(msgType, in);
//                    notificationMidText.setText(String.valueOf(msgController.getTimeRemaining()));
//                    double width = notificationMidText.getBoundsInLocal().getWidth();
//                    System.out.println(width);
//                    notificationMidText.setLayoutX(600 - width / 2);
//                } else if (msgType == Msg.START_GAME_MSG) {
//                    for (Map.Entry<Integer, CreatureClass> entry : monsterFamily.entrySet()) {
//                        int creatureId = entry.getKey();
//                        CreatureClass monsterMember = entry.getValue();
//                        ImageView tempImageView = monsterMember.getCreatureImageView();
//                        tempImageView.setOnMouseDragged(null);
//                        if(tempImageView.getParent() == fightScene) {
//                            Platform.runLater(new Runnable() {
//                                @Override
//                                public void run() {
//                                    fightScene.getChildren().remove(tempImageView);
//                                    mapPane.getChildren().add(tempImageView);
//                                }
//                            });
//                            monsterMember.setCreatureImagePos(
//                                    randomNum.nextDouble() * (Constant.FIGHT_PANE_WIDTH / 2 - tempImageView.getFitWidth()) + Constant.FIGHT_PANE_WIDTH / 2,
//                                    randomNum.nextDouble() * (Constant.FIGHT_PANE_HEIGHT - 100));
//                        }
//                        new PositionNotifyMsg("Monster", creatureId,
//                                tempImageView.getLayoutX(), tempImageView.getLayoutY()).sendMsg(out);
//                    }
//                    new NoParseMsg(Msg.FINISH_FLAG_MSG).sendMsg(out);
//                    break;
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        while (true) {
//            try {
//                int msgType = in.readInt();
//                System.out.println(msgType);
//                if (msgType == Msg.POSITION_NOTIFY_MSG) {
//                    msgController.getMsgClass(msgType, in);
//                } else if (msgType == Msg.START_GAME_MSG) {
//                    break;
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        monsterStartFight();
//    }
//
//    public void monsterStartFight() {
//        //TODO monsterStartFight
//        while(true) {
//            try {
//                for(CreatureClass monsterMember : monsterFamily.values()) {
//                    monsterMember.randomMove();
//                    monsterMember.draw();
//                    Bullet bulletAttack = monsterMember.aiAttack();
//                    if(bulletAttack != null) {
//                        for(Bullet bullet : bulletList) {
//                            if(!bullet.isValid()) {
//                                bullet.changeBullet(bulletAttack);
//                                break;
//                            }
//                        }
//                    }
//                    for (Bullet bullet : bulletList) {
//                        if (bullet.isValid()) {
//                            Collision collision = bullet.move();
//                            bullet.draw();
//                            if (collision != null)
//                                collision.collisionEvent();
//                        }
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }

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
}
