package com.sjq.gourd.localtest;

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
import com.sjq.gourd.stage.SceneController;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class GameStart {

    public static Logger logger = Logger.getLogger(GameStart.class.getName());
    private HashMap<Integer, Creature> gourdFamily = new HashMap<Integer, Creature>();
    private HashMap<Integer, Creature> monsterFamily = new HashMap<Integer, Creature>();
    private SceneController sceneController;
    private final Random randomNum = new Random(System.currentTimeMillis());
    //add
    private List<Bullet> bullets = new ArrayList<>();
    private List<Equipment> equipmentList = new ArrayList<>();
    private EquipmentFactory equipmentFactory = null;
    private Creature myCreature = null;
    private Creature enemyCreature = null;

    private boolean flag = false;

    private final ImageView myCreatureImageView = new ImageView();
    private final ImageView enemyCreatureImageView = new ImageView();
    private final Text myCreatureText = new Text();
    private final Text enemyCreatureText = new Text();

    public GameStart(HashMap<Integer, Creature> gourdFamily,
                     HashMap<Integer, Creature> monsterFamily,
                     SceneController sceneController) {
        //TODO 这个类里，随便改，添加任何需要的东西。
        //TODO client和server那两个包应该都用不到，不用改.
        this.gourdFamily = gourdFamily;
        this.monsterFamily = monsterFamily;
        this.sceneController = sceneController;
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
                myCreatureImageView.setLayoutX(5);
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

    public void startGame() {
        try {
            ImageUrl.initImageUrl();
            initGame();
        } catch (Exception e) {
            System.out.println("allWrong");
            e.printStackTrace();
        }
//        monsterStartGame();
    }

    private void initGame() throws Exception {

        //todo 千万不要把初始化的顺序调整了,装备应该是在最下层
        ArrayList<ImageView> imageViews = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            ImageView imageView = new ImageView();
            imageView.setVisible(false);
            imageView.setDisable(true);
            imageViews.add(imageView);
            sceneController.addImageViewToMapPane(imageView);
        }
        equipmentFactory = new EquipmentFactory(imageViews);

        boolean campFlag = randomNum.nextBoolean();
        String camp;
        if (campFlag)
            camp = Constant.CampType.GOURD;
        else
            camp = Constant.CampType.MONSTER;

        int gourdFaceDirection, monsterFaceDirection;
        boolean isFaceRight = randomNum.nextBoolean();
        if (isFaceRight)
            gourdFaceDirection = Constant.Direction.RIGHT;
        else
            gourdFaceDirection = Constant.Direction.LEFT;
        if (gourdFaceDirection == Constant.Direction.RIGHT)
            monsterFaceDirection = Constant.Direction.LEFT;
        else
            monsterFaceDirection = Constant.Direction.RIGHT;

        ArrayList<ImageView> gourdImageView = new ArrayList<>();
        ArrayList<ImageView> monsterImageView = new ArrayList<>();
        //保证自己方的图片在地方上层?
        if (camp == Constant.CampType.GOURD) {
            for (int i = 0; i <= 20; i++) {
                ImageView imageView = new ImageView();
                imageView.setVisible(false);
                imageView.setDisable(true);
                sceneController.getMapPane().getChildren().add(imageView);
                gourdImageView.add(imageView);
            }
            for (int i = 0; i <= 20; i++) {
                ImageView imageView = new ImageView();
                imageView.setVisible(false);
                imageView.setDisable(true);
                sceneController.getMapPane().getChildren().add(imageView);
                monsterImageView.add(imageView);
            }
        } else {
            for (int i = 0; i <= 20; i++) {
                ImageView imageView = new ImageView();
                imageView.setVisible(false);
                imageView.setDisable(true);
                sceneController.getMapPane().getChildren().add(imageView);
                monsterImageView.add(imageView);
            }
            for (int i = 0; i <= 20; i++) {
                ImageView imageView = new ImageView();
                imageView.setVisible(false);
                imageView.setDisable(true);
                sceneController.getMapPane().getChildren().add(imageView);
                gourdImageView.add(imageView);
            }
        }
        CreatureFactory gourdFactory = new CreatureFactory(Constant.CampType.GOURD, gourdFaceDirection,
                null, null, gourdImageView);
        CreatureFactory monsterFactory = new CreatureFactory(Constant.CampType.MONSTER, monsterFaceDirection,
                null, null, monsterImageView);

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

        if (gourdFaceDirection == Constant.Direction.RIGHT) {
            for (Creature creature : gourdFamily.values()) {
                creature.setEnemyFamily(monsterFamily);
                creature.setMyFamily(gourdFamily);
                creature.setCreatureImagePos(
                        randomNum.nextDouble() * (Constant.FIGHT_PANE_WIDTH / 2 - creature.getWIDTH()),
                        randomNum.nextDouble() * (Constant.FIGHT_PANE_HEIGHT - creature.getHEIGHT()));
                sceneController.getMapPane().getChildren().add(creature.getHealthProgressBar());
                sceneController.getMapPane().getChildren().add(creature.getMagicProgressBar());
            }
            for (Creature creature : monsterFamily.values()) {
                creature.setEnemyFamily(gourdFamily);
                creature.setMyFamily(monsterFamily);
                creature.setCreatureImagePos(
                        randomNum.nextDouble() * (Constant.FIGHT_PANE_WIDTH / 2 - creature.getWIDTH()) + Constant.FIGHT_PANE_WIDTH / 2,
                        randomNum.nextDouble() * (Constant.FIGHT_PANE_HEIGHT - creature.getHEIGHT()));
                sceneController.getMapPane().getChildren().add(creature.getHealthProgressBar());
                sceneController.getMapPane().getChildren().add(creature.getMagicProgressBar());
            }
        } else {
            for (Creature creature : gourdFamily.values()) {
                creature.setEnemyFamily(monsterFamily);
                creature.setMyFamily(gourdFamily);
                creature.setCreatureImagePos(
                        randomNum.nextDouble() * (Constant.FIGHT_PANE_WIDTH / 2 - creature.getWIDTH()) + Constant.FIGHT_PANE_WIDTH / 2,
                        randomNum.nextDouble() * (Constant.FIGHT_PANE_HEIGHT - creature.getHEIGHT()));
                sceneController.getMapPane().getChildren().add(creature.getHealthProgressBar());
                sceneController.getMapPane().getChildren().add(creature.getMagicProgressBar());
            }
            for (Creature creature : monsterFamily.values()) {
                creature.setEnemyFamily(gourdFamily);
                creature.setMyFamily(monsterFamily);
                creature.setCreatureImagePos(
                        randomNum.nextDouble() * (Constant.FIGHT_PANE_WIDTH / 2 - creature.getWIDTH()),
                        randomNum.nextDouble() * (Constant.FIGHT_PANE_HEIGHT - creature.getHEIGHT()));
                sceneController.getMapPane().getChildren().add(creature.getHealthProgressBar());
                sceneController.getMapPane().getChildren().add(creature.getMagicProgressBar());
            }
        }

        logger.debug("开始第一场游戏,我方:" + camp);
        if (camp.equals(Constant.CampType.GOURD)) {
            myGameStart(camp, gourdFamily, monsterFamily);
            testWinRate(camp, gourdFamily, monsterFamily);
        } else {
            myGameStart(camp, monsterFamily, gourdFamily);
            testWinRate(camp, monsterFamily, gourdFamily);
        }
    }

    final int[] judgeWin = {3};

    private void testWinRate(String myCamp, HashMap<Integer, Creature> myFamily, HashMap<Integer, Creature> enemyFamily) {
        new Thread(new Runnable() {
            int gourdWin = 0;
            int monsterWin = 0;
            int allWinAndLose = 0;//平局
            int count = 0;

            @Override
            public void run() {
                while (true) {
                    if (judgeWin[0] != 3) {
                        System.out.println("judgeWin:" + judgeWin[0]);
                        count++;
                        if (judgeWin[0] == -1) {
                            monsterWin++;
                            logger.debug("第" + count + "局,本局妖精获胜  " + "妖精总共获胜" + monsterWin + "局" +
                                    "  妖精胜率" + String.format("%.4f", monsterWin / (double) count));
                        } else if (judgeWin[0] == 0) {
                            allWinAndLose++;
                            logger.debug("第" + count + "局,本局平局  " + "总共平局" + allWinAndLose + "局" +
                                    "  双方平局率" + String.format("%.4f", allWinAndLose / (double) count));
                        } else if (judgeWin[0] == 1) {
                            gourdWin++;
                            logger.debug("第" + count + "局,本局葫芦娃获胜  " + "葫芦娃总共获胜" + gourdWin + "局" +
                                    "  葫芦娃胜率" + String.format("%.4f", gourdWin / (double) count));
                        }
                        judgeWin[0] = 3;
                        System.out.println(count + " " + judgeWin[0]);
                        for (Creature creature : myFamily.values()) {
                            creature.setCurrentHealth(creature.getBaseHealth());
                            creature.setCurrentMagic(0);
                            creature.setCurrentDefense(creature.getBaseDefense());
                            creature.setCurrentAttack(creature.getBaseAttack());
                            creature.setCurrentMoveSpeed(creature.getBaseMoveSpeed());
                            creature.setCurrentAttackSpeed(creature.getBaseAttackSpeed());
                        }
                        for (Creature creature : enemyFamily.values()) {
                            creature.setCurrentHealth(creature.getBaseHealth());
                            creature.setCurrentMagic(0);
                            creature.setCurrentDefense(creature.getBaseDefense());
                            creature.setCurrentAttack(creature.getBaseAttack());
                            creature.setCurrentMoveSpeed(creature.getBaseMoveSpeed());
                            creature.setCurrentAttackSpeed(creature.getBaseAttackSpeed());
                        }

                        logger.debug("开始第" + (count + 1) + "场游戏,我方:" + myCamp);
                        myGameStart(myCamp, myFamily, enemyFamily);
                    }
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void myGameStart(String camp, HashMap<Integer, Creature> myFamily, HashMap<Integer, Creature> enemyFamily) {
//        init(camp, myFamily, enemyFamily);//鼠标键盘交互逻辑
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                for (Creature myMember : myFamily.values()) {
                                    ArrayList<Bullet> tempBullet = myMember.update();
                                    if (tempBullet.size() != 0) {
                                        bullets.addAll(tempBullet);
                                        Iterator<Bullet> bulletIterator = tempBullet.listIterator();
                                        while (bulletIterator.hasNext()) {
                                            Bullet bullet = bulletIterator.next();
                                            if (bullet.getBulletType() == Constant.REMOTE_BULLET_TYPE)
                                                Platform.runLater(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        sceneController.getMapPane().getChildren().add(bullet.getCircleShape());
                                                    }
                                                });
                                        }
                                    }
                                }
                                for (Creature enemyMember : enemyFamily.values()) {
                                    ArrayList<Bullet> tempBullet = enemyMember.update();
                                    if (tempBullet.size() != 0) {
                                        bullets.addAll(tempBullet);
                                        Iterator<Bullet> bulletIterator = tempBullet.listIterator();
                                        while (bulletIterator.hasNext()) {
                                            Bullet bullet = bulletIterator.next();
                                            if (bullet.getBulletType() == Constant.REMOTE_BULLET_TYPE)
                                                Platform.runLater(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        sceneController.getMapPane().getChildren().add(bullet.getCircleShape());
                                                    }
                                                });
                                        }
                                    }
                                }
                                Iterator<Bullet> bulletIterator = bullets.listIterator();
                                while (bulletIterator.hasNext()) {
                                    Bullet bullet = bulletIterator.next();
                                    if (bullet.isValid()) {
                                        Collision collision = bullet.update();
                                        if (collision != null) {
                                            collision.collisionEvent();
                                            bulletIterator.remove();
                                            if (bullet.getBulletType() == Constant.REMOTE_BULLET_TYPE) {
                                                Platform.runLater(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        sceneController.getMapPane().getChildren().remove(bullet.getCircleShape());
                                                    }
                                                });
                                            }
                                        }
                                    }
                                }
                                //todo Equipment逻辑
//                                if (equipmentFactory.hasNext()) {
//                                    Equipment equipment = equipmentFactory.next();
//                                    if (equipment != null)
//                                        equipmentList.add(equipment);
//                                }
//                                if (myCreature != null && myCreature.isAlive()) {
//                                    Iterator<Equipment> equipmentIterator = equipmentList.listIterator();
//                                    while (equipmentIterator.hasNext()) {
//                                        Equipment equipment = equipmentIterator.next();
//                                        if (equipment.getImageView().getBoundsInParent().intersects(myCreature.getCreatureImageView().getBoundsInParent())) {
//                                            myCreature.pickUpEquipment(equipment);
//                                            equipmentIterator.remove();
//                                        }
//                                    }
//                                }
//                                for (Equipment equipment : equipmentList) {
//                                    equipment.draw();
//                                }
                            }
                        });
                        //flag=!flag;
                        Thread.yield();
                        Thread.sleep(Constant.FRAME_TIME);
                        int judge = judgeWin(camp, myFamily, enemyFamily);
                        if (judge != 2) {
                            judgeWin[0] = judge;
                            break;
                        }
                    } catch (Exception e) {
                        System.out.println("while(true)出错");
                        e.printStackTrace();
                    } finally {
                        //flag = !flag;
                    }
                }
            }
        }).start();
    }

    private int judgeWin(String camp, HashMap<Integer, Creature> myFamily, HashMap<Integer, Creature> enemyFamily) {
        //todo -1 0 1 2 分别代表妖精胜利,平局,葫芦娃胜利,还没结束
        int flag = 2;
        boolean allMineDie = true, allEnemyDie = true;
        for (Creature creature : myFamily.values())
            if (creature.isAlive()) {
                allMineDie = false;
                break;
            }

        for (Creature creature : enemyFamily.values())
            if (creature.isAlive()) {
                allEnemyDie = false;
                break;
            }

        if (allMineDie && allEnemyDie)
            flag = 0;
        else if (allMineDie && !allEnemyDie)
            flag = -1;
        else if (!allMineDie && allEnemyDie)
            flag = 1;
        if (camp.equals(Constant.CampType.MONSTER) && flag != 2)
            flag = -flag;
        return flag;
    }

    private void init(String camp, HashMap<Integer, Creature> myFamily, HashMap<Integer, Creature> enemyFamily) {
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
                                if (camp.equals(Constant.CampType.GOURD)) {
                                    myCreatureImageView.setImage(ImageUrl.gourdLeftImageMap.get(id));
                                } else
                                    myCreatureImageView.setImage(ImageUrl.monsterLeftImageMap.get(id));
                                myCreatureText.setText(myCreature.showMessage());
                                myCreatureText.setLayoutX(5);
                                myCreatureText.setLayoutY(20 + myCreatureImageView.getBoundsInLocal().getMaxY() + 20);
                                myCreatureText.setVisible(true);
                                if (enemyCreature == null || !enemyCreature.isAlive()) {
                                    enemyCreatureImageView.setVisible(false);
                                    enemyCreatureImageView.setDisable(true);
                                    enemyCreatureText.setVisible(false);
                                } else {
                                    enemyCreatureImageView.setVisible(true);
                                    enemyCreatureImageView.setDisable(false);
                                    enemyCreatureImageView.setLayoutX(5);
                                    enemyCreatureImageView.setLayoutY(20 + myCreatureImageView.getBoundsInLocal().getMaxY()
                                            + 20 + myCreatureText.getBoundsInLocal().getMaxY() + 20);
                                    int id0 = enemyCreature.getCreatureId();
                                    if (camp.equals(Constant.CampType.GOURD)) {
                                        if (myCreature.getCreatureId() == CreatureId.GRANDPA_ID)
                                            enemyCreatureImageView.setImage(ImageUrl.gourdLeftImageMap.get(id0));
                                        else
                                            enemyCreatureImageView.setImage(ImageUrl.monsterLeftImageMap.get(id0));
                                    } else
                                        enemyCreatureImageView.setImage(ImageUrl.gourdLeftImageMap.get(id0));
                                    enemyCreatureText.setText(enemyCreature.showMessage());
                                    enemyCreatureText.setLayoutX(5);
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

        System.out.println(camp);
        int idOffset = 0;
        if (!camp.equals(Constant.CampType.GOURD))
            idOffset = CreatureId.MIN_MONSTER_ID;
//        for (Creature creature : myFamily.values()) {
//            ImageView imageView = creature.getCreatureImageView();
//            imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
//                @Override
//                public void handle(MouseEvent event) {
//                    if (myCreature == null || !myCreature.isAlive()) {
//                        myCreature = creature;
//                        myCreature.flipControlled();
//                    } else if (myCreature != creature) {
//                        myCreature.flipControlled();
//                        myCreature = creature;
//                        myCreature.flipControlled();
//                    } else if (!myCreature.isControlled()) {
//                        myCreature.flipControlled();
//                    }
//                }
//            });
//        }
        for (Creature creature : myFamily.values()) {
            ImageView imageView = creature.getCreatureImageView();
            imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    System.out.println("clickMine");
                    if (enemyCreature != creature && myCreature != null && myCreature.isAlive()
                            && myCreature.getCreatureId() == CreatureId.GRANDPA_ID) {
                        enemyCreature = creature;
                        myCreature.setPlayerAttackTarget(enemyCreature);
                    }
//                    if (myCreature == null || !myCreature.isAlive()) {
//                        myCreature = creature;
//                        myCreature.flipControlled();
//                    } else if (myCreature != creature) {
//                        myCreature.flipControlled();
//                        myCreature = creature;
//                        myCreature.flipControlled();
//                    } else if (!myCreature.isControlled()) {
//                        myCreature.flipControlled();
//                    }
                }
            });
        }
        for (Creature creature : enemyFamily.values()) {
            ImageView imageView = creature.getCreatureImageView();
            imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    System.out.println("clickHis");
                    if (enemyCreature != creature && myCreature != null && myCreature.isAlive()
                            && myCreature.getCreatureId() != CreatureId.GRANDPA_ID) {
                        enemyCreature = creature;
                        myCreature.setPlayerAttackTarget(enemyCreature);
                    }
                }
            });
        }


        sceneController.getMapPane().setFocusTraversable(true);

        final boolean[] isUpPressOn = {false};
        final boolean[] isDownPressOn = {false};
        final boolean[] isLeftPressOn = {false};
        final boolean[] isRightPressOn = {false};
        final int[] lastPressOn = {Constant.Direction.STOP};
        int finalIdOffset = idOffset;
        sceneController.getMapPane().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                KeyCode keyCode = event.getCode();
                if (keyCode == KeyCode.W) {
                    isUpPressOn[0] = true;
                    lastPressOn[0] = Constant.Direction.UP;
                } else if (keyCode == KeyCode.S) {
                    isDownPressOn[0] = true;
                    lastPressOn[0] = Constant.Direction.DOWN;
                } else if (keyCode == KeyCode.A) {
                    isLeftPressOn[0] = true;
                    lastPressOn[0] = Constant.Direction.LEFT;
                } else if (keyCode == KeyCode.D) {
                    isRightPressOn[0] = true;
                    lastPressOn[0] = Constant.Direction.RIGHT;
                } else if (keyCode == KeyCode.Q) {
                    if (myCreature != null && myCreature.isAlive()) {
                        myCreature.setQFlag(true);
                    }
                } else if (keyCode == KeyCode.E) {
                    if (myCreature != null && myCreature.isAlive())
                        myCreature.setEFlag(true);
                } else if (keyCode == KeyCode.R) {
                    if (myCreature != null && myCreature.isAlive())
                        myCreature.setRFlag(true);
                } else if (keyCode == KeyCode.SPACE) {
                    if (myCreature != null && myCreature.isControlled()) {
                        myCreature.flipControlled();
                        myCreature = null;
                    }
                }
                if (myCreature != null) {
                    if (isLeftPressOn[0] || isRightPressOn[0] || isUpPressOn[0] || isDownPressOn[0])
                        myCreature.setDirection(lastPressOn[0]);
                    else
                        myCreature.setDirection(Constant.Direction.STOP);
                }
                if (keyCode.isDigitKey()) {
                    int num = keyCode.ordinal() - 25;
                    System.out.println(num);
                    if (0 <= num && num <= 8) {
                        Creature creature = myFamily.get(finalIdOffset + num);
                        if (myCreature != creature) {
                            if (creature != null && creature.isAlive()) {
                                if (myCreature != null && myCreature.isAlive() && myCreature.isControlled())
                                    myCreature.flipControlled();
                                if (!creature.isControlled())
                                    creature.flipControlled();
                                myCreature = creature;
                                enemyCreature = null;
                            }
                        }
                    }
                }
            }
        });

        sceneController.getMapPane().setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                KeyCode keyCode = event.getCode();
                if (keyCode == KeyCode.W)
                    isUpPressOn[0] = false;
                else if (keyCode == KeyCode.S)
                    isDownPressOn[0] = false;
                else if (keyCode == KeyCode.A)
                    isLeftPressOn[0] = false;
                else if (keyCode == KeyCode.D)
                    isRightPressOn[0] = false;
                if (myCreature != null) {
                    if (isLeftPressOn[0] || isRightPressOn[0] || isUpPressOn[0] || isDownPressOn[0])
                        myCreature.setDirection(lastPressOn[0]);
                    else
                        myCreature.setDirection(Constant.Direction.STOP);
                }
            }
        });

    }
}


