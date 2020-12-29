package com.sjq.gourd.server;

import com.sjq.gourd.bullet.Bullet;
import com.sjq.gourd.bullet.BulletState;
import com.sjq.gourd.constant.Constant;
import com.sjq.gourd.creature.Creature;
import com.sjq.gourd.log.MyLogger;
import com.sjq.gourd.protocol.*;
import javafx.scene.image.ImageView;
import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class MsgController {
    private HashMap<Integer, Creature> gourdFamily = new HashMap<Integer, Creature>();
    private HashMap<Integer, Creature> monsterFamily = new HashMap<Integer, Creature>();
    private HashMap<Integer, Bullet> buildBullets = new HashMap<>();
    private ArrayList<Bullet> closeBullets = new ArrayList<>();

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

    public void getMsgClass(int msgType, ObjectInputStream inputStream) {
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
            case Msg.IMAGE_DIRECTION_MSG: {
//                ImageDirectionMsg imageDirectionMsg = new ImageDirectionMsg();
//                imageDirectionMsg.parseMsg(inputStream);
//                campType = imageDirectionMsg.getCampType();
//                creatureId = imageDirectionMsg.getCreatureId();
//                int direction = imageDirectionMsg.getDirection();
//                Creature creature = null;
//                if (campType.equals(Constant.CampType.GOURD)) {
//                    creature = gourdFamily.get(creatureId);
//                    synchronized (outMonster) {
//                        new ImageDirectionMsg(campType, creatureId, direction).sendMsg(outMonster);
//                    }
//                } else {
//                    creature = monsterFamily.get(creatureId);
//                    synchronized (outGourd) {
//                        new ImageDirectionMsg(campType, creatureId, direction).sendMsg(outGourd);
//                    }
//                }
//                creature.setDirection(direction);
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
            default: {
                break;
            }
        }
    }
}
