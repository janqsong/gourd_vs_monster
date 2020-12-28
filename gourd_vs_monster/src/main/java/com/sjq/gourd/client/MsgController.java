package com.sjq.gourd.client;

import com.sjq.gourd.constant.Constant;
import com.sjq.gourd.creature.Creature;
import com.sjq.gourd.log.MyLogger;
import com.sjq.gourd.protocol.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.DataInputStream;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class MsgController {
    private HashMap<Integer, Creature> gourdFamily = new HashMap<Integer, Creature>();
    private HashMap<Integer, Creature> monsterFamily = new HashMap<Integer, Creature>();

    Logger log = Logger.getLogger(MsgController.class.getName());
    private String campType;
    private int timeRemaining;

    public MsgController() {

    }

    public MsgController(HashMap<Integer, Creature> gourdFamily,
                         HashMap<Integer, Creature> monsterFamily) {
        this.gourdFamily = gourdFamily;
        this.monsterFamily = monsterFamily;
    }

    public String getCampType() {
        return campType;
    }
    public int getTimeRemaining() {return timeRemaining;}

    public void getMsgClass(int msgType, DataInputStream inputStream) {
        switch (msgType) {
            case Msg.DISTRIBUTION_OF_CAMP_MSG: {
                DistributionCampMsg distributionCampMsg = new DistributionCampMsg();
                distributionCampMsg.parseMsg(inputStream);
                campType = distributionCampMsg.getCampType();
                log.info("\nMsg.DISTRIBUTION_OF_CAMP_MSG\n" +
                        "campType: " +campType);
                break;
            }
            case Msg.COUNT_DOWN_MSG: {
                CountDownMsg countDownMsg = new CountDownMsg();
                countDownMsg.parseMsg(inputStream);
                timeRemaining = countDownMsg.getTimeRemaining();
                log.info("\nMsg.COUNT_DOWN_MSG\n" +
                        "timeRemaining: " + timeRemaining);
                break;
            }
            case Msg.POSITION_NOTIFY_MSG: {
                PositionNotifyMsg positionNotifyMsg = new PositionNotifyMsg();
                positionNotifyMsg.parseMsg(inputStream);
                String campType = positionNotifyMsg.getCampType();
                int creatureId = positionNotifyMsg.getCreatureId();
                double layoutX = positionNotifyMsg.getLayoutX();
                double layoutY = positionNotifyMsg.getLayoutY();
                System.out.println("Msg.POSITION_NOTIFY_MSG\n" +
                        "campType: " + campType +
                        "\ncreatureId: " +creatureId +
                        "\nlayoutX: " + layoutX +
                        "\nlayoutY: " + layoutY);
                ImageView tempImageView = null;
                if (campType.equals(Constant.CampType.GOURD)) {
                    gourdFamily.get(creatureId).setCreatureImagePos(layoutX, layoutY);
                    tempImageView = gourdFamily.get(creatureId).getCreatureImageView();

                } else if (campType.equals(Constant.CampType.MONSTER)) {
                    monsterFamily.get(creatureId).setCreatureImagePos(layoutX, layoutY);
                    tempImageView = monsterFamily.get(creatureId).getCreatureImageView();
                }
                break;
            }
            case Msg.ATTRIBUTE_VALUE_MSG: {
                AttributeValueMsg attributeValueMsg = new AttributeValueMsg();
                attributeValueMsg.parseMsg(inputStream);
                campType = attributeValueMsg.getCampType();
                int creatureId = attributeValueMsg.getCreatureId();
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
                if (campType.equals(Constant.CampType.GOURD))
                    creature = gourdFamily.get(creatureId);
                else
                    creature = monsterFamily.get(creatureId);
                MyLogger.log.info("Msg.POSITION_NOTIFY_MSG\n" +
                        "campType: " + campType +
                        "\ncreatureId: " +creatureId +
                        "\nlayoutX: " + layoutX +
                        "\nlayoutY: " + layoutY);
                creature.setCreatureImagePos(layoutX, layoutY);
                creature.setDirection(direction);
                creature.setCurrentHealth(currentHealth);
                creature.setCurrentMagic(currentMagic);
                creature.setCurrentAttack(currentAttack);
                creature.setCurrentDefense(currentDefense);
                creature.setCurrentAttackSpeed(currentAttackSpeed);
                creature.setCurrentMoveSpeed(currentMoveSpeed);
                creature.drawBar();
                break;
            }
            case Msg.IMAGE_DIRECTION_MSG: {
                ImageDirectionMsg imageDirectionMsg = new ImageDirectionMsg();
                imageDirectionMsg.parseMsg(inputStream);
                campType = imageDirectionMsg.getCampType();
                int creatureId = imageDirectionMsg.getCreatureId();
                int direction = imageDirectionMsg.getDirection();
                Creature creature = null;
                if (campType.equals(Constant.CampType.GOURD))
                    creature = gourdFamily.get(creatureId);
                else
                    creature = monsterFamily.get(creatureId);
                creature.setDirection(direction);
                break;
            }
            default: {
                break;
            }
        }
    }
}
