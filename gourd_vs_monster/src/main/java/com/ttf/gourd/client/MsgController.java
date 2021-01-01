package com.ttf.gourd.client;

import com.ttf.gourd.bullet.Bullet;
import com.ttf.gourd.bullet.BulletState;
import com.ttf.gourd.collision.Collision;
import com.ttf.gourd.constant.Constant;
import com.ttf.gourd.constant.CreatureId;
import com.ttf.gourd.constant.ImageUrl;
import com.ttf.gourd.creature.Creature;
import com.ttf.gourd.creature.CreatureState;
import com.ttf.gourd.creature.CreatureStateWithClock;
import com.ttf.gourd.creature.ImagePosition;
import com.ttf.gourd.equipment.Equipment;
import com.ttf.gourd.equipment.EquipmentFactory;
import com.ttf.gourd.protocol.*;
import com.ttf.gourd.tool.PositionXY;
import com.ttf.gourd.protocol.AttributeValueMsg;
import com.ttf.gourd.protocol.Msg;
import javafx.application.Platform;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.*;

public class MsgController {
    private HashMap<Integer, Creature> gourdFamily = new HashMap<Integer, Creature>();
    private HashMap<Integer, Creature> monsterFamily = new HashMap<Integer, Creature>();
    private HashMap<Integer, Bullet> buildBullets = new HashMap<>();
    private HashMap<Integer, PositionXY> moveBullets = new HashMap<>();
    private ArrayList<Integer> deleteBulletKeys = new ArrayList<>();

    private HashMap<Integer, Equipment> buildEquipment = new HashMap<>();
    EquipmentFactory equipmentFactory = null;

    private HashMap<Creature, Integer> equipmentPickUp = new HashMap<>();
    private HashMap<Creature, Double> sameDestinyHashMap = new HashMap<>();

    private String campType;
    private int timeRemaining;
    private String winCampType;

    public MsgController() {

    }

    public MsgController(HashMap<Integer, Creature> gourdFamily,
                         HashMap<Integer, Creature> monsterFamily) {
        this.gourdFamily = gourdFamily;
        this.monsterFamily = monsterFamily;
    }

    public MsgController(HashMap<Integer, Creature> gourdFamily,
                         HashMap<Integer, Creature> monsterFamily, EquipmentFactory equipmentFactory) {
        this.gourdFamily = gourdFamily;
        this.monsterFamily = monsterFamily;
        this.equipmentFactory = equipmentFactory;
    }

    public String getCampType() {
        return campType;
    }

    public int getTimeRemaining() {
        return timeRemaining;
    }

    public String getWinCampType() {return winCampType;}

    public HashMap<Integer, Bullet> getBullets() {
        HashMap<Integer, Bullet> tempBullet = buildBullets;
        buildBullets = new HashMap<>();
        return tempBullet;
    }

    public HashMap<Integer, PositionXY> getMoveBullets() {
        HashMap<Integer, PositionXY> tempMoveBullets = moveBullets;
        moveBullets = new HashMap<>();
        return tempMoveBullets;
    }

    public ArrayList<Integer> getDeleteBulletKeys() {
        ArrayList<Integer> tempBulletKeys = deleteBulletKeys;
        deleteBulletKeys = new ArrayList<>();
        return tempBulletKeys;
    }

    public HashMap<Integer, Equipment> getBuildEquipment() {
        HashMap<Integer, Equipment> tempEquipment = buildEquipment;
        buildEquipment = new HashMap<>();
        return tempEquipment;
    }

    public HashMap<Creature, Integer> getEquipmentPickUp() {
        HashMap<Creature, Integer> tempRequestEquipment = equipmentPickUp;
        equipmentPickUp = new HashMap<>();
        return tempRequestEquipment;
    }

    public HashMap<Creature, Double> getSameDestinyHashMap() {
        HashMap<Creature, Double> tempSameDestinyHashMap = sameDestinyHashMap;
        sameDestinyHashMap = new HashMap<>();
        return tempSameDestinyHashMap;
    }