//    public void gourdStartGame() {
//        //TODO 葫芦娃的游戏接口一定要在这个线程里写。
//
//        for (CreatureClass gourd : gourdFamily.values()) {
//            ImageView imageView = gourd.getCreatureImageView();
//            //点击选中
//            imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
//                @Override
//                public void handle(MouseEvent event) {
//                    try {
//                        for (CreatureClass gourdClass : gourdFamily.values())
//                            if (gourdClass.isControlled() && gourdClass != gourd) {
//                                gourdClass.flipControlled();
//                                gourdClass.setCreatureImageView();
//                                break;
//                            }
//                        if (!gourd.isControlled()) {
//                            myGourd = gourd;
//                            gourd.flipControlled();
//                            gourd.setCreatureImageView();
//                            gourd.getCreatureImageView().setFocusTraversable(true);
//                        }
//                    } catch (Exception e) {
//                        System.out.println("clickWrong");
//                        e.printStackTrace();
//                    }
//                }
//
//            });
//        }
//
//        for(CreatureClass monster: monsterFamily.values()){
//            ImageView imageView= monster.getCreatureImageView();
//            imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
//                @Override
//                public void handle(MouseEvent event) {
//                    if(myGourd!=null&& myGourd.isAlive()){
//                        myGourd.setAttackTarget(monster);
//                    }
//                }
//            });
//        }
//
//        sceneController.getMapPane().setFocusTraversable(true);
//        sceneController.getMapPane().setOnKeyPressed(new EventHandler<KeyEvent>() {
//            @Override
//            public void handle(KeyEvent event) {
//                try {
//                    if(myGourd!=null&& myGourd.isAlive()){
//                        KeyCode keyCode = event.getCode();
//                        if (keyCode == KeyCode.A || keyCode == KeyCode.KP_LEFT)
//                            myGourd.setDirection(Constant.Direction.LEFT);
//                        else if (keyCode == KeyCode.D || keyCode == KeyCode.KP_RIGHT)
//                            myGourd.setDirection(Constant.Direction.RIGHT);
//                        else if (keyCode == KeyCode.W || keyCode == KeyCode.KP_UP)
//                            myGourd.setDirection(Constant.Direction.UP);
//                        else if (keyCode == KeyCode.S || keyCode == KeyCode.KP_DOWN)
//                            myGourd.setDirection(Constant.Direction.DOWN);
//                        else if (keyCode == KeyCode.R)
//                            ;
//                    }
//                } catch (Exception e) {
//                    System.out.println("pressWrong");
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        sceneController.getMapPane().setOnKeyReleased(new EventHandler<KeyEvent>() {
//            @Override
//            public void handle(KeyEvent event) {
//                try {
//                    if(myGourd!=null){
//                        KeyCode keyCode = event.getCode();
//                        System.out.println(keyCode.getName());
//                        if (keyCode == KeyCode.A || keyCode == KeyCode.KP_LEFT)
//                            myGourd.setDirection(Constant.Direction.STOP);
//                        else if (keyCode == KeyCode.D || keyCode == KeyCode.KP_RIGHT)
//                            myGourd.setDirection(Constant.Direction.STOP);
//                        else if (keyCode == KeyCode.W || keyCode == KeyCode.KP_UP)
//                            myGourd.setDirection(Constant.Direction.STOP);
//                        else if (keyCode == KeyCode.S || keyCode == KeyCode.KP_DOWN)
//                            myGourd.setDirection(Constant.Direction.STOP);
//                    }
//                } catch (Exception e) {
//                    System.out.println("releasedWrong");
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                while (true) {
//                    try {
//                        if (true) {
//                            for (CreatureClass gourdMember : gourdFamily.values()) {
//                                Bullet bullet = gourdMember.update();
//                                if (!pushBackBulletList(bullet))
//                                    throw new RuntimeException("子弹过多");
//                            }
//                            for (CreatureClass monsterMember : monsterFamily.values()) {
//                                Bullet bullet = monsterMember.update();
//                                if (!pushBackBulletList(bullet))
//                                    throw new RuntimeException("子弹过多");
//                            }
//                            for (Bullet bullet : bullets) {
//                                if (bullet.isValid()) {
//                                    Collision collision = bullet.update();
//                                    if (collision != null)
//                                        collision.collisionEvent();
//                                }
//                            }
//                            if (equipmentFactory.hasNext()) {
//                                Equipment equipment = equipmentFactory.next();
//                                if (equipment != null)
//                                    equipmentList.add(equipment);
//                            }
//                            for (CreatureClass creatureClass : gourdFamily.values()) {
//                                if (creatureClass.isControlled()) {
//                                    Iterator<Equipment> equipmentIterator = equipmentList.listIterator();
//                                    while (equipmentIterator.hasNext()) {
//                                        Equipment equipment = equipmentIterator.next();
//                                        if (equipment.getImageView().getBoundsInParent().intersects(creatureClass.getCreatureImageView().getBoundsInParent())) {
//                                            creatureClass.pickUpEquipment(equipment);
//                                            equipmentIterator.remove();
//                                        }
//                                    }
//                                }
//                            }
//                            for (Equipment equipment : equipmentList) {
//                                equipment.draw();
//                            }
//                            //flag=!flag;
//                            Thread.yield();
//                            Thread.sleep(Constant.FRAME_TIME);
//                        }
//                    } catch (Exception e) {
//                        System.out.println("while(true)出错");
//                        e.printStackTrace();
//                    } finally {
//                        //flag = !flag;
//                    }
//                }
//            }
//        }).start();
//    }
//
//    public void monsterStartGame() {
//        //TODO 妖怪的开始游戏接口一定要在这个线程里写。
//        //TODO 两个线程主要是通过全局变量通信。
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                for (MonsterClass monsterMember : monsterFamily.values()) {
//                    ImageView imageView = monsterMember.getCreatureImageView();
//                    //点击选中
//                    imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
//                        @Override
//                        public void handle(MouseEvent event) {
//                            for (MonsterClass monsterClass : monsterFamily.values())
//                                if (monsterClass.isControlled()) {
//                                    monsterClass.flipControlled();
//                                    break;
//                                }
//                            monsterMember.setSelectCreatureImageView();
//                            monsterMember.flipControlled();
//                        }
//                    });
//                    imageView.setOnKeyPressed(new EventHandler<KeyEvent>() {
//                        @Override
//                        public void handle(KeyEvent event) {
//                            KeyCode keyCode = event.getCode();
//                            if (keyCode == KeyCode.A || keyCode == KeyCode.KP_UP)
//                                ;
//                            else if (keyCode == KeyCode.D || keyCode == KeyCode.KP_DOWN)
//                                ;
//                            else if (keyCode == KeyCode.W || keyCode == KeyCode.KP_LEFT)
//                                ;
//                            else if (keyCode == KeyCode.S || keyCode == KeyCode.KP_RIGHT)
//                                ;
//                            else if (keyCode == KeyCode.R)
//                                ;
//                        }
//                    });
//
//            }
//                while (true) {
//                    try {
//                        if (!flag) {
//
//                            System.out.println("MonsterStart");
//                            for (CreatureClass monsterMember : monsterFamily.values()) {
//                                Bullet bullet = monsterMember.update();
//                                if (!pushBackBulletList(bullet))
//                                    throw new RuntimeException("子弹过多");
//                            }
//                            for (Bullet bullet : bullets) {
//                                if (bullet.isValid()) {
//                                    Collision collision = bullet.update();
//                                    if (collision != null)
//                                        collision.collisionEvent();
//                                }
//                            }
//                            System.out.println("endMonster");
//                            Thread.sleep(Constant.FRAME_TIME / 2);
//                            flag = !flag;
//                        }
//                    } catch (Exception e) {
//                        System.out.println("MonsterException");
//                        e.printStackTrace();
//                    } finally {
//                        flag = !flag;
//                    }
//                }
//            }
//        }).start();
//    }
//
//    public void initGame() {
//        //TODO 游戏初始化尽量照着这个样子初始化，不要差太大.
//        JSONObject gourdJSONObject = new JSONObject(getJsonContentText("GourdInformation.json"));
//        JSONArray gourdInfoArray = (JSONArray) gourdJSONObject.get("gourdInfo");
//
//        JSONObject monsterJSONObject = new JSONObject(getJsonContentText("MonsterInformation.json"));
//        JSONArray monsterInfoArray = (JSONArray) monsterJSONObject.get("monsterInfo");
//
//        ImageUrl.initImageUrl();
//        for (int i = 0; i < gourdInfoArray.length(); i++) {
//            JSONObject gourdObject = (JSONObject) gourdInfoArray.get(i);
//            int creatureId = gourdObject.getInt("creatureId");
//            String creatureName = gourdObject.getString("creatureName");
//            double baseHealth = gourdObject.getDouble("baseHealth");
//            double baseMagic = gourdObject.getDouble("baseMagic");
//            double baseAttack = gourdObject.getDouble("baseAttack");
//            double baseDefense = gourdObject.getDouble("baseDefense");
//            double baseAttackSpeed = gourdObject.getDouble("baseAttackSpeed");
//            double baseMoveSpeed = gourdObject.getDouble("baseMoveSpeed");
//            double shootRange = gourdObject.getDouble("shootRange");
//            int faceDirection = gourdObject.getInt("faceDirection");
//            double imageWidth = gourdObject.getDouble("imageWidth");
//            boolean isCloseAttack = gourdObject.getBoolean("isCloseAttack");
//            int clawType = gourdObject.getInt("clawType");
//            Image gourdLeftImage = ImageUrl.gourdLeftImageMap.get(creatureId);
//            Image gourdLeftSelectImage = ImageUrl.gourdLeftSelectImageMap.get(creatureId);
//            Image gourdRightImage = ImageUrl.gourdRightImageMap.get(creatureId);
//            Image gourdRightSelectImage = ImageUrl.gourdRightSelectImageMap.get(creatureId);
//            ImageView imageView = new ImageView();
//            ImageView closeAttackImageView = new ImageView();
//            //sceneController.addImageViewToMapPane(closeAttackImageView);
//            sceneController.addImageViewToMapPane(imageView);
//
//            CreatureClass gourdMember = new CreatureClass(null, null, Constant.CampType.GOURD, creatureId, creatureName,
//                    baseHealth, baseMagic, baseAttack, baseDefense, baseAttackSpeed, baseMoveSpeed, shootRange, faceDirection,
//                    imageWidth, isCloseAttack, clawType, imageView, closeAttackImageView, gourdLeftImage, gourdLeftSelectImage, gourdRightImage, gourdRightSelectImage);
//            gourdMember.setCreatureImageView();
//            ImageView tempImageView = gourdMember.getCreatureImageView();
//            gourdMember.setCreatureImagePos(
//                    randomNum.nextDouble() * (Constant.FIGHT_PANE_WIDTH / 2 - tempImageView.getFitWidth()),
//                    randomNum.nextDouble() * (Constant.FIGHT_PANE_HEIGHT - 100));
//
//            sceneController.addProgressBarToMapPane(gourdMember.getHealthProgressBar());
//            sceneController.addProgressBarToMapPane(gourdMember.getMagicProgressBar());
//
//            gourdMember.getCreatureImageView().setFitWidth(50);
//            gourdFamily.put(creatureId, gourdMember);
//        }
//
//        for (int i = 0; i < monsterInfoArray.length(); i++) {
//            JSONObject monsterObject = (JSONObject) monsterInfoArray.get(i);
//            int creatureId = monsterObject.getInt("creatureId");
//            String creatureName = monsterObject.getString("creatureName");
//            int baseHealth = monsterObject.getInt("baseHealth");
//            int baseMagic = monsterObject.getInt("baseMagic");
//            int baseAttack = monsterObject.getInt("baseAttack");
//            int baseDefense = monsterObject.getInt("baseDefense");
//            double baseAttackSpeed = monsterObject.getDouble("baseAttackSpeed");
//            double baseMoveSpeed = monsterObject.getDouble("baseMoveSpeed");
//            double shootRange = monsterObject.getDouble("shootRange");
//            int faceDirection = monsterObject.getInt("faceDirection");
//            double imageWidth = monsterObject.getDouble("imageWidth");
//            boolean isCloseAttack = monsterObject.getBoolean("isCloseAttack");
//            int clawType = monsterObject.getInt("clawType");
//            Image monsterLeftImage = ImageUrl.monsterLeftImageMap.get(creatureId);
//            Image monsterLeftSelectImage = ImageUrl.monsterLeftSelectImageMap.get(creatureId);
//            Image monsterRightImage = ImageUrl.monsterRightImageMap.get(creatureId);
//            Image monsterRightSelectImage = ImageUrl.monsterRightSelectImageMap.get(creatureId);
//            ImageView imageView = new ImageView();
//            sceneController.addImageViewToMapPane(imageView);
//
//            ImageView closeAttackImageView = new ImageView();
//            //sceneController.addImageViewToMapPane(closeAttackImageView);
//            CreatureClass monsterMember = new CreatureClass(null, null, Constant.CampType.MONSTER, creatureId, creatureName,
//                    baseHealth, baseMagic, baseAttack, baseDefense, baseAttackSpeed, baseMoveSpeed, shootRange, faceDirection,
//                    imageWidth, isCloseAttack, clawType, imageView, closeAttackImageView, monsterLeftImage, monsterLeftSelectImage, monsterRightImage, monsterRightSelectImage);
//            monsterMember.setCreatureImageView();
//            ImageView tempImageView = monsterMember.getCreatureImageView();
//            monsterMember.setCreatureImagePos(
//                    randomNum.nextDouble() * (Constant.FIGHT_PANE_WIDTH / 2 - tempImageView.getFitWidth()) + Constant.FIGHT_PANE_WIDTH / 2,
//                    randomNum.nextDouble() * (Constant.FIGHT_PANE_HEIGHT - 100));
//
//
//            sceneController.addProgressBarToMapPane(monsterMember.getHealthProgressBar());
//            sceneController.addProgressBarToMapPane(monsterMember.getMagicProgressBar());
//
//            monsterFamily.put(creatureId, monsterMember);
//        }
//
//        for (CreatureClass creature : gourdFamily.values()) {
//            sceneController.addImageViewToMapPane(creature.getCloseAttackImageView());
//        }
//        for (CreatureClass creature : monsterFamily.values()) {
//            sceneController.addImageViewToMapPane(creature.getCloseAttackImageView());
//        }
//    }
//
//    public String getJsonContentText(String path) {
//        StringBuilder jsonContent = new StringBuilder();
//        BufferedReader reader = null;
//        try {
//            InputStream in = getClass().getClassLoader().getResourceAsStream(path);
//            assert in != null;
//            reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
//            String tempString = null;
//            while ((tempString = reader.readLine()) != null) {
//                jsonContent.append(tempString);
//            }
//            reader.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return jsonContent.toString();
//    }
//
//    private boolean pushBackBulletList(Bullet bullet) {
//        if (bullet == null)
//            return true;
//        for (Bullet bullet1 : bullets) {
//            if (!bullet1.isValid()) {
//                bullet1.changeBullet(bullet);
//                return true;
//            }
//        }
//        return false;
//    }
//}
