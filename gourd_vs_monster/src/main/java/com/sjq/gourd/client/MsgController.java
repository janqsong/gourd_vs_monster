package com.sjq.gourd.client;

import com.sjq.gourd.bullet.Bullet;
import com.sjq.gourd.bullet.BulletState;
import com.sjq.gourd.constant.Constant;
import com.sjq.gourd.creature.Creature;
import com.sjq.gourd.log.MyLogger;
import com.sjq.gourd.protocol.*;
import com.sjq.gourd.stage.SceneController;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.checkerframework.checker.units.qual.A;

import java.io.DataInputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class MsgController {
    private HashMap<Integer, Creature> gourdFamily = new HashMap<Integer, Creature>();
    private HashMap<Integer, Creature> monsterFamily = new HashMap<Integer, Creature>();
    private HashMap<Integer, Bullet> buildBullets = new HashMap<>();

    private ConcurrentHashMap<Integer, Bullet> bulletsHashMap = new ConcurrentHashMap<>();
    private SceneController sceneController = null;

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

    public MsgController(HashMap<Integer, Creature> gourdFamily,
                         HashMap<Integer, Creature> monsterFamily,
                         ConcurrentHashMap<Integer, Bullet> bulletsHashMap) {
        this.gourdFamily = gourdFamily;
        this.monsterFamily = monsterFamily;
        this.bulletsHashMap = bulletsHashMap;
    }

    public String getCampType() {
        return campType;
    }

    public int getTimeRemaining() {
        return timeRemaining;
    }

    public HashMap<Integer, Bullet> getBullets() {
        HashMap<Integer, Bullet> tempBullet = buildBullets;
        buildBullets = new HashMap<>();
        return tempBullet;
    }

    public void getMsgClass(int msgType, DataInputStream inputStream) {
        switch (msgType) {
            case Msg.DISTRIBUTION_OF_CAMP_MSG: {
                DistributionCampMsg distributionCampMsg = new DistributionCampMsg();
                distributionCampMsg.parseMsg(inputStream);
                campType = distributionCampMsg.getCampType();
                log.info("\nMsg.DISTRIBUTION_OF_CAMP_MSG\n" +
                        "campType: " + campType);
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
//                System.out.println("Msg.POSITION_NOTIFY_MSG\n" +
//                        "campType: " + campType +
//                        "\ncreatureId: " +creatureId +
//                        "\nlayoutX: " + layoutX +
//                        "\nlayoutY: " + layoutY);
                ImageView tempImageView = null;
                if (campType.equals(Constant.CampType.GOURD))
                    gourdFamily.get(creatureId).setCreatureImagePos(layoutX, layoutY);
                else
                    monsterFamily.get(creatureId).setCreatureImagePos(layoutX, layoutY);
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
//                MyLogger.log.info("Msg.POSITION_NOTIFY_MSG\n" +
//                        "campType: " + campType +
//                        "\ncreatureId: " +creatureId +
//                        "\nlayoutX: " + layoutX +
//                        "\nlayoutY: " + layoutY);
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
            case Msg.BULLET_BUILD_MSG: {
                BulletBuildMsg bulletBuildMsg = new BulletBuildMsg();
                bulletBuildMsg.parseMsg(inputStream);
                int bulletKey = bulletBuildMsg.getBulletKey();
                String sourceCamp = bulletBuildMsg.getSourceCamp();
                int sourceCreatureId = bulletBuildMsg.getSourceCreatureId();
                String targetCamp = bulletBuildMsg.getTargetCamp();
                int targetCreatureId = bulletBuildMsg.getTargetCreatureId();
                int bulletType = bulletBuildMsg.getBulletType();
                int bulletState = bulletBuildMsg.getBulletState();
                Creature sourceCreature = null;
                Creature targetCreature = null;
                if (sourceCamp.equals(Constant.CampType.GOURD))
                    sourceCreature = gourdFamily.get(sourceCreatureId);
                else
                    sourceCreature = monsterFamily.get(sourceCreatureId);
                if (targetCamp.equals(Constant.CampType.GOURD))
                    targetCreature = gourdFamily.get(targetCreatureId);
                else
                    targetCreature = monsterFamily.get(targetCreatureId);
                Bullet tempBullet = new Bullet(sourceCreature, targetCreature, bulletType, BulletState.values()[bulletState]);
//                System.out.println("receive bulletKey: " + bulletKey + " " +
//                                                "sourceCreatureId: " + tempBullet.getSourceCreature().getCreatureId() + " " +
//                                                "targetCreatureId: " + tempBullet.getTargetCreature().getCreatureId());
                buildBullets.put(bulletKey, tempBullet);
                break;
            }
            case Msg.BULLET_MOVE_MSG: {
//                System.out.println("Msg.BULLET_MOVE_MSG");
                BulletMoveMsg bulletMoveMsg = new BulletMoveMsg();
                bulletMoveMsg.parseMsg(inputStream);
                int bulletKey = bulletMoveMsg.getBulletKey();
                double layoutX = bulletMoveMsg.getLayoutX();
                double layoutY = bulletMoveMsg.getLayoutY();
                boolean valid = bulletMoveMsg.isValid();
                if(bulletsHashMap.get(bulletKey) != null) {
                    bulletsHashMap.get(bulletKey).setImagePosition(layoutX, layoutY);
                    bulletsHashMap.get(bulletKey).setValid(valid);
                }
                break;
            }
            default: {
                break;
            }
        }
    }
}