    public void getMsgClass(int msgType, ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
        switch (msgType) {
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
                if (campType.equals(Constant.CampType.GOURD))
                    gourdFamily.get(creatureId).setCreatureImagePos(layoutX, layoutY);
                else
                    monsterFamily.get(creatureId).setCreatureImagePos(layoutX, layoutY);
                break;
            }
            case Msg.ATTRIBUTE_VALUE_MSG: {
                AttributeValueMsg attributeValueMsg = new AttributeValueMsg();
                attributeValueMsg.parseMsg(inputStream);
                String campType = attributeValueMsg.getCampType();
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
                buildBullets.put(bulletKey, tempBullet);
                break;
            }
            case Msg.BULLET_MOVE_MSG: {
                BulletMoveMsg bulletMoveMsg = new BulletMoveMsg();
                bulletMoveMsg.parseMsg(inputStream);
                int bulletKey = bulletMoveMsg.getBulletKey();
                double layoutX = bulletMoveMsg.getLayoutX();
                double layoutY = bulletMoveMsg.getLayoutY();
                moveBullets.put(bulletKey, new PositionXY(layoutX, layoutY));
                break;
            }
            case Msg.BULLET_DELETE_MSG: {
                BulletDeleteMsg bulletDeleteMsg = new BulletDeleteMsg();
                bulletDeleteMsg.parseMsg(inputStream);
                int bulletKey = bulletDeleteMsg.getBulletKey();
                deleteBulletKeys.add(bulletKey);
                break;
            }
            case Msg.BULLET_CLOSE_ATTACK_MSG: {
                BulletCloseAttackMsg bulletCloseAttackMsg = new BulletCloseAttackMsg();
                bulletCloseAttackMsg.parseMsg(inputStream);
                int sourceCreatureId = bulletCloseAttackMsg.getSourceCreatureId();
                int targetCreatureId = bulletCloseAttackMsg.getTargetCreatureId();
                BulletState bulletState = BulletState.values()[bulletCloseAttackMsg.getBulletState()];

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Creature sourceCreature = null;
                        Creature targetCreature = null;
                        if(CreatureId.MIN_GOURD_ID <= sourceCreatureId && sourceCreatureId <= CreatureId.MAX_GOURD_ID)
                            sourceCreature = gourdFamily.get(sourceCreatureId);
                        else
                            sourceCreature = monsterFamily.get(sourceCreatureId);
                        if(CreatureId.MIN_GOURD_ID <= targetCreatureId && targetCreatureId <= CreatureId.MAX_GOURD_ID)
                            targetCreature = gourdFamily.get(targetCreatureId);
                        else
                            targetCreature = monsterFamily.get(targetCreatureId);
                        if(sourceCreature != null && targetCreature != null) {
                            targetCreature.getCloseAttackImageView().setImage(ImageUrl.closeAttackImageMap.get(sourceCreature.getClawType()));
                            targetCreature.setLastCloseAttack(System.currentTimeMillis());
                            Bullet bullet = new Bullet(sourceCreature, targetCreature, Constant.CLOSE_BULLET_TYPE, bulletState);
                            Collision collision = new Collision(bullet);
                            collision.collisionEvent();
                        }
                    }
                });
                break;
            }
            case Msg.EQUIPMENT_GENERATE_MSG: {
                EquipmentGenerateMsg equipmentGenerateMsg = new EquipmentGenerateMsg();
                equipmentGenerateMsg.parseMsg(inputStream);
                int equipmentKey = equipmentGenerateMsg.getEquipmentMsg();
                int randNum = equipmentGenerateMsg.getRandNum();
                double layoutX = equipmentGenerateMsg.getLayoutX();
                double layoutY = equipmentGenerateMsg.getLayoutY();
                Equipment equipment = equipmentFactory.next(randNum, new ImagePosition(layoutX, layoutY));
                buildEquipment.put(equipmentKey, equipment);
                break;
            }
            case Msg.EQUIPMENT_REQUEST_MSG: {
                EquipmentRequestMsg equipmentRequestMsg = new EquipmentRequestMsg();
                equipmentRequestMsg.parseMsg(inputStream);
                String campType = equipmentRequestMsg.getCampType();
                int creatureId = equipmentRequestMsg.getCreatureId();
                int equipmentKey = equipmentRequestMsg.getEquipmentKey();
                Creature creature = null;
                if(campType.equals(Constant.CampType.GOURD))
                    creature = gourdFamily.get(creatureId);
                else
                    creature = monsterFamily.get(creatureId);
                equipmentPickUp.put(creature, equipmentKey);
                break;
            }
            case Msg.CREATURE_STATE_MSG: {
                CreatureStateMsg creatureStateMsg = new CreatureStateMsg();
                creatureStateMsg.parseMsg(inputStream);
                String campType = creatureStateMsg.getCampType();
                int creatureId = creatureStateMsg.getCreatureId();
                CreatureState creatureState = CreatureState.values()[creatureStateMsg.getCreatureState()];
                long gapTime = creatureStateMsg.getGapTime();
                Creature creature = null;
                if(campType.equals(Constant.CampType.GOURD))
                    creature = gourdFamily.get(creatureId);
                else
                    creature = monsterFamily.get(creatureId);
                creature.addState(new CreatureStateWithClock(creatureState, gapTime));
                break;
            }
            case Msg.SAME_DESTINY_MSG: {
                SameDestinyMsg sameDestinyMsg = new SameDestinyMsg();
                sameDestinyMsg.parseMsg(inputStream);
                int creatureId = sameDestinyMsg.getCreatureId();
                double deltaHealth = sameDestinyMsg.getDeltaHealth();
                Creature creature = gourdFamily.get(creatureId);
                sameDestinyHashMap.put(creature, deltaHealth);
                break;
            }
            case Msg.FINISH_GAME_FLAG_MSG: {
                FinishGameFlagMsg finishGameFlagMsg = new FinishGameFlagMsg();
                finishGameFlagMsg.parseMsg(inputStream);
                winCampType = finishGameFlagMsg.getWinCampType();
//                System.out.println("winCampType: " + winCampType);
                break;
            }
            default: {
                break;
            }
        }

    }
}
