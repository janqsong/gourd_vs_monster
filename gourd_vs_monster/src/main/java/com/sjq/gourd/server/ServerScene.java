package com.sjq.gourd.server;

import com.sjq.gourd.constant.CreatureId;
import com.sjq.gourd.constant.ImageUrl;
import com.sjq.gourd.creature.GourdClass;
import com.sjq.gourd.creature.MonsterClass;
import com.sjq.gourd.protocol.Msg;
import com.sjq.gourd.protocol.NoParseMsg;
import com.sjq.gourd.protocol.PositionNotifyMsg;
import javafx.fxml.FXML;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.HashMap;
import java.util.Map;

public class ServerScene {
    private HashMap<Integer, GourdClass> gourdFamily = new HashMap<Integer, GourdClass>();
    private HashMap<Integer, MonsterClass> monsterFamily = new HashMap<Integer, MonsterClass>();
    private DataInputStream inGourd;
    private DataOutputStream outGourd;
    private DataInputStream inMonster;
    private DataOutputStream outMonster;
    private MsgController msgController;

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
        gourdFamily.put(CreatureId.FIRST_GOURD_ID, new GourdClass(CreatureId.FIRST_GOURD_NAME));
        gourdFamily.put(CreatureId.SECOND_GOURD_ID, new GourdClass(CreatureId.SECOND_GOURD_NAME));
        gourdFamily.put(CreatureId.THIRD_GOURD_ID, new GourdClass(CreatureId.THIRD_GOURD_NAME));
        gourdFamily.put(CreatureId.FOURTH_GOURD_ID,new GourdClass(CreatureId.FOURTH_GOURD_NAME));
        gourdFamily.put(CreatureId.FIFTH_GOURD_ID, new GourdClass(CreatureId.FIFTH_GOURD_NAME));
        gourdFamily.put(CreatureId.SIXTH_GOURD_ID, new GourdClass(CreatureId.SIXTH_GOURD_NAME));
        gourdFamily.put(CreatureId.SEVENTH_GOURD_ID, new GourdClass(CreatureId.SEVENTH_GOURD_NAME));
        gourdFamily.put(CreatureId.PANGOLIN_ID, new GourdClass(CreatureId.PANGOLIN_NAME));
        gourdFamily.put(CreatureId.GRANDPA_ID, new GourdClass(CreatureId.GRANDPA_NAME));
        ImageUrl.initImageUrl();
        for(Map.Entry<Integer, String> entry : ImageUrl.gourdImageUrlMap.entrySet()) {
            int key = entry.getKey();
            String value = entry.getValue();
            gourdFamily.get(key).addCreatureImageUrl(value);
            gourdFamily.get(key).setCreatureImageView();
        }

        monsterFamily.put(CreatureId.SNAKE_GOBLIN_ID, new MonsterClass(CreatureId.SNAKE_GOBLIN_NAME));
        monsterFamily.put(CreatureId.SCORPION_GOBLIN_ID, new MonsterClass(CreatureId.SCORPION_GOBLIN_NAME));
        monsterFamily.put(CreatureId.MONSTER1_ID, new MonsterClass(CreatureId.MONSTER1_NAME));
        monsterFamily.put(CreatureId.MONSTER2_ID, new MonsterClass(CreatureId.MONSTER2_NAME));
        monsterFamily.put(CreatureId.MONSTER3_ID, new MonsterClass(CreatureId.MONSTER3_NAME));
        monsterFamily.put(CreatureId.MONSTER4_ID, new MonsterClass(CreatureId.MONSTER4_NAME));
        monsterFamily.put(CreatureId.MONSTER5_ID, new MonsterClass(CreatureId.MONSTER5_NAME));
        monsterFamily.put(CreatureId.MONSTER6_ID, new MonsterClass(CreatureId.MONSTER6_NAME));
        monsterFamily.put(CreatureId.MONSTER7_ID, new MonsterClass(CreatureId.MONSTER7_NAME));
        for(Map.Entry<Integer, String> entry : ImageUrl.monsterImageUrlMap.entrySet()) {
            int key = entry.getKey();
            String value = entry.getValue();
            monsterFamily.get(key).addCreatureImageUrl(value);
            monsterFamily.get(key).setCreatureImageView();
        }
        msgController = new MsgController(gourdFamily, monsterFamily);
    }

    public void setImageViewPos(String creatureType, String creatureId, double layoutX, double layoutY) {
//        if(creatureType.equals("Gourd")) {
//            ImageView tempImageView = gourdFamily.get(creatureId);
//            tempImageView.setLayoutX(layoutX);
//            tempImageView.setLayoutY(layoutY);
//        } else if(creatureType.equals("Monster")) {
//            ImageView tempImageView = monsterFamily.get(creatureId);
//            tempImageView.setLayoutX(layoutX);
//            tempImageView.setLayoutY(layoutY);
//        }
    }

    public void startGame() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        int gourdMsgType = inGourd.readInt();
                        if(gourdMsgType == Msg.FINISH_FLAG_MSG) {
                            gourdFinishFlag = true;
                            break;
                        } else if(gourdMsgType == Msg.POSITION_NOTIFY_MSG) {
                            msgController.getMsgClass(gourdMsgType, inGourd);
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
                while(true) {
                    try {
                        int monsterMsgType = inMonster.readInt();
                        if(monsterMsgType == Msg.FINISH_FLAG_MSG) {
                            monsterFinishFlag = true;
                            break;
                        } else if(monsterMsgType == Msg.POSITION_NOTIFY_MSG) {
                            msgController.getMsgClass(monsterMsgType, inMonster);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        while(true) {
            if(gourdFinishFlag && monsterFinishFlag) {
                for(Map.Entry<Integer, GourdClass> entry : gourdFamily.entrySet()) {
                    int creatureId = entry.getKey();
                    GourdClass gourdMember = entry.getValue();
                    ImageView tempImageView = gourdMember.getCreatureImageView();
                    new PositionNotifyMsg("Gourd", creatureId,
                            tempImageView.getLayoutX(), tempImageView.getLayoutY()).sendMsg(outMonster);
                }
                for(Map.Entry<Integer, MonsterClass> entry : monsterFamily.entrySet()) {
                    int creatureId = entry.getKey();
                    MonsterClass monsterMember = entry.getValue();
                    ImageView tempImageView = monsterMember.getCreatureImageView();
                    new PositionNotifyMsg("Monster", creatureId,
                            tempImageView.getLayoutX(), tempImageView.getLayoutY()).sendMsg(outGourd);
                }
                new NoParseMsg(Msg.START_GAME_MSG).sendMsg(outGourd);
                new NoParseMsg(Msg.START_GAME_MSG).sendMsg(outMonster);
                break;
            }
        }
        System.out.println("开始游戏");
    }
}
