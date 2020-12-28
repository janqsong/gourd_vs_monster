package com.sjq.gourd.server;

import com.sjq.gourd.constant.Constant;
import com.sjq.gourd.creature.Creature;
import com.sjq.gourd.log.MyLogger;
import com.sjq.gourd.protocol.*;
import javafx.scene.image.ImageView;
import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.HashMap;

public class MsgController {
    private HashMap<Integer, Creature> gourdFamily = new HashMap<Integer, Creature>();
    private HashMap<Integer, Creature> monsterFamily = new HashMap<Integer, Creature>();

    private String campType;
    private int creatureId;
    private double layoutX;
    private double layoutY;

    private DataInputStream inGourd;
    private DataOutputStream outGourd;
    private DataInputStream inMonster;
    private DataOutputStream outMonster;

    public MsgController() {

    }

    public MsgController(HashMap<Integer, Creature> gourdFamily,
                         HashMap<Integer, Creature> monsterFamily,
                         DataInputStream inGourd, DataOutputStream outGourd,
                         DataInputStream inMonster, DataOutputStream outMonster) {
        this.gourdFamily = gourdFamily;
        this.monsterFamily = monsterFamily;
        this.inGourd = inGourd;
        this.outGourd = outGourd;
        this.inMonster = inMonster;
        this.outMonster = outMonster;
    }

    public void getMsgClass(int msgType, DataInputStream inputStream) {
        switch (msgType) {
            case Msg.POSITION_NOTIFY_MSG: {
                PositionNotifyMsg positionNotifyMsg = new PositionNotifyMsg();
                positionNotifyMsg.parseMsg(inputStream);
                campType = positionNotifyMsg.getCampType();
                creatureId = positionNotifyMsg.getCreatureId();
                layoutX = positionNotifyMsg.getLayoutX();
                layoutY = positionNotifyMsg.getLayoutY();
                System.out.println("Msg.POSITION_NOTIFY_MSG\n" +
                        "campType: " + campType +
                        "\ncreatureId: " + creatureId +
                        "\nlayoutX: " + layoutX +
                        "\nlayoutY: " + layoutY);
                if (campType.equals(Constant.CampType.GOURD)) {
                    gourdFamily.get(creatureId).setCreatureImagePos(layoutX, layoutY);
                    new PositionNotifyMsg(campType, creatureId, layoutX, layoutY).sendMsg(outMonster);
                } else if (campType.equals(Constant.CampType.MONSTER)) {
                    monsterFamily.get(creatureId).setCreatureImagePos(layoutX, layoutY);
                    new PositionNotifyMsg(campType, creatureId, layoutX, layoutY).sendMsg(outGourd);
                }
                break;
            }
            case Msg.ATTRIBUTE_VALUE_MSG: {
                AttributeValueMsg attributeValueMsg = new AttributeValueMsg();
                attributeValueMsg.parseMsg(inputStream);
                campType = attributeValueMsg.getCampType();
                creatureId = attributeValueMsg.getCreatureId();
                double layoutX = attributeValueMsg.getLayoutX();
                double layoutY = attributeValueMsg.getLayoutY();
                int direction = attributeValueMsg.getDirection();
                double currentHealth = attributeValueMsg.getCurrentHealth();
                double currentMagic = attributeValueMsg.getCurrentMagic();
                double currentAttack = attributeValueMsg.getCurrentAttack();
                double currentDefense = attributeValueMsg.getCurrentDefense();
                double currentAttackSpeed = attributeValueMsg.getCurrentAttackSpeed();
                double currentMoveSpeed = attributeValueMsg.getCurrentMoveSpeed();
                Creature creature = null;
                if (campType.equals(Constant.CampType.GOURD)) {
                    creature = gourdFamily.get(creatureId);
                    new AttributeValueMsg(campType, creatureId, layoutX, layoutY, direction,
                            currentHealth, currentMagic, currentAttack, currentDefense,
                            currentAttackSpeed, currentMoveSpeed).sendMsg(outMonster);
                } else {
                    creature = monsterFamily.get(creatureId);
                    new AttributeValueMsg(campType, creatureId, layoutX, layoutY, direction,
                            currentHealth, currentMagic, currentAttack, currentDefense,
                            currentAttackSpeed, currentMoveSpeed).sendMsg(outGourd);
                }
//                creature.setCreatureImagePos(layoutX, layoutY);
//                creature.setDirection(direction);
//                creature.setCurrentHealth(currentHealth);
//                creature.setCurrentMagic(currentMagic);
//                creature.setCurrentAttack(currentAttack);
//                creature.setCurrentDefense(currentDefense);
//                creature.setCurrentAttackSpeed(currentAttackSpeed);
//                creature.setCurrentMoveSpeed(currentMoveSpeed);
                break;
            }
            case Msg.IMAGE_DIRECTION_MSG: {
                ImageDirectionMsg imageDirectionMsg = new ImageDirectionMsg();
                imageDirectionMsg.parseMsg(inputStream);
                campType = imageDirectionMsg.getCampType();
                creatureId = imageDirectionMsg.getCreatureId();
                int direction = imageDirectionMsg.getDirection();
                Creature creature = null;
                if (campType.equals(Constant.CampType.GOURD)) {
                    creature = gourdFamily.get(creatureId);
                    new ImageDirectionMsg(campType, creatureId, direction).sendMsg(outMonster);
                } else {
                    creature = monsterFamily.get(creatureId);
                    new ImageDirectionMsg(campType, creatureId, direction).sendMsg(outGourd);
                }
                creature.setDirection(direction);
                break;
            }
            default: {
                break;
            }
        }
    }
}
