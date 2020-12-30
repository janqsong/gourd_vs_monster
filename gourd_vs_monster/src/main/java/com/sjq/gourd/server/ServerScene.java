package com.sjq.gourd.server;

import com.sjq.gourd.bullet.Bullet;
import com.sjq.gourd.collision.Collision;
import com.sjq.gourd.constant.Constant;
import com.sjq.gourd.constant.CreatureId;
import com.sjq.gourd.constant.ImageUrl;
import com.sjq.gourd.creature.Creature;
import com.sjq.gourd.creature.CreatureFactory;
import com.sjq.gourd.creature.ImagePosition;
import com.sjq.gourd.equipment.Equipment;
import com.sjq.gourd.equipment.EquipmentFactory;
import com.sjq.gourd.protocol.*;

import javafx.scene.image.ImageView;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ServerScene {
    private HashMap<Integer, Creature> gourdFamily = new HashMap<>();
    private HashMap<Integer, Creature> monsterFamily = new HashMap<>();

    private EquipmentFactory equipmentFactory = null;
    private HashMap<Integer, Equipment> equipmentList = new HashMap<>();
    private int equipmentKey = 0;

    private ObjectOutputStream outFile;

    private ObjectInputStream inGourd;
    private ObjectOutputStream outGourd;
    private ObjectInputStream inMonster;
    private ObjectOutputStream outMonster;

    private MsgController gourdMsgController = null;
    private MsgController monsterMsgController = null;

    boolean gourdFinishFlag = false;
    boolean monsterFinishFlag = false;

    private ConcurrentHashMap<Integer, Bullet> bullets = new ConcurrentHashMap<>();

    public ServerScene(ObjectInputStream inGourd, ObjectOutputStream outGourd,
                       ObjectInputStream inMonster, ObjectOutputStream outMonster) {
        this.inGourd = inGourd;
        this.outGourd = outGourd;
        this.inMonster = inMonster;
        this.outMonster = outMonster;
        initScene();
    }

    public void initScene() {
        ImageUrl.initImageUrl();

        ArrayList<ImageView> imageViews = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            ImageView imageView = new ImageView();
            imageView.setVisible(false);
            imageView.setDisable(true);
            imageViews.add(imageView);
        }
        equipmentFactory = new EquipmentFactory(imageViews);

        ArrayList<ImageView> gourdImageView = new ArrayList<>();
        ArrayList<ImageView> monsterImageView = new ArrayList<>();
        for (int i = 0; i <= 20; i++) {
            ImageView imageView = new ImageView();
            gourdImageView.add(imageView);
        }
        for (int i = 0; i <= 20; i++) {
            ImageView imageView = new ImageView();
            monsterImageView.add(imageView);
        }

        try {
            CreatureFactory gourdFactory = new CreatureFactory(outGourd, Constant.CampType.GOURD, Constant.Direction.RIGHT,
                    gourdImageView);
            CreatureFactory monsterFactory = new CreatureFactory(outMonster, Constant.CampType.MONSTER, Constant.Direction.LEFT,
                    monsterImageView);

            int id = CreatureId.MIN_GOURD_ID;
            while (gourdFactory.hasNext()) {
                Creature creature = gourdFactory.next();
                gourdFamily.put(id++, creature);
            }
            id = CreatureId.MIN_MONSTER_ID;
            while (monsterFactory.hasNext()) {
                Creature creature = monsterFactory.next();
                monsterFamily.put(id++, creature);
            }

            for (Creature creature : gourdFamily.values()) {
                creature.setEnemyFamily(monsterFamily);
                creature.setMyFamily(gourdFamily);
            }
            for (Creature creature : monsterFamily.values()) {
                creature.setEnemyFamily(gourdFamily);
                creature.setMyFamily(monsterFamily);
            }

            gourdMsgController = new MsgController(gourdFamily, monsterFamily);
            monsterMsgController = new MsgController(gourdFamily, monsterFamily);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startGame() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        int gourdMsgType = inGourd.readInt();
                        if (gourdMsgType == Msg.FINISH_FLAG_MSG) {
                            gourdFinishFlag = true;
                            break;
                        } else if (gourdMsgType == Msg.POSITION_NOTIFY_MSG) {
                            gourdMsgController.getMsgClass(gourdMsgType, inGourd);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        int monsterMsgType = inMonster.readInt();
                        if (monsterMsgType == Msg.FINISH_FLAG_MSG) {
                            monsterFinishFlag = true;
                            break;
                        } else if (monsterMsgType == Msg.POSITION_NOTIFY_MSG) {
                            monsterMsgController.getMsgClass(monsterMsgType, inMonster);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        while (true) {
            if (gourdFinishFlag && monsterFinishFlag) {
                for(Map.Entry<Integer, Creature> entry : gourdFamily.entrySet()) {
                    int creatureId = entry.getKey();
                    Creature creature = entry.getValue();
                    ImageView tempImageView = creature.getCreatureImageView();
                    PositionNotifyMsg positionNotifyMsg = new PositionNotifyMsg(Constant.CampType.GOURD, creatureId,
                            tempImageView.getLayoutX(), tempImageView.getLayoutY());
                    positionNotifyMsg.sendMsg(outMonster);
                    positionNotifyMsg.sendMsg(outFile);
                }

                for(Map.Entry<Integer, Creature> entry : monsterFamily.entrySet()) {
                    int creatureId = entry.getKey();
                    Creature creature = entry.getValue();
                    ImageView tempImageView = creature.getCreatureImageView();
                    PositionNotifyMsg positionNotifyMsg = new PositionNotifyMsg(Constant.CampType.MONSTER, creatureId,
                            tempImageView.getLayoutX(), tempImageView.getLayoutY());
                    positionNotifyMsg.sendMsg(outGourd);
                    positionNotifyMsg.sendMsg(outFile);
                }
                new NoParseMsg(Msg.START_GAME_MSG).sendMsg(outGourd);
                new NoParseMsg(Msg.START_GAME_MSG).sendMsg(outMonster);
                break;
            }
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        gourdFinishFlag = false;
        monsterFinishFlag = false;
        startFight();
    }

    public void startFight() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        int gourdMsgType = inGourd.readInt();
                        if (gourdMsgType == Msg.FINISH_FLAG_MSG) {
                            gourdFinishFlag = true;
                        } else {
                            gourdMsgController.getMsgClass(gourdMsgType, inGourd);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        int monsterMsgType = inMonster.readInt();
                        if (monsterMsgType == Msg.FINISH_FLAG_MSG) {
                            monsterFinishFlag = true;
                        } else {
                            monsterMsgController.getMsgClass(monsterMsgType, inMonster);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        while (true) {
            for(Creature gourdCreature : gourdFamily.values()) {
                gourdCreature.sendAllAttribute(outMonster);
            }

            for(Creature monsterCreature : monsterFamily.values()) {
                monsterCreature.sendAllAttribute(outGourd);
            }

            ArrayList<Bullet> closeBullets = gourdMsgController.getCloseBullets();
            closeBullets.addAll(monsterMsgController.getCloseBullets());
            for(Bullet tempBullet : closeBullets) {
                if(tempBullet.isValid()) {
                    Collision collision = tempBullet.update();
//                    collision.collisionEvent();
                    BulletCloseAttackMsg bulletCloseAttackMsg = new BulletCloseAttackMsg(tempBullet.getSourceCreature().getCreatureId(),
                            tempBullet.getTargetCreature().getCreatureId(), tempBullet.getBulletState().ordinal());
                    bulletCloseAttackMsg.sendMsg(outGourd);
                    bulletCloseAttackMsg.sendMsg(outMonster);
                }
            }


            HashMap<Integer, Bullet> buildBullets = gourdMsgController.getBuildBullets();
            if(buildBullets.size() != 0) {
                Iterator<Map.Entry<Integer, Bullet>> bulletMapIterator = buildBullets.entrySet().iterator();
                while(bulletMapIterator.hasNext()) {
                    Map.Entry<Integer, Bullet> bulletEntry = bulletMapIterator.next();
                    int bulletKey = bulletEntry.getKey();
                    Bullet bullet = bulletEntry.getValue();
                    bullets.put(bulletKey, bullet);
                    new BulletBuildMsg(bulletKey,
                                bullet.getSourceCreature().getCampType(), bullet.getSourceCreature().getCreatureId(),
                                bullet.getTargetCreature().getCampType(), bullet.getTargetCreature().getCreatureId(),
                                bullet.getBulletType(), bullet.getBulletState().ordinal()).sendMsg(outMonster);
                }
            }

            buildBullets = monsterMsgController.getBuildBullets();
            if(buildBullets.size() != 0) {
                Iterator<Map.Entry<Integer, Bullet>> bulletMapIterator = buildBullets.entrySet().iterator();
                while(bulletMapIterator.hasNext()) {
                    Map.Entry<Integer, Bullet> bulletEntry = bulletMapIterator.next();
                    int bulletKey = bulletEntry.getKey();
                    Bullet bullet = bulletEntry.getValue();
                    bullets.put(bulletKey, bullet);
                    new BulletBuildMsg(bulletKey,
                            bullet.getSourceCreature().getCampType(), bullet.getSourceCreature().getCreatureId(),
                            bullet.getTargetCreature().getCampType(), bullet.getTargetCreature().getCreatureId(),
                            bullet.getBulletType(), bullet.getBulletState().ordinal()).sendMsg(outGourd);
                }
            }

            ArrayList<CreatureStateGroup> creatureStateList = gourdMsgController.getCreatureStateGroupArrayList();
            for(CreatureStateGroup group : creatureStateList) {
                String campType = group.campType;
                int creatureId = group.creatureId;
                int creatureState = group.creatureState;
                long gapTime = group.gapTime;
                CreatureStateMsg creatureStateMsg = new CreatureStateMsg(campType, creatureId, creatureState, gapTime);
                creatureStateMsg.sendMsg(outGourd);
                creatureStateMsg.sendMsg(outMonster);
            }

            Iterator<Map.Entry<Integer, Bullet>> hashMapIterator = bullets.entrySet().iterator();
            while(hashMapIterator.hasNext()) {
                Map.Entry<Integer, Bullet> mapEntry = hashMapIterator.next();
                int bulletKey = mapEntry.getKey();
                Bullet bullet = mapEntry.getValue();
                if(bullet.isValid()) {
                    Collision collision = bullet.update();
                    if(collision != null) {
                        hashMapIterator.remove();
                        bullet.setValid(false);
                        new BulletDeleteMsg(bulletKey).sendMsg(outGourd);
                        new BulletDeleteMsg(bulletKey).sendMsg(outMonster);
                    } else {
                        new BulletMoveMsg(bulletKey, bullet.getImagePosition().getLayoutX(),
                                bullet.getImagePosition().getLayoutY()).sendMsg(outGourd);
                        new BulletMoveMsg(bulletKey, bullet.getImagePosition().getLayoutX(),
                                bullet.getImagePosition().getLayoutY()).sendMsg(outMonster);
                    }
                }
            }
            if(equipmentFactory.hasNext()) {
                int randNum = equipmentFactory.nextInt();
                ImagePosition imagePosition = equipmentFactory.nextImagePosition();
                Equipment equipment = equipmentFactory.next(randNum, imagePosition);
                equipmentList.put(equipmentKey, equipment);
                EquipmentGenerateMsg equipmentGenerateMsg = new EquipmentGenerateMsg(equipmentKey, randNum,
                        imagePosition.getLayoutX(), imagePosition.getLayoutY());
                equipmentGenerateMsg.sendMsg(outMonster);
                equipmentGenerateMsg.sendMsg(outGourd);
                equipmentKey += 1;
            }

            HashMap<Creature, Integer> requestEquipment = gourdMsgController.getRequestEquipment();
            requestEquipment.putAll(monsterMsgController.getRequestEquipment());
            for(Map.Entry<Creature, Integer> entry : requestEquipment.entrySet()) {
                Creature creature = entry.getKey();
                int equipmentKey = entry.getValue();
                if(equipmentList.get(equipmentKey) != null) {
                    Equipment equipment = equipmentList.get(equipmentKey);
                    creature.pickUpEquipment(equipment);
                    new EquipmentRequestMsg(creature.getCampType(), creature.getCreatureId(), equipmentKey).sendMsg(outGourd);
                    new EquipmentRequestMsg(creature.getCampType(), creature.getCreatureId(), equipmentKey).sendMsg(outMonster);
                    equipmentList.remove(equipmentKey);
                }
            }

            for(Equipment equipment : equipmentList.values()) {
                equipment.draw();
            }

            try {
                Thread.yield();
                Thread.sleep(Constant.FRAME_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
