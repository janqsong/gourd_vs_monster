package com.ttf.gourd.server;

import com.ttf.gourd.bullet.Bullet;
import com.ttf.gourd.bullet.BulletState;
import com.ttf.gourd.constant.Constant;
import com.ttf.gourd.creature.Creature;
import com.ttf.gourd.protocol.AttributeValueMsg;
import com.ttf.gourd.protocol.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;

class CreatureStateGroup {
    public String campType;
    public int creatureId;
    public int creatureState;
    public long gapTime;
}

public class MsgController {
    private HashMap<Integer, Creature> gourdFamily = new HashMap<Integer, Creature>();
    private HashMap<Integer, Creature> monsterFamily = new HashMap<Integer, Creature>();
    private HashMap<Integer, Bullet> buildBullets = new HashMap<>();
    private ArrayList<Bullet> closeBullets = new ArrayList<>();

    private HashMap<Creature, Integer> requestEquipment = new HashMap<>();

    private ArrayList<CreatureStateGroup> creatureStateGroupArrayList = new ArrayList<>();

    private HashMap<Creature, Double> sameDestinyHashMap = new HashMap<>();

    private String campType;
    private int creatureId;
    private double layoutX;
    private double layoutY;


    public MsgController() {

    }

    public MsgController(HashMap<Integer, Creature> gourdFamily,
                         HashMap<Integer, Creature> monsterFamily) {
        this.gourdFamily = gourdFamily;
        this.monsterFamily = monsterFamily;
    }

    public HashMap<Integer, Bullet> getBuildBullets() {
        HashMap<Integer, Bullet> tempBuildBullets = buildBullets;
        buildBullets = new HashMap<>();
        return tempBuildBullets;
    }

    public ArrayList<Bullet> getCloseBullets() {
        ArrayList<Bullet> tempCloseBullets = closeBullets;
        closeBullets = new ArrayList<>();
        return tempCloseBullets;
    }

    public HashMap<Creature, Integer> getRequestEquipment() {
        HashMap<Creature, Integer> tempRequestEquipment = requestEquipment;
        requestEquipment = new HashMap<>();
        return tempRequestEquipment;
    }

    public ArrayList<CreatureStateGroup> getCreatureStateGroupArrayList() {
        ArrayList<CreatureStateGroup> tempCreatureState = creatureStateGroupArrayList;
        creatureStateGroupArrayList = new ArrayList<>();
        return tempCreatureState;
    }

    public HashMap<Creature, Double> getSameDestinyHashMap() {
        HashMap<Creature, Double> tempSameDestinyHashMap = sameDestinyHashMap;
        sameDestinyHashMap = new HashMap<>();
        return tempSameDestinyHashMap;
    }

    public void getMsgClass(int msgType, ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
        switch (msgType) {
            case Msg.POSITION_NOTIFY_MSG: {
                PositionNotifyMsg positionNotifyMsg = new PositionNotifyMsg();
                positionNotifyMsg.parseMsg(inputStream);
                campType = positionNotifyMsg.getCampType();
                creatureId = positionNotifyMsg.getCreatureId();
                layoutX = positionNotifyMsg.getLayoutX();
                layoutY = positionNotifyMsg.getLayoutY();
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
                if(bulletType == Constant.REMOTE_BULLET_TYPE)
                    buildBullets.put(bulletKey, tempBullet);
                else
                    closeBullets.add(tempBullet);
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
                requestEquipment.put(creature, equipmentKey);
                break;
            }
            case Msg.CREATURE_STATE_MSG: {
                CreatureStateMsg creatureStateMsg = new CreatureStateMsg();
                creatureStateMsg.parseMsg(inputStream);
                String campType = creatureStateMsg.getCampType();
                int creatureId = creatureStateMsg.getCreatureId();
                int creatureState = creatureStateMsg.getCreatureState();
                long gapTime = creatureStateMsg.getGapTime();
                CreatureStateGroup creatureStateGroup = new CreatureStateGroup();
                creatureStateGroup.campType = campType;
                creatureStateGroup.creatureId = creatureId;
                creatureStateGroup.creatureState = creatureState;
                creatureStateGroup.gapTime = gapTime;
                creatureStateGroupArrayList.add(creatureStateGroup);
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
            default: {
                break;
            }
        }
    }
}
