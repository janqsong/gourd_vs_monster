package com.sjq.gourd.client;

import com.sjq.gourd.bullet.Bullet;
import com.sjq.gourd.collision.Collision;
import com.sjq.gourd.constant.Constant;
import com.sjq.gourd.constant.CreatureId;
import com.sjq.gourd.creature.Creature;
import com.sjq.gourd.equipment.Equipment;
import com.sjq.gourd.equipment.EquipmentFactory;
import com.sjq.gourd.log.MyLogger;
import com.sjq.gourd.protocol.BulletBuildMsg;
import com.sjq.gourd.protocol.Msg;
import com.sjq.gourd.stage.SceneController;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class GameStartFight {
    private SceneController sceneController = null;
    private ObjectInputStream in = null;
    private ObjectOutputStream out = null;
    private String campType = "";
    private HashMap<Integer, Creature> myFamily = null;
    private HashMap<Integer, Creature> enemyFamily = null;

    private Creature myCreature = null;
    private Creature enemyCreature = null;

    private ConcurrentHashMap<Integer, Bullet> bullets = new ConcurrentHashMap<>();
    private List<Equipment> equipmentList = new ArrayList<>();

    private EquipmentFactory equipmentFactory = null;

    private MsgController msgController;

    private boolean updateFlag = false;


    public GameStartFight(String campType, SceneController sceneController,
                          ObjectInputStream in, ObjectOutputStream out,
                          HashMap<Integer, Creature> myFamily, HashMap<Integer, Creature> enemyFamily,
                          EquipmentFactory equipmentFactory) {
        this.campType = campType;
        this.sceneController = sceneController;
        this.in = in;
        this.out = out;
        this.myFamily = myFamily;
        this.enemyFamily = enemyFamily;
        this.equipmentFactory = equipmentFactory;
        if (campType.equals(Constant.CampType.GOURD))
            msgController = new MsgController(myFamily, enemyFamily, bullets);
        else
            msgController = new MsgController(enemyFamily, myFamily, bullets);
    }

    public void initGame() {
        int idOffset = CreatureId.MIN_GOURD_ID;
        if (this.campType.equals(Constant.CampType.MONSTER))
            idOffset = CreatureId.MIN_MONSTER_ID;
        for (Creature creature : myFamily.values()) {
            ImageView imageView = creature.getCreatureImageView();
            imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (enemyCreature != creature && myCreature != null
                            && myCreature.isAlive() && myCreature.getCreatureId() == CreatureId.GRANDPA_ID) {
                        enemyCreature = creature;
                        myCreature.setPlayerAttackTarget(enemyCreature);
                    }
                    if (myCreature == null || !myCreature.isAlive()) {
                        myCreature = creature;
                        myCreature.flipControlled();
                    } else if (myCreature != creature) {
                        myCreature.flipControlled();
                        myCreature = creature;
                        myCreature.flipControlled();
                    } else if (!myCreature.isControlled()) {
                        myCreature.flipControlled();
                    }
                }
            });
        }
        for (Creature creature : enemyFamily.values()) {
            ImageView imageView = creature.getCreatureImageView();
            imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (enemyCreature != creature) {
                        enemyCreature = creature;
                        if (myCreature != null && myCreature.isAlive())
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

    public void start() {
        initGame();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        int msgType = in.readInt();
//                        System.out.println("msgType: " + msgType);
                        if (msgType == Msg.UPDATE_FLAG_MSG) {
                            updateFlag = true;
                        } else {
                            msgController.getMsgClass(msgType, in);
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
                int bulletKey = 0;
                if (campType.equals(Constant.CampType.MONSTER))
                    bulletKey = 1;
                while (true) {
                    try {
                        for (Creature myMember : myFamily.values()) {
                            ArrayList<Bullet> tempBullet = myMember.update();
                            if (tempBullet.size() != 0) {
                                Iterator<Bullet> bulletIterator = tempBullet.listIterator();
                                while (bulletIterator.hasNext()) {
                                    Bullet bullet = bulletIterator.next();
                                    if(bullet.getBulletType() == Constant.REMOTE_BULLET_TYPE) {
                                        bullets.put(bulletKey, bullet);
//                                        System.out.println("send bulletKey: " + bulletKey + " " +
//                                                "sourceCreatureId: " + bullet.getSourceCreature().getCreatureId() + " " +
//                                                "targetCreatureId: " + bullet.getTargetCreature().getCreatureId());
                                        new BulletBuildMsg(campType, Constant.CampType.SERVER, bulletKey,
                                                bullet.getSourceCreature().getCampType(), bullet.getSourceCreature().getCreatureId(),
                                                bullet.getTargetCreature().getCampType(), bullet.getTargetCreature().getCreatureId(),
                                                bullet.getBulletType(), bullet.getBulletState().ordinal()).sendMsg(out);
                                        bulletKey += 2;
                                        Platform.runLater(new Runnable() {
                                            @Override
                                            public void run() {
                                                sceneController.getMapPane().getChildren().add(bullet.getCircleShape());
                                            }
                                        });
                                    } else {

                                    }
                                }
                            }
                        }

                        HashMap<Integer, Bullet> buildBullets = msgController.getBullets();
                        if (buildBullets.size() != 0) {
                            Iterator<Map.Entry<Integer, Bullet>> bulletMapIterator = buildBullets.entrySet().iterator();
                            while (bulletMapIterator.hasNext()) {
                                Map.Entry<Integer, Bullet> bulletEntry = bulletMapIterator.next();
                                int key = bulletEntry.getKey();
                                Bullet bullet = bulletEntry.getValue();
                                bullets.put(key, bullet);
                                if (bullet.getBulletType() == Constant.REMOTE_BULLET_TYPE) {
                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            sceneController.getMapPane().getChildren().add(bullet.getCircleShape());
                                        }
                                    });
                                }
                            }
                        }

                        Iterator<Bullet> bulletIterator = bullets.values().iterator();
                        while (bulletIterator.hasNext()) {
                            Bullet bullet = bulletIterator.next();
                            if (bullet.isValid()) {
                                if(bullet.getCircleShape() != null)
                                    bullet.draw();
//                                Collision collision = bullet.update();
//                                if (collision != null) {
//                                    collision.collisionEvent();
//                                    bulletIterator.remove();
//                                    if (bullet.getBulletType() == Constant.REMOTE_BULLET_TYPE) {
//                                        Platform.runLater(new Runnable() {
//                                            @Override
//                                            public void run() {
//                                                sceneController.getMapPane().getChildren().remove(bullet.getCircleShape());
//                                            }
//                                        });
//                                    }
//                                }
                            } else {
//                                MyLogger.log.info("bulletKey: " + bulletKey + " " +
//                                        "sourceCreatureId: " + bullet.getSourceCreature().getCreatureId() + " " +
//                                        "targetCreatureId: " + bullet.getTargetCreature().getCreatureId());
                                bullet.setVisible(false);
                                bulletIterator.remove();
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        sceneController.getMapPane().getChildren().remove(bullet.getCircleShape());
                                    }
                                });
                            }
                        }
                        for (Creature myMember : myFamily.values()) {
                            myMember.sendAllAttribute(out);
                        }
                        Thread.sleep(Constant.FRAME_TIME);
                    } catch (Exception e) {
                        System.out.println("while(true)出错");
                        e.printStackTrace();
                    } finally {
                        //flag = !flag;
                    }
                }
            }
        }).start();

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (true) {
//                    try {
//                        Platform.runLater(new Runnable() {
//                            @Override
//                            public void run() {
//                                for (Creature myMember : myFamily.values()) {
//                                    ArrayList<Bullet> tempBullet = myMember.update();
////                                    if (tempBullet.size() != 0) {
////                                        bullets.addAll(tempBullet);
////                                        Iterator<Bullet> bulletIterator = tempBullet.listIterator();
////                                        while (bulletIterator.hasNext()) {
////                                            Bullet bullet = bulletIterator.next();
////                                            if (bullet.getBulletType() == Constant.REMOTE_BULLET_TYPE)
////                                                sceneController.getMapPane().getChildren().add(bullet.getCircleShape());
////                                        }
////                                    }
//                                }
////                                for (Creature enemyMember : enemyFamily.values()) {
////                                     // TODO: 改成接受服务器的消息进行变动
////                                    ArrayList<Bullet> tempBullet = enemyMember.update();
////                                    if (tempBullet.size() != 0) {
////                                        bullets.addAll(tempBullet);
////                                        Iterator<Bullet> bulletIterator = tempBullet.listIterator();
////                                        while (bulletIterator.hasNext()) {
////                                            Bullet bullet = bulletIterator.next();
////                                            if (bullet.getBulletType() == Constant.REMOTE_BULLET_TYPE)
////                                                sceneController.getMapPane().getChildren().add(bullet.getCircleShape());
////                                        }
////                                    }
////                                }
////                                Iterator<Bullet> bulletIterator = bullets.listIterator();
////                                while (bulletIterator.hasNext()) {
////                                    Bullet bullet = bulletIterator.next();
////                                    if (bullet.isValid()) {
////                                        Collision collision = bullet.update();
////                                        if (collision != null) {
////                                            collision.collisionEvent();
////                                            bulletIterator.remove();
////                                            if (bullet.getBulletType() == Constant.REMOTE_BULLET_TYPE) {
////                                                Platform.runLater(new Runnable() {
////                                                    @Override
////                                                    public void run() {
////                                                        sceneController.getMapPane().getChildren().remove(bullet.getCircleShape());
////                                                    }
////                                                });
////                                            }
////                                        }
////                                    }
////                                }
////                                if (equipmentFactory.hasNext()) {
////                                    Equipment equipment = equipmentFactory.next();
////                                    if (equipment != null)
////                                        equipmentList.add(equipment);
////                                }
////                                if (myCreature != null && myCreature.isAlive()) {
////                                    Iterator<Equipment> equipmentIterator = equipmentList.listIterator();
////                                    while (equipmentIterator.hasNext()) {
////                                        Equipment equipment = equipmentIterator.next();
////                                        if (equipment.getImageView().getBoundsInParent().intersects(myCreature.getCreatureImageView().getBoundsInParent())) {
////                                            myCreature.pickUpEquipment(equipment);
////                                            equipmentIterator.remove();
////                                        }
////                                    }
////                                }
////                                for (Equipment equipment : equipmentList) {
////                                    equipment.draw();
////                                }
//                            }
//                        });
//                        for(Creature myMember : myFamily.values()) {
//                            myMember.sendAllAttribute();
//                        }
//                        Thread.sleep(Constant.FRAME_TIME);
//                    } catch (Exception e) {
//                        System.out.println("while(true)出错");
//                        e.printStackTrace();
//                    } finally {
//                        //flag = !flag;
//                    }
//                }
//            }
//        }).start();
    }
}
