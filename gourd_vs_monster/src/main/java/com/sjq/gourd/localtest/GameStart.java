package com.sjq.gourd.localtest;

import com.sjq.gourd.bullet.Bullet;
import com.sjq.gourd.collision.Collision;
import com.sjq.gourd.constant.Constant;
import com.sjq.gourd.constant.ImageUrl;
import com.sjq.gourd.creature.CreatureClass;
import com.sjq.gourd.creature.GourdClass;
import com.sjq.gourd.creature.ImagePosition;
import com.sjq.gourd.creature.MonsterClass;
import com.sjq.gourd.stage.PositionXY;
import com.sjq.gourd.stage.SceneController;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import org.checkerframework.checker.units.qual.C;
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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class GameStart {

    private HashMap<Integer, CreatureClass> gourdFamily = new HashMap<Integer, CreatureClass>();
    private HashMap<Integer, CreatureClass> monsterFamily = new HashMap<Integer, CreatureClass>();
    private SceneController sceneController;
    private final Random randomNum = new Random(System.currentTimeMillis());
    //add
    private List<Bullet> bullets = new ArrayList<>();

    //add
    private Lock lock = new ReentrantLock();
    private boolean flag = false;

    public GameStart(HashMap<Integer, CreatureClass> gourdFamily,
                     HashMap<Integer, CreatureClass> monsterFamily,
                     SceneController sceneController) {
        //TODO 这个类里，随便改，添加任何需要的东西。
        //TODO client和server那两个包应该都用不到，不用改.
        this.gourdFamily = gourdFamily;
        this.monsterFamily = monsterFamily;
        this.sceneController = sceneController;

        for (int i = 0; i < 200; i++){
            Circle circle=new Circle(Constant.BULLET_CIRCLE_RADIUS);
            sceneController.addShapeToMapPane(circle);
            bullets.add(new Bullet(null,null,new ImagePosition(0,0),circle));
        }
    }

    public void startGame() {
        initGame();

        for(CreatureClass creatureClass:gourdFamily.values())
            creatureClass.setEnemyFamily(monsterFamily);
        for(CreatureClass creatureClass:monsterFamily.values())
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
                                if (gourdClass.isControlled()) {
                                    gourdClass.flipControlled();
                                    gourdClass.setCreatureImageView();
                                    break;
                                }
                            gourd.setSelectCreatureImageView();
                            gourd.flipControlled();
                        }
                    });
                    imageView.setOnKeyPressed(new EventHandler<KeyEvent>() {
                        @Override
                        public void handle(KeyEvent event) {
                            KeyCode keyCode = event.getCode();
                            if (keyCode == KeyCode.A || keyCode == KeyCode.KP_UP)
                                ;
                            else if (keyCode == KeyCode.D || keyCode == KeyCode.KP_DOWN)
                                ;
                            else if (keyCode == KeyCode.W || keyCode == KeyCode.KP_LEFT)
                                ;
                            else if (keyCode == KeyCode.S || keyCode == KeyCode.KP_RIGHT)
                                ;
                            else if (keyCode == KeyCode.R)
                                ;
                        }
                    });


                }
                while (true) {
                    try {
                        if (true) {
                            System.out.println("GourdStart");
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
                            System.out.println("endGourd");
                            //flag=!flag;
                            Thread.sleep(Constant.FRAME_TIME);
                        }
                    } catch (Exception e){
                        System.out.println("GourdException");
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
                while (true) {
                    try {
                        if (!flag) {

                            System.out.println("MonsterStart");
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
                            System.out.println("endMonster");
                            Thread.sleep(Constant.FRAME_TIME / 2);
                            flag=!flag;
                        }
                    } catch (Exception e) {
                        System.out.println("MonsterException");
                        e.printStackTrace();
                    } finally {
                        flag = !flag;
                    }
                }
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
            int baseHealth = gourdObject.getInt("baseHealth");
            int baseMagic = gourdObject.getInt("baseMagic");
            int baseAttack = gourdObject.getInt("baseAttack");
            int baseDefense = gourdObject.getInt("baseDefense");
            int baseAttackSpeed = gourdObject.getInt("baseAttackSpeed");
            int baseMoveSpeed = gourdObject.getInt("baseMoveSpeed");
            double shootRange = gourdObject.getDouble("shootRange");
            int faceDirection = gourdObject.getInt("faceDirection");
            Image gourdLeftImage = ImageUrl.gourdLeftImageMap.get(creatureId);
            Image gourdLeftSelectImage = ImageUrl.gourdLeftSelectImageMap.get(creatureId);
            Image gourdRightImage = ImageUrl.gourdRightImageMap.get(creatureId);
            Image gourdRightSelectImage = ImageUrl.gourdRightSelectImageMap.get(creatureId);
            GourdClass gourdMember = new GourdClass(null, null, Constant.CampType.GOURD, creatureId, creatureName,
                    baseHealth, baseMagic, baseAttack, baseDefense, baseAttackSpeed, baseMoveSpeed, shootRange, faceDirection,
                    gourdLeftImage, gourdLeftSelectImage, gourdRightImage, gourdRightSelectImage);
            gourdMember.setCreatureImageView();
            ImageView tempImageView = gourdMember.getCreatureImageView();
            gourdMember.setCreatureImagePos(
                    randomNum.nextDouble() * (Constant.FIGHT_PANE_WIDTH / 2 - tempImageView.getFitWidth()),
                    randomNum.nextDouble() * (Constant.FIGHT_PANE_HEIGHT - 100));
            tempImageView.setVisible(true);
            tempImageView.setDisable(false);
            sceneController.addImageViewToMapPane(gourdMember.getCreatureImageView());
            sceneController.addProgressBarToMapPane(gourdMember.getHealthProgressBar());
            sceneController.addProgressBarToMapPane(gourdMember.getMagicProgressBar());

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
            int baseAttackSpeed = monsterObject.getInt("baseAttackSpeed");
            int baseMoveSpeed = monsterObject.getInt("baseMoveSpeed");
            double shootRange = monsterObject.getDouble("shootRange");
            int faceDirection = monsterObject.getInt("faceDirection");
            Image monsterLeftImage = ImageUrl.monsterLeftImageMap.get(creatureId);
            Image monsterLeftSelectImage = ImageUrl.monsterLeftSelectImageMap.get(creatureId);
            Image monsterRightImage = ImageUrl.monsterRightImageMap.get(creatureId);
            Image monsterRightSelectImage = ImageUrl.monsterRightSelectImageMap.get(creatureId);
            MonsterClass monsterMember = new MonsterClass(null, null, Constant.CampType.GOURD, creatureId, creatureName,
                    baseHealth, baseMagic, baseAttack, baseDefense, baseAttackSpeed, baseMoveSpeed, shootRange, faceDirection,
                    monsterLeftImage, monsterLeftSelectImage, monsterRightImage, monsterRightSelectImage);
            monsterMember.setCreatureImageView();
            ImageView tempImageView = monsterMember.getCreatureImageView();
            monsterMember.setCreatureImagePos(
                    randomNum.nextDouble() * (Constant.FIGHT_PANE_WIDTH / 2 - tempImageView.getFitWidth()) + Constant.FIGHT_PANE_WIDTH / 2,
                    randomNum.nextDouble() * (Constant.FIGHT_PANE_HEIGHT - 100));
            tempImageView.setVisible(true);
            tempImageView.setDisable(false);
            sceneController.addImageViewToMapPane(monsterMember.getCreatureImageView());
            sceneController.addProgressBarToMapPane(monsterMember.getHealthProgressBar());
            sceneController.addProgressBarToMapPane(monsterMember.getMagicProgressBar());
            monsterFamily.put(creatureId, monsterMember);
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
