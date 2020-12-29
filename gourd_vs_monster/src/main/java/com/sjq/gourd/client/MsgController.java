package com.sjq.gourd.client;

import com.sjq.gourd.bullet.Bullet;
import com.sjq.gourd.bullet.BulletState;
import com.sjq.gourd.collision.Collision;
import com.sjq.gourd.constant.Constant;
import com.sjq.gourd.constant.CreatureId;
import com.sjq.gourd.constant.ImageUrl;
import com.sjq.gourd.creature.Creature;
import com.sjq.gourd.creature.CreatureState;
import com.sjq.gourd.creature.CreatureStateWithClock;
import com.sjq.gourd.creature.ImagePosition;
import com.sjq.gourd.equipment.Equipment;
import com.sjq.gourd.equipment.EquipmentFactory;
import com.sjq.gourd.protocol.*;
import com.sjq.gourd.tool.PositionXY;
import javafx.application.Platform;
import javafx.scene.image.ImageView;
import java.io.ObjectInputStream;
import java.util.*;
import java.util.logging.Logger;

public class MsgController {
    private HashMap<Integer, Creature> gourdFamily = new HashMap<Integer, Creature>();
    private HashMap<Integer, Creature> monsterFamily = new HashMap<Integer, Creature>();
    private HashMap<Integer, Bullet> buildBullets = new HashMap<>();
    private HashMap<Integer, PositionXY> moveBullets = new HashMap<>();
    private ArrayList<Integer> deleteBulletKeys = new ArrayList<>();

    private HashMap<Integer, Equipment> buildEquipment = new HashMap<>();
    EquipmentFactory equipmentFactory = null;

    private HashMap<Creature, Integer> equipmentPickUp = new HashMap<>();

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

    public void getMsgClass(int msgType, ObjectInputStream inputStream) {
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
            default: {
                break;
            }
        }

    }
}
