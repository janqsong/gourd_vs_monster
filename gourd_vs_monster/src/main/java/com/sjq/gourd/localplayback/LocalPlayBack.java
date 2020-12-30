package com.sjq.gourd.localplayback;

import com.sjq.gourd.bullet.Bullet;
import com.sjq.gourd.bullet.BulletState;
import com.sjq.gourd.collision.Collision;
import com.sjq.gourd.constant.Constant;
import com.sjq.gourd.constant.CreatureId;
import com.sjq.gourd.constant.ImageUrl;
import com.sjq.gourd.creature.Creature;
import com.sjq.gourd.creature.CreatureFactory;
import com.sjq.gourd.equipment.Equipment;
import com.sjq.gourd.equipment.EquipmentFactory;
import com.sjq.gourd.equipment.TreasureBag;
import com.sjq.gourd.protocol.BulletBuildMsg;
import com.sjq.gourd.protocol.Msg;
import com.sjq.gourd.protocol.PositionNotifyMsg;
import com.sjq.gourd.server.MsgController;
import com.sjq.gourd.stage.SceneController;
import javafx.application.Platform;
import javafx.scene.image.ImageView;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class LocalPlayBack {
    private HashMap<Integer, Creature> gourdFamily = new HashMap<>();
    private HashMap<Integer, Creature> monsterFamily = new HashMap<>();

    private SceneController sceneController = null;

    private EquipmentFactory equipmentFactory = null;
    private HashMap<Integer, Equipment> equipmentHashMap = new HashMap<>();

    private ArrayList<Bullet> bullets = new ArrayList<>();

    private ObjectInputStream inputStream;

    public LocalPlayBack(File file, SceneController sceneController) {
        try {
            inputStream = new ObjectInputStream(new FileInputStream(file));
            this.sceneController = sceneController;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initGame() {
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
            CreatureFactory gourdFactory = new CreatureFactory(null, Constant.CampType.GOURD, Constant.Direction.RIGHT,
                    gourdImageView);
            CreatureFactory monsterFactory = new CreatureFactory(null, Constant.CampType.MONSTER, Constant.Direction.LEFT,
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playBackGame() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while(true) {
                        int contentType = inputStream.readInt();
                        if(contentType == Msg.FRAME_FINISH_FLAG) break;

                    }

                    for (Creature creature : gourdFamily.values()) {
                        creature.notMyCampUpdate();
                    }

                    for (Creature creature : monsterFamily.values()) {
                        creature.notMyCampUpdate();
                    }

                    Iterator<Bullet> bulletIterator = bullets.iterator();
                    while (bulletIterator.hasNext()) {
                        Bullet bullet = bulletIterator.next();
                        if (bullet.isValid()) {
                            bullet.draw();
                        } else {
                            new Collision(bullet).collisionEvent();
                            bulletIterator.remove();
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    bullet.getCircleShape().setVisible(false);
                                    sceneController.getMapPane().getChildren().remove(bullet.getCircleShape());
                                }
                            });
                        }
                    }

                    for(Equipment equipment : equipmentHashMap.values()) {
                        equipment.draw();
                    }

                    Thread.sleep(Constant.FRAME_TIME);

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }).start();
    }
}
