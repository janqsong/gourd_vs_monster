package com.sjq.gourd.server;

import com.sjq.gourd.constant.Constant;
import com.sjq.gourd.constant.CreatureId;
import com.sjq.gourd.constant.ImageUrl;
import com.sjq.gourd.creature.Creature;
import com.sjq.gourd.creature.CreatureFactory;
import com.sjq.gourd.log.MyLogger;
import com.sjq.gourd.protocol.Msg;
import com.sjq.gourd.protocol.NoParseMsg;
import com.sjq.gourd.protocol.PositionNotifyMsg;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ServerScene {
    private HashMap<Integer, Creature> gourdFamily = new HashMap<Integer, Creature>();
    private HashMap<Integer, Creature> monsterFamily = new HashMap<Integer, Creature>();
    private DataInputStream inGourd;
    private DataOutputStream outGourd;
    private DataInputStream inMonster;
    private DataOutputStream outMonster;

    private MsgController gourdMsgController = null;
    private MsgController monsterMsgController = null;

    boolean gourdFinishFlag = false;
    boolean monsterFinishFlag = false;

    public ServerScene(DataInputStream inGourd, DataOutputStream outGourd,
                       DataInputStream inMonster, DataOutputStream outMonster) {
        this.inGourd = inGourd;
        this.outGourd = outGourd;
        this.inMonster = inMonster;
        this.outMonster = outMonster;
        initScene();
    }

    public void initScene() {
        ImageUrl.initImageUrl();
        ArrayList<ImageView> gourdImageView = new ArrayList<>();
        ArrayList<ImageView> monsterImageView = new ArrayList<>();
        for (int i = 0; i <= 20; i++) {
            ImageView imageView = new ImageView();
            gourdImageView.add(imageView);
        }
        for (int i = 0; i <= 20; i++) {
            ImageView imageView = new ImageView();
            monsterImageView.add(imageView);
        }

        try {
            CreatureFactory gourdFactory = new CreatureFactory(Constant.CampType.GOURD, Constant.Direction.RIGHT,
                    null, null, gourdImageView);
            CreatureFactory monsterFactory = new CreatureFactory(Constant.CampType.MONSTER, Constant.Direction.LEFT,
                    null, null, monsterImageView);

            int id = CreatureId.MIN_GOURD_ID;
            while (gourdFactory.hasNext()) {
                Creature creature = gourdFactory.next();
                gourdFamily.put(id++, creature);
            }
            id = CreatureId.MIN_MONSTER_ID;
            while (monsterFactory.hasNext()) {
                Creature creature = monsterFactory.next();
                monsterFamily.put(id++, creature);
            }

            for (Creature creature : gourdFamily.values()) {
                creature.setEnemyFamily(monsterFamily);
                creature.setMyFamily(gourdFamily);
            }
            for (Creature creature : monsterFamily.values()) {
                creature.setEnemyFamily(gourdFamily);
                creature.setMyFamily(monsterFamily);
            }

            gourdMsgController = new MsgController(gourdFamily, monsterFamily,
                    inGourd, outGourd, inMonster, outMonster);
            monsterMsgController = new MsgController(gourdFamily, monsterFamily,
                    inGourd, outGourd, inMonster, outMonster);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startGame() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        int gourdMsgType = inGourd.readInt();
                        if (gourdMsgType == Msg.FINISH_FLAG_MSG) {
                            gourdFinishFlag = true;
                            break;
                        } else if (gourdMsgType == Msg.POSITION_NOTIFY_MSG) {
                            gourdMsgController.getMsgClass(gourdMsgType, inGourd);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        int monsterMsgType = inMonster.readInt();
                        if (monsterMsgType == Msg.FINISH_FLAG_MSG) {
                            monsterFinishFlag = true;
                            break;
                        } else if (monsterMsgType == Msg.POSITION_NOTIFY_MSG) {
                            monsterMsgController.getMsgClass(monsterMsgType, inMonster);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        while (true) {
            if (gourdFinishFlag && monsterFinishFlag) {
                for(Map.Entry<Integer, Creature> entry : gourdFamily.entrySet()) {
                    int creatureId = entry.getKey();
                    Creature creature = entry.getValue();
                    ImageView tempImageView = creature.getCreatureImageView();
                    MyLogger.log.info("CreatureId: " + creatureId +
                            "\nlayoutX: " + tempImageView.getLayoutX() +
                            "layoutY: " + tempImageView.getLayoutY());
                    new PositionNotifyMsg(Constant.CampType.GOURD, creatureId,
                            tempImageView.getLayoutX(), tempImageView.getLayoutY()).sendMsg(outMonster);
                }

                for(Map.Entry<Integer, Creature> entry : monsterFamily.entrySet()) {
                    int creatureId = entry.getKey();
                    Creature creature = entry.getValue();
                    ImageView tempImageView = creature.getCreatureImageView();
                    MyLogger.log.info("CreatureId: " + creatureId +
                            "\nlayoutX: " + tempImageView.getLayoutX() +
                            " layoutY: " + tempImageView.getLayoutY());
                    new PositionNotifyMsg(Constant.CampType.MONSTER, creatureId,
                            tempImageView.getLayoutX(), tempImageView.getLayoutY()).sendMsg(outGourd);
                }
                new NoParseMsg(Msg.START_GAME_MSG).sendMsg(outGourd);
                new NoParseMsg(Msg.START_GAME_MSG).sendMsg(outMonster);
                break;
            }
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        gourdFinishFlag = false;
        monsterFinishFlag = false;
        startFight();
    }

    public void startFight() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        int gourdMsgType = inGourd.readInt();
                        if (gourdMsgType == Msg.FINISH_FLAG_MSG) {
                            gourdFinishFlag = true;
                        } else {
                            gourdMsgController.getMsgClass(gourdMsgType, inGourd);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        int monsterMsgType = inMonster.readInt();
                        if (monsterMsgType == Msg.FINISH_FLAG_MSG) {
                            monsterFinishFlag = true;
                        } else {
                            monsterMsgController.getMsgClass(monsterMsgType, inMonster);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        while (true) {
            if(gourdFinishFlag && monsterFinishFlag) {
                // TODO: 发送信息
                gourdFinishFlag = false;
                monsterFinishFlag = false;
            }
            try {
                Thread.yield();
                Thread.sleep(Constant.FRAME_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
