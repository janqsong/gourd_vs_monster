package com.sjq.gourd.localtest;

import com.sjq.gourd.bullet.Bullet;
import com.sjq.gourd.collision.Collision;
import com.sjq.gourd.constant.Constant;
import com.sjq.gourd.constant.ImageUrl;
import com.sjq.gourd.creature.CreatureClass;
import com.sjq.gourd.creature.ImagePosition;
import com.sjq.gourd.equipment.Equipment;
import com.sjq.gourd.equipment.EquipmentFactory;
import com.sjq.gourd.stage.SceneController;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.List;


public class GameStart {

    private HashMap<Integer, CreatureClass> gourdFamily = new HashMap<Integer, CreatureClass>();
    private HashMap<Integer, CreatureClass> monsterFamily = new HashMap<Integer, CreatureClass>();
    private SceneController sceneController;
    private final Random randomNum = new Random(System.currentTimeMillis());
    //add
    private List<Bullet> bullets = new ArrayList<>();
    private List<Equipment> equipmentList = new ArrayList<>();
    private EquipmentFactory equipmentFactory = null;

    private boolean flag = false;

    public GameStart(HashMap<Integer, CreatureClass> gourdFamily,
                     HashMap<Integer, CreatureClass> monsterFamily,
                     SceneController sceneController) {
        //TODO 这个类里，随便改，添加任何需要的东西。
        //TODO client和server那两个包应该都用不到，不用改.
        this.gourdFamily = gourdFamily;
        this.monsterFamily = monsterFamily;
        this.sceneController = sceneController;

        for (int i = 0; i < 300; i++) {
            Circle circle = new Circle(Constant.BULLET_CIRCLE_RADIUS);
            sceneController.addShapeToMapPane(circle);
            bullets.add(new Bullet(null, null, new ImagePosition(0, 0), circle));
        }
    }

    public void startGame() {
        ArrayList<ImageView> imageViews = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            ImageView imageView = new ImageView();
            imageView.setVisible(false);
            imageView.setDisable(true);
            imageViews.add(imageView);
            sceneController.addImageViewToMapPane(imageView);
        }
        equipmentFactory = new EquipmentFactory(imageViews);
        initGame();

        for (CreatureClass creatureClass : gourdFamily.values())
            creatureClass.setEnemyFamily(monsterFamily);
        for (CreatureClass creatureClass : monsterFamily.values())
            creatureClass.setEnemyFamily(gourdFamily);

