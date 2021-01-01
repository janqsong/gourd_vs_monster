package com.ttf.gourd.localplayback;

import com.ttf.gourd.bullet.Bullet;
import com.ttf.gourd.collision.Collision;
import com.ttf.gourd.constant.Constant;
import com.ttf.gourd.constant.CreatureId;
import com.ttf.gourd.constant.ImageUrl;
import com.ttf.gourd.creature.Creature;
import com.ttf.gourd.creature.CreatureFactory;
import com.ttf.gourd.equipment.Equipment;
import com.ttf.gourd.equipment.EquipmentFactory;
import com.ttf.gourd.protocol.Msg;
import com.ttf.gourd.stage.SceneController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class GamePlayBack {
    private HashMap<Integer, Creature> gourdFamily = new HashMap<>();
    private HashMap<Integer, Creature> monsterFamily = new HashMap<>();

    private SceneController sceneController = null;
    private LoadPlayBackFiles loadPlayBackFiles = null;

    private EquipmentFactory equipmentFactory = null;
    private HashMap<Integer, Equipment> equipmentHashMap = new HashMap<>();

    private HashMap<Integer, Bullet> bullets = new HashMap<>();

    private ObjectInputStream inputStream;
    private ContentParse contentParse = null;

    private Creature myCreature = null;
    private final ImageView myCreatureImageView = new ImageView();
    private final Text myCreatureText = new Text();

    private int frameTime = Constant.FRAME_TIME;

    private boolean gameOverFlag = false;
    private boolean stopPlayBack = false;
    private boolean backFlag = false;

    public GamePlayBack(File file, SceneController sceneController, LoadPlayBackFiles loadPlayBackFiles) {
        try {
            inputStream = new ObjectInputStream(new FileInputStream(file));
            this.sceneController = sceneController;
            this.loadPlayBackFiles = loadPlayBackFiles;
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
            sceneController.addImageViewToMapPane(imageView);
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

        sceneController.getFightScene().getChildren().add(myCreatureImageView);
        sceneController.getFightScene().getChildren().add(myCreatureText);
        myCreatureImageView.setVisible(false);
        myCreatureImageView.setDisable(true);
        myCreatureImageView.setPreserveRatio(true);
        myCreatureImageView.setFitWidth(80);
        myCreatureImageView.setLayoutY(20);
        myCreatureText.setVisible(false);
        myCreatureText.setFont(Font.font ("Verdana", 13));
        myCreatureText.setFill(Color.RED);
    }

    public void playBackGame() {
        initGame();
        init();

        new Thread(new Runnable() {
            @Override
            public void run() {
//                System.out.println("begin playback game");
                try {
                    while(true) {
                        int contentType = inputStream.readInt();
                        if(contentType == Msg.FRAME_FINISH_FLAG_MSG) break;
                        else if(contentType == Msg.FINISH_GAME_FLAG_MSG) {
//                            System.out.println("游戏结束");
                            gameOverFlag = true;
                            break;
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
                        Thread.sleep(frameTime);
                        if(stopPlayBack) {
                            continue;
                        }
                        if(backFlag) {
                            inputStream.close();
                            break;
                        }
                        while(true) {
                            int contentType = inputStream.readInt();
                            if(contentType == Msg.FRAME_FINISH_FLAG_MSG) break;
                            else if(contentType == Msg.FINISH_GAME_FLAG_MSG) {
                                gameOverFlag = true;
                                gameOver(Constant.gameOverState.VICTORY);
                                inputStream.close();
                                break;
                            }
                            else contentParse.parsePlayBackContent(inputStream, contentType);
                        }
                        if(gameOverFlag) break;

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
        // 初始化绑定事件函数

        // 初始化界面上的button，主要用来控制回放速度和暂停回放以及返回上一层
        Button halfSpeedButton = new Button();
        halfSpeedButton.setText("0.5x");
        Button normalSpeedButton = new Button();
        normalSpeedButton.setText("1.0x");
        Button doubleSpeedButton = new Button();
        doubleSpeedButton.setText("2.0x");
        Button tripleSpeedButton = new Button();
        tripleSpeedButton.setText("3.0x");
        Button fivefoldSpeedButton = new Button();
        fivefoldSpeedButton.setText("5.0x");
        Button stopButton = new Button();
        stopButton.setText("暂停");
        Button backButton = new Button();
        backButton.setText("返回");

        halfSpeedButton.setLayoutX(50);
        normalSpeedButton.setLayoutX(50);
        doubleSpeedButton.setLayoutX(50);
        tripleSpeedButton.setLayoutX(50);
        fivefoldSpeedButton.setLayoutX(50);
        stopButton.setLayoutX(20);
        backButton.setLayoutX(80);


        halfSpeedButton.setLayoutY(350);
        normalSpeedButton.setLayoutY(380);
        doubleSpeedButton.setLayoutY(410);
        tripleSpeedButton.setLayoutY(440);
        fivefoldSpeedButton.setLayoutY(470);
        stopButton.setLayoutY(500);
        backButton.setLayoutY(500);


        halfSpeedButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                frameTime = Constant.FRAME_TIME * 2;
            }
        });

        normalSpeedButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                frameTime = Constant.FRAME_TIME;
            }
        });

        doubleSpeedButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                frameTime = Constant.FRAME_TIME / 2;
            }
        });

        tripleSpeedButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                frameTime = Constant.FRAME_TIME / 3;
            }
        });

        fivefoldSpeedButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                frameTime = Constant.FRAME_TIME / 5;
            }
        });

        stopButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(stopPlayBack) {
                    stopPlayBack = false;
                    stopButton.setText("暂停");
                }
                else {
                    stopPlayBack = true;
                    stopButton.setText("开始");
                }
            }
        });

        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                backFlag = true;
                sceneController.getFightScene().getChildren().clear();
                sceneController.getFightScene().getChildren().add(sceneController.getMapPane());
                sceneController.getMapPane().getChildren().clear();
                loadPlayBackFiles.backToSelectFile();
            }
        });

        sceneController.getFightScene().getChildren().addAll(halfSpeedButton, normalSpeedButton,
                doubleSpeedButton, tripleSpeedButton, fivefoldSpeedButton, stopButton, backButton);

        sceneController.getFightScene().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(gameOverFlag) {
                    sceneController.getFightScene().getChildren().clear();
                    sceneController.getFightScene().getChildren().add(sceneController.getMapPane());
                    sceneController.getMapPane().getChildren().clear();
                    loadPlayBackFiles.backToSelectFile();
                }
            }
        });

        myCreatureImageView.setLayoutX(5);
        myCreatureText.setLayoutX(5);

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

        sceneController.getFightScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                KeyCode keyCode = event.getCode();
                if(keyCodeCreatureIdMap.get(keyCode) != null) {
                    int creatureId = keyCodeCreatureIdMap.get(keyCode);
                    myCreature = wholeFamily.get(creatureId);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            myCreatureImageView.setLayoutX(Constant.FIGHT_INFO_MARGIN_SIZE / 2 - myCreature.getWIDTH() / 2);
                        }
                    });

                }
            }
        });
    }

    private void gameOver(int gameOverState) {
        ImageView imageView = new ImageView();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                imageView.setImage(ImageUrl.gameOverImageMap.get(gameOverState));
                imageView.setPreserveRatio(true);
                double width = 40;
                double targetWidth = 500;
                imageView.setFitWidth(width);
                imageView.setLayoutX((Constant.FIGHT_PANE_WIDTH - width) / 2);
                imageView.setLayoutY((Constant.FIGHT_PANE_HEIGHT - imageView.getBoundsInLocal().getMaxY()) / 2);
                sceneController.getMapPane().getChildren().add(imageView);
                imageView.setVisible(true);

            }
        });

        double width = 40;
        double targetWidth = 550;
        while (width <= targetWidth) {
            double finalWidth = width;
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    imageView.setFitWidth(finalWidth);
                    imageView.setLayoutX((Constant.FIGHT_PANE_WIDTH - finalWidth) / 2);
                    imageView.setLayoutY((Constant.FIGHT_PANE_HEIGHT - imageView.getBoundsInLocal().getMaxY()) / 2);
                }
            });
            width += 5;
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //todo,在这个地方加上选择重新开始或者返回菜单之类的选择
    }
}
