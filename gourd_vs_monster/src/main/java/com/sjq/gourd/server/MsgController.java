//package com.sjq.gourd.server;
//
//import com.sjq.gourd.creature.GourdClass;
//import com.sjq.gourd.creature.MonsterClass;
//import com.sjq.gourd.protocol.*;
//import javafx.scene.image.ImageView;
//
//import java.io.DataInputStream;
//import java.util.HashMap;
//
//public class MsgController {
//    private HashMap<Integer, GourdClass> gourdFamily = new HashMap<Integer, GourdClass>();
//    private HashMap<Integer, MonsterClass> monsterFamily = new HashMap<Integer, MonsterClass>();
//    private String campType;
//    private int creatureId;
//    private double layoutX;
//    private double layoutY;
//
//    public MsgController(HashMap<Integer, GourdClass> gourdFamily,
//                         HashMap<Integer, MonsterClass> monsterFamily) {
//        this.gourdFamily = gourdFamily;
//        this.monsterFamily = monsterFamily;
//    }
//
//    public void getMsgClass(int msgType, DataInputStream inputStream) {
//        switch (msgType) {
//            case Msg.NOTIFICATION_MSG: {
//                new NotificationMsg().parseMsg(inputStream);
//                break;
//            }
//            case Msg.POSITION_NOTIFY_MSG: {
//                PositionNotifyMsg positionNotifyMsg = new PositionNotifyMsg();
//                positionNotifyMsg.parseMsg(inputStream);
//                campType = positionNotifyMsg.getCampType();
//                creatureId = positionNotifyMsg.getCreatureId();
//                layoutX = positionNotifyMsg.getLayoutX();
//                layoutY = positionNotifyMsg.getLayoutY();
//                if(campType.equals("Gourd")) {
//                    ImageView tempImageView = gourdFamily.get(creatureId).getCreatureImageView();
//                    tempImageView.setLayoutX(layoutX);
//                    tempImageView.setLayoutY(layoutY);
//                } else if(campType.equals("Monster")) {
//                    ImageView tempImageView = monsterFamily.get(creatureId).getCreatureImageView();
//                    tempImageView.setLayoutX(layoutX);
//                    tempImageView.setLayoutY(layoutY);
//                }
//                break;
//            }
//            default: {
//                break;
//            }
//        }
//    }
//}