        gourdStartGame();
//        monsterStartGame();
    }

    public void gourdStartGame() {
        //TODO 葫芦娃的游戏接口一定要在这个线程里写。
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (CreatureClass gourd : gourdFamily.values()) {
                    ImageView imageView = gourd.getCreatureImageView();
                    //点击选中
                    imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            for (CreatureClass gourdClass : gourdFamily.values())
                                if (gourdClass.isControlled() && gourdClass != gourd) {
                                    gourdClass.flipControlled();
                                    gourdClass.setCreatureImageView();
                                    gourdClass.getCreatureImageView().setFocusTraversable(false);
                                    break;
                                }
                            if (!gourd.isControlled()) {
                                gourd.flipControlled();
                                gourd.setCreatureImageView();
                                gourd.getCreatureImageView().setFocusTraversable(true);
                            }
                        }
                    });
                    imageView.setOnKeyPressed(new EventHandler<KeyEvent>() {
                        @Override
                        public void handle(KeyEvent event) {
                            KeyCode keyCode = event.getCode();
                            System.out.println(keyCode.getName());
                            if (keyCode == KeyCode.A || keyCode == KeyCode.KP_LEFT)
                                gourd.setDirection(Constant.Direction.LEFT);
                            else if (keyCode == KeyCode.D || keyCode == KeyCode.KP_RIGHT)
                                gourd.setDirection(Constant.Direction.RIGHT);
                            else if (keyCode == KeyCode.W || keyCode == KeyCode.KP_UP)
                                gourd.setDirection(Constant.Direction.UP);
                            else if (keyCode == KeyCode.S || keyCode == KeyCode.KP_DOWN)
                                gourd.setDirection(Constant.Direction.DOWN);
                            else if (keyCode == KeyCode.R)
                                ;
                        }
                    });

                    imageView.setOnKeyReleased(new EventHandler<KeyEvent>() {
                        @Override
                        public void handle(KeyEvent event) {
                            KeyCode keyCode = event.getCode();
                            System.out.println(keyCode.getName());
                            if (keyCode == KeyCode.A || keyCode == KeyCode.KP_LEFT)
                                gourd.setDirection(Constant.Direction.STOP);
                            else if (keyCode == KeyCode.D || keyCode == KeyCode.KP_RIGHT)
                                gourd.setDirection(Constant.Direction.STOP);
                            else if (keyCode == KeyCode.W || keyCode == KeyCode.KP_UP)
                                gourd.setDirection(Constant.Direction.STOP);
                            else if (keyCode == KeyCode.S || keyCode == KeyCode.KP_DOWN)
                                gourd.setDirection(Constant.Direction.STOP);
                        }
                    });


                }
                while (true) {
                    try {
                        if (true) {
                            for (CreatureClass gourdMember : gourdFamily.values()) {
                                Bullet bullet = gourdMember.update();
                                if (!pushBackBulletList(bullet))
                                    throw new RuntimeException("子弹过多");
                            }
                            for (CreatureClass monsterMember : monsterFamily.values()) {
                                Bullet bullet = monsterMember.update();
                                if (!pushBackBulletList(bullet))
                                    throw new RuntimeException("子弹过多");
                            }
                            for (Bullet bullet : bullets) {
                                if (bullet.isValid()) {
                                    Collision collision = bullet.update();
                                    if (collision != null)
                                        collision.collisionEvent();
                                }
                            }
                            if (equipmentFactory.hasNext()) {
                                Equipment equipment = equipmentFactory.next();
                                if (equipment != null)
                                    equipmentList.add(equipment);
                            }
                            for (CreatureClass creatureClass : gourdFamily.values()) {
                                if (creatureClass.isControlled()) {
                                    for (Equipment equipment : equipmentList) {
                                        if (equipment.getImageView().getBoundsInParent().intersects(creatureClass.getCreatureImageView().getBoundsInParent())) {
                                            creatureClass.pickUpEquipment(equipment);
                                            equipmentList.remove(equipment);
                                        }
                                    }
                                }
                            }
                            for(Equipment equipment:equipmentList){
                                equipment.draw();
                            }
                            //flag=!flag;
                            Thread.sleep(Constant.FRAME_TIME);
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

    public void monsterStartGame() {
        //TODO 妖怪的开始游戏接口一定要在这个线程里写。
        //TODO 两个线程主要是通过全局变量通信。
        new Thread(new Runnable() {
            @Override
            public void run() {
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
            }
        }).start();
    }

    public void initGame() {
        //TODO 游戏初始化尽量照着这个样子初始化，不要差太大.
        JSONObject gourdJSONObject = new JSONObject(getJsonContentText("GourdInformation.json"));
        JSONArray gourdInfoArray = (JSONArray) gourdJSONObject.get("gourdInfo");

        JSONObject monsterJSONObject = new JSONObject(getJsonContentText("MonsterInformation.json"));
        JSONArray monsterInfoArray = (JSONArray) monsterJSONObject.get("monsterInfo");

        ImageUrl.initImageUrl();
        for (int i = 0; i < gourdInfoArray.length(); i++) {
            JSONObject gourdObject = (JSONObject) gourdInfoArray.get(i);
            int creatureId = gourdObject.getInt("creatureId");
            String creatureName = gourdObject.getString("creatureName");
            double baseHealth = gourdObject.getDouble("baseHealth");
            double baseMagic = gourdObject.getDouble("baseMagic");
            double baseAttack = gourdObject.getDouble("baseAttack");
            double baseDefense = gourdObject.getDouble("baseDefense");
            double baseAttackSpeed = gourdObject.getDouble("baseAttackSpeed");
            double baseMoveSpeed = gourdObject.getDouble("baseMoveSpeed");
            double shootRange = gourdObject.getDouble("shootRange");
            int faceDirection = gourdObject.getInt("faceDirection");
            double imageWidth = gourdObject.getDouble("imageWidth");
            boolean isCloseAttack = gourdObject.getBoolean("isCloseAttack");
            int clawType = gourdObject.getInt("clawType");
            Image gourdLeftImage = ImageUrl.gourdLeftImageMap.get(creatureId);
            Image gourdLeftSelectImage = ImageUrl.gourdLeftSelectImageMap.get(creatureId);
            Image gourdRightImage = ImageUrl.gourdRightImageMap.get(creatureId);
            Image gourdRightSelectImage = ImageUrl.gourdRightSelectImageMap.get(creatureId);
            ImageView imageView = new ImageView();
            ImageView closeAttackImageView = new ImageView();
            //sceneController.addImageViewToMapPane(closeAttackImageView);
            sceneController.addImageViewToMapPane(imageView);

            CreatureClass gourdMember = new CreatureClass(null, null, Constant.CampType.GOURD, creatureId, creatureName,
                    baseHealth, baseMagic, baseAttack, baseDefense, baseAttackSpeed, baseMoveSpeed, shootRange, faceDirection,
                    imageWidth, isCloseAttack, clawType, imageView, closeAttackImageView, gourdLeftImage, gourdLeftSelectImage, gourdRightImage, gourdRightSelectImage);
            gourdMember.setCreatureImageView();
            ImageView tempImageView = gourdMember.getCreatureImageView();
            gourdMember.setCreatureImagePos(
                    randomNum.nextDouble() * (Constant.FIGHT_PANE_WIDTH / 2 - tempImageView.getFitWidth()),
                    randomNum.nextDouble() * (Constant.FIGHT_PANE_HEIGHT - 100));

            sceneController.addProgressBarToMapPane(gourdMember.getHealthProgressBar());
            sceneController.addProgressBarToMapPane(gourdMember.getMagicProgressBar());

            gourdMember.getCreatureImageView().setFitWidth(50);
            gourdFamily.put(creatureId, gourdMember);
        }

        for (int i = 0; i < monsterInfoArray.length(); i++) {
            JSONObject monsterObject = (JSONObject) monsterInfoArray.get(i);
            int creatureId = monsterObject.getInt("creatureId");
            String creatureName = monsterObject.getString("creatureName");
            int baseHealth = monsterObject.getInt("baseHealth");
            int baseMagic = monsterObject.getInt("baseMagic");
            int baseAttack = monsterObject.getInt("baseAttack");
            int baseDefense = monsterObject.getInt("baseDefense");
            double baseAttackSpeed = monsterObject.getDouble("baseAttackSpeed");
            double baseMoveSpeed = monsterObject.getDouble("baseMoveSpeed");
            double shootRange = monsterObject.getDouble("shootRange");
            int faceDirection = monsterObject.getInt("faceDirection");
            double imageWidth = monsterObject.getDouble("imageWidth");
            boolean isCloseAttack = monsterObject.getBoolean("isCloseAttack");
            int clawType = monsterObject.getInt("clawType");
            Image monsterLeftImage = ImageUrl.monsterLeftImageMap.get(creatureId);
            Image monsterLeftSelectImage = ImageUrl.monsterLeftSelectImageMap.get(creatureId);
            Image monsterRightImage = ImageUrl.monsterRightImageMap.get(creatureId);
            Image monsterRightSelectImage = ImageUrl.monsterRightSelectImageMap.get(creatureId);
            ImageView imageView = new ImageView();
            sceneController.addImageViewToMapPane(imageView);

            ImageView closeAttackImageView = new ImageView();
            //sceneController.addImageViewToMapPane(closeAttackImageView);
            CreatureClass monsterMember = new CreatureClass(null, null, Constant.CampType.MONSTER, creatureId, creatureName,
                    baseHealth, baseMagic, baseAttack, baseDefense, baseAttackSpeed, baseMoveSpeed, shootRange, faceDirection,
                    imageWidth, isCloseAttack, clawType, imageView, closeAttackImageView, monsterLeftImage, monsterLeftSelectImage, monsterRightImage, monsterRightSelectImage);
            monsterMember.setCreatureImageView();
            ImageView tempImageView = monsterMember.getCreatureImageView();
            monsterMember.setCreatureImagePos(
                    randomNum.nextDouble() * (Constant.FIGHT_PANE_WIDTH / 2 - tempImageView.getFitWidth()) + Constant.FIGHT_PANE_WIDTH / 2,
                    randomNum.nextDouble() * (Constant.FIGHT_PANE_HEIGHT - 100));


            sceneController.addProgressBarToMapPane(monsterMember.getHealthProgressBar());
            sceneController.addProgressBarToMapPane(monsterMember.getMagicProgressBar());

            monsterFamily.put(creatureId, monsterMember);
        }

        for (CreatureClass creature : gourdFamily.values()) {
            sceneController.addImageViewToMapPane(creature.getCloseAttackImageView());
        }
        for (CreatureClass creature : monsterFamily.values()) {
            sceneController.addImageViewToMapPane(creature.getCloseAttackImageView());
        }
    }

    public String getJsonContentText(String path) {
        StringBuilder jsonContent = new StringBuilder();
        BufferedReader reader = null;
        try {
            InputStream in = getClass().getClassLoader().getResourceAsStream(path);
            assert in != null;
            reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                jsonContent.append(tempString);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonContent.toString();
    }

    private boolean pushBackBulletList(Bullet bullet) {
        if (bullet == null)
            return true;
        for (Bullet bullet1 : bullets) {
            if (!bullet1.isValid()) {
                bullet1.changeBullet(bullet);
                return true;
            }
        }
        return false;
    }
}
