package com.sjq.gourd.client;

import com.sjq.gourd.creature.GourdClass;
import com.sjq.gourd.creature.MonsterClass;
import com.sjq.gourd.protocol.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.DataInputStream;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;

public class MsgController {
    private String campType;
    private int timeRemaining;
    private HashMap<Integer, GourdClass> gourdFamily = new HashMap<Integer, GourdClass>();
    private HashMap<Integer, MonsterClass> monsterFamily = new HashMap<Integer, MonsterClass>();

    public MsgController() {

    }

    public MsgController(HashMap<Integer, GourdClass> gourdFamily,
                         HashMap<Integer, MonsterClass> monsterFamily) {
        this.gourdFamily = gourdFamily;
        this.monsterFamily = monsterFamily;
    }

    public String getCampType() {
        return campType;
    }
    public int getTimeRemaining() {return timeRemaining;}

    public void getMsgClass(int msgType, DataInputStream inputStream) {
        switch (msgType) {
            case Msg.NOTIFICATION_MSG: {
                new NotificationMsg().parseMsg(inputStream);
                break;
            }
            case Msg.DISTRIBUTION_OF_CAMP_MSG: {
                DistributionCampMsg distributionCampMsg = new DistributionCampMsg();
                distributionCampMsg.parseMsg(inputStream);
                campType = distributionCampMsg.getCampType();
                break;
            }
            case Msg.COUNT_DOWN_MSG: {
                CountDownMsg countDownMsg = new CountDownMsg();
                countDownMsg.parseMsg(inputStream);
                timeRemaining = countDownMsg.getTimeRemaining();
                break;
            }
            case Msg.POSITION_NOTIFY_MSG: {
                PositionNotifyMsg positionNotifyMsg = new PositionNotifyMsg();
                positionNotifyMsg.parseMsg(inputStream);
                String campType = positionNotifyMsg.getCampType();
                int creatureId = positionNotifyMsg.getCreatureId();
                double layoutX = positionNotifyMsg.getLayoutX();
                double layoutY = positionNotifyMsg.getLayoutY();
                System.out.println(campType);
                System.out.println(creatureId);
                ImageView tempImageView = new ImageView();
                if(campType.equals("Gourd")) {
                    tempImageView = gourdFamily.get(creatureId).getCreatureImageView();
                } else if(campType.equals("Monster")) {
                    tempImageView = monsterFamily.get(creatureId).getCreatureImageView();
                }
                tempImageView.setLayoutX(layoutX);
                tempImageView.setLayoutY(layoutY);
                tempImageView.setVisible(true);
                tempImageView.setDisable(false);
            }
            default: {
                break;
            }
        }
    }
}