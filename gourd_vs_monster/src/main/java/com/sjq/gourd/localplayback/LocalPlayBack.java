package com.sjq.gourd.localplayback;

import com.sjq.gourd.bullet.Bullet;
import com.sjq.gourd.collision.Collision;
import com.sjq.gourd.constant.Constant;
import com.sjq.gourd.constant.CreatureId;
import com.sjq.gourd.constant.ImageUrl;
import com.sjq.gourd.creature.Creature;
import com.sjq.gourd.creature.CreatureFactory;
import com.sjq.gourd.equipment.Equipment;
import com.sjq.gourd.equipment.EquipmentFactory;
import com.sjq.gourd.protocol.Msg;
import com.sjq.gourd.stage.SceneController;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class LocalPlayBack {
    private HashMap<Integer, Creature> gourdFamily = new HashMap<>();
    private HashMap<Integer, Creature> monsterFamily = new HashMap<>();

    private SceneController sceneController = null;

    private EquipmentFactory equipmentFactory = null;
    private HashMap<Integer, Equipment> equipmentHashMap = new HashMap<>();

    private HashMap<Integer, Bullet> bullets = new HashMap<>();

    private ObjectInputStream inputStream;
    private ContentParse contentParse = null;

    private Creature myCreature = null;
    private Creature enemyCreature = null;
    private final ImageView myCreatureImageView = new ImageView();
    private final ImageView enemyCreatureImageView = new ImageView();
    private final Text myCreatureText = new Text();
    private final Text enemyCreatureText = new Text();

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
            sceneController.getMapPane().getChildren().add(imageView);
            gourdImageView.add(imageView);
        }
        for (int i = 0; i <= 20; i++) {
            ImageView imageView = new ImageView();
            sceneController.getMapPane().getChildren().add(imageView);
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
                sceneController.getMapPane().getChildren().add(creature.getHealthProgressBar());
                sceneController.getMapPane().getChildren().add(creature.getMagicProgressBar());
            }
            for (Creature creature : monsterFamily.values()) {
                creature.setEnemyFamily(gourdFamily);
                creature.setMyFamily(monsterFamily);
                sceneController.getMapPane().getChildren().add(creature.getHealthProgressBar());
                sceneController.getMapPane().getChildren().add(creature.getMagicProgressBar());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        contentParse = new ContentParse(gourdFamily, monsterFamily, bullets,
                equipmentFactory, equipmentHashMap, sceneController);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                sceneController.getFightScene().getChildren().add(myCreatureImageView);
                sceneController.getFightScene().getChildren().add(enemyCreatureImageView);
                sceneController.getFightScene().getChildren().add(myCreatureText);
                sceneController.getFightScene().getChildren().add(enemyCreatureText);
                myCreatureImageView.setVisible(false);
                myCreatureImageView.setDisable(true);
                myCreatureImageView.setPreserveRatio(true);
                myCreatureImageView.setFitWidth(80);
                myCreatureImageView.setLayoutY(20);
                enemyCreatureImageView.setVisible(false);
                enemyCreatureImageView.setDisable(true);
                enemyCreatureImageView.setPreserveRatio(true);
                enemyCreatureImageView.setFitWidth(80);
                myCreatureText.setVisible(false);
                enemyCreatureText.setVisible(false);
            }
        });
    }

    public void playBackGame() {
        initGame();
        init();
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("begin playback game");
                boolean gameOver = false;
                try {
                    while(true) {
                        int contentType = inputStream.readInt();
                        if(contentType == Msg.FRAME_FINISH_FLAG_MSG) break;
                        else if(contentType == Msg.FINISH_GAME_FLAG_MSG) {
                            System.out.println("游戏结束");
                            gameOver = true;
                        }
                        else contentParse.parsePlayBackContent(inputStream, contentType);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                for (Creature creature : gourdFamily.values()) {
                    creature.notMyCampUpdate();
                }

                for (Creature creature : monsterFamily.values()) {
                    creature.notMyCampUpdate();
                }

                for(int i = 0; i < 3; i++) {
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                while(true) {
                    try {
                        Thread.sleep(Constant.FRAME_TIME);
                        while(true) {
                            int contentType = inputStream.readInt();
                            if(contentType == Msg.FRAME_FINISH_FLAG_MSG) break;
                            else if(contentType == Msg.FINISH_GAME_FLAG_MSG) {
                                System.out.println("游戏结束");
                                gameOver = true;
                            }
                            else contentParse.parsePlayBackContent(inputStream, contentType);
                        }
                        if(gameOver) break;

                        for (Creature creature : gourdFamily.values()) {
                            creature.notMyCampUpdate();
                        }

                        for (Creature creature : monsterFamily.values()) {
                            creature.notMyCampUpdate();
                        }

                        Iterator<Bullet> bulletIterator = bullets.values().iterator();
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

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void init() {
        System.out.println("初始化绑定事件");
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                myCreatureImageView.setLayoutX(5);
                enemyCreatureImageView.setLayoutX(5);
                myCreatureText.setLayoutX(5);
                enemyCreatureText.setLayoutX(5);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if (myCreature == null || !myCreature.isAlive()) {
                                myCreatureImageView.setVisible(false);
                                myCreatureImageView.setDisable(true);
                                myCreatureText.setVisible(false);
                                enemyCreatureImageView.setVisible(false);
                                enemyCreatureImageView.setDisable(true);
                                enemyCreatureText.setVisible(false);
                            } else {
                                myCreatureImageView.setVisible(true);
                                myCreatureImageView.setDisable(false);
                                int id = myCreature.getCreatureId();
                                if(CreatureId.MIN_GOURD_ID <= id && id <= CreatureId.MAX_GOURD_ID)
                                    myCreatureImageView.setImage(ImageUrl.gourdLeftImageMap.get(id));
                                else
                                    myCreatureImageView.setImage(ImageUrl.monsterLeftImageMap.get(id));
                                myCreatureText.setText(myCreature.showMessage());
                                myCreatureText.setLayoutY(20 + myCreatureImageView.getBoundsInLocal().getMaxY() + 20);
                                myCreatureText.setVisible(true);
                                if (enemyCreature == null || !enemyCreature.isAlive()) {
                                    enemyCreatureImageView.setVisible(false);
                                    enemyCreatureImageView.setDisable(true);
                                    enemyCreatureText.setVisible(false);
                                } else {
                                    enemyCreatureImageView.setVisible(true);
                                    enemyCreatureImageView.setDisable(false);
                                    enemyCreatureImageView.setLayoutY(20 + myCreatureImageView.getBoundsInLocal().getMaxY()
                                            + 20 + myCreatureText.getBoundsInLocal().getMaxY() + 20);
                                    int id0 = enemyCreature.getCreatureId();
                                    if(CreatureId.MIN_GOURD_ID <= id && id <= CreatureId.MAX_GOURD_ID)
                                        enemyCreatureImageView.setImage(ImageUrl.gourdLeftImageMap.get(id));
                                    else
                                        enemyCreatureImageView.setImage(ImageUrl.monsterLeftImageMap.get(id));
                                    enemyCreatureText.setText(enemyCreature.showMessage());
                                    enemyCreatureText.setLayoutY(20 + myCreatureImageView.getBoundsInLocal().getMaxY()
                                            + 20 + myCreatureText.getBoundsInLocal().getMaxY() + 20 +
                                            enemyCreatureImageView.getBoundsInLocal().getMaxY() + 20);
                                    enemyCreatureText.setVisible(true);
                                }
                            }
                        }
                    });
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        HashMap<Integer, Creature> wholeFamily = gourdFamily;
        wholeFamily.putAll(monsterFamily);

        for (Creature creature : wholeFamily.values()) {
            ImageView imageView =creature.getCreatureImageView();
            imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    myCreature = creature;
                }
            });
        }
        HashMap<KeyCode, Integer> keyCodeCreatureIdMap = new HashMap<>();
        for(int i = CreatureId.MIN_GOURD_ID; i <= CreatureId.MAX_GOURD_ID; i++) {
            keyCodeCreatureIdMap.put(KeyCode.values()[i + 25], i);
        }
        keyCodeCreatureIdMap.put(KeyCode.Q, CreatureId.SNAKE_MONSTER_ID);
        keyCodeCreatureIdMap.put(KeyCode.W, CreatureId.SCORPION_MONSTER_ID);
        keyCodeCreatureIdMap.put(KeyCode.E, CreatureId.MONSTER1_ID);
        keyCodeCreatureIdMap.put(KeyCode.R, CreatureId.MONSTER2_ID);
        keyCodeCreatureIdMap.put(KeyCode.T, CreatureId.MONSTER3_ID);


        sceneController.getMapPane().setFocusTraversable(true);

        sceneController.getMapPane().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                KeyCode keyCode = event.getCode();
                if(keyCodeCreatureIdMap.get(keyCode) != null) {
                    int creatureId = keyCodeCreatureIdMap.get(keyCode);
                    System.out.println("creatureId: " + creatureId);
                    myCreature = wholeFamily.get(creatureId);
                }
            }
        });
    }
}
