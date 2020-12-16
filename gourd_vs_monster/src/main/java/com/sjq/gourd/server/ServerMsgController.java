package com.sjq.gourd.server;

import com.sjq.gourd.creature.GourdClass;
import com.sjq.gourd.creature.MonsterClass;
import com.sjq.gourd.protocol.*;
import javafx.scene.image.ImageView;

import java.io.DataInputStream;
import java.util.HashMap;

public class ServerMsgController {
    private HashMap<Integer, GourdClass> gourdFamily = new HashMap<Integer, GourdClass>();
    private HashMap<Integer, MonsterClass> monsterFamily = new HashMap<Integer, MonsterClass>();

    public ServerMsgController(HashMap<Integer, GourdClass> gourdFamily,
                               HashMap<Integer, MonsterClass> monsterFamily) {
        this.gourdFamily = gourdFamily;
        this.monsterFamily = monsterFamily;
    }

    public void getMsgClass(int msgType, DataInputStream inputStream) {
        switch (msgType) {
            case Msg.NOTIFICATION_MSG: {
                new NotificationMsg().parseMsg(inputStream);
                break;
            }
            case Msg.POSITION_NOTIFY_MSG: {
                PositionNotifyMsg positionNotifyMsg = new PositionNotifyMsg();
                positionNotifyMsg.parseMsg(inputStream);
                String campType = positionNotifyMsg.getCampType();
                int creatureId = positionNotifyMsg.getCreatureId();
                double layoutX = positionNotifyMsg.getLayoutX();
                double layoutY = positionNotifyMsg.getLayoutY();
                if(campType.equals("Gourd")) {
                    ImageView tempImageView = gourdFamily.get(creatureId).getCreatureImageView();
                    tempImageView.setLayoutX(layoutX);
                    tempImageView.setLayoutY(layoutY);
                } else if(campType.equals("Monster")) {
                    System.out.println("=======Server Msg=========");
                    System.out.println(creatureId + " campType: " + campType);
                    System.out.println(creatureId + " creatureId: " + creatureId);
                    System.out.println(creatureId + " layoutX: " + layoutX);
                    System.out.println(creatureId + " layoutY: " + layoutY);
                    System.out.println("======================");
                    ImageView tempImageView = monsterFamily.get(creatureId).getCreatureImageView();
                    tempImageView.setLayoutX(layoutX);
                    tempImageView.setLayoutY(layoutY);
                }
                break;
            }
            default: {
                break;
            }
        }
    }
}
