package com.sjq.gourd.creature;

import com.sjq.gourd.constant.Constant;
import com.sjq.gourd.protocol.PositionNotifyMsg;
import com.sjq.gourd.bullet.Bullet;
import javafx.application.Platform;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.HashMap;
import java.util.Random;

public class CreatureClass {
    protected HashMap<Integer, CreatureClass> enemyFamily;
    protected DataInputStream inputStream;
    protected DataOutputStream outputStream;

    protected final String campType;
    protected final int creatureId;
    protected final String creatureName;

    //protected boolean isAlive;//删除这个,保留接口判断生死

    protected final double baseHealth;
    protected final double baseMagic;
    protected final double baseAttack;
    protected final double baseDefense;
    protected final double baseAttackSpeed;
    protected final double baseMoveSpeed;
    protected final double shootRange;

    protected double currentHealth;
    protected double currentMagic;
    protected double currentAttack;
    protected double currentDefense;
    protected double currentAttackSpeed;
    protected double currentMoveSpeed;

    protected long lastAttackMillis = System.currentTimeMillis();

    protected int direction;
    protected int faceDirection;
    protected boolean isControlled = false;
    protected ImagePosition imagePosition;

    ProgressBar healthProgressBar = new ProgressBar();
    ProgressBar magicProgressBar = new ProgressBar();


    protected ImageView creatureImageView;

    protected Image creatureRightImage;
    protected Image selectCreatureRightImage;
    protected Image creatureLeftImage;
    protected Image selectCreatureLeftImage;

    private final Random randomNum = new Random(System.currentTimeMillis());

    public CreatureClass(DataInputStream in, DataOutputStream out,
                         String campType, int creatureId, String creatureName,
                         int baseHealth, int baseMagic, int baseAttack, int baseDefense, int baseAttackSpeed,
                         int baseMoveSpeed, double shootRange, int faceDirection,
                         Image creatureLeftImage, Image selectCreatureLeftImage,
                         Image creatureRightImage, Image selectCreatureRightImage) {
        //TODO 这个类里，尽量不要改，改也可以，你可以和我说你下，你要改哪些内容，可以多加函数。
        this.inputStream = in;
        this.outputStream = out;
        this.campType = campType;
        this.creatureId = creatureId;
        this.creatureName = creatureName;
        this.currentHealth = this.baseHealth = baseHealth;
        this.baseMagic = baseMagic;
        this.currentMagic = 0;
        this.currentAttack = this.baseAttack = baseAttack;
        this.currentDefense = this.baseDefense = baseDefense;
        this.currentAttackSpeed = this.baseAttackSpeed = baseAttackSpeed;
        this.currentMoveSpeed = this.baseMoveSpeed = baseMoveSpeed;
        this.shootRange = shootRange;
        this.direction = faceDirection;
        this.faceDirection = faceDirection;

        this.creatureLeftImage = creatureLeftImage;
        this.selectCreatureLeftImage = selectCreatureLeftImage;
        this.creatureRightImage = creatureRightImage;
        this.selectCreatureRightImage = selectCreatureRightImage;

        creatureImageView = new ImageView();
        creatureImageView.setFitWidth(Constant.CREATURE_IMAGE_WIDTH);
        creatureImageView.setFitHeight(Constant.CREATURE_IMAGE_HEIGHT);
        creatureImageView.setPreserveRatio(true);
        creatureImageView.setVisible(false);
        creatureImageView.setDisable(true);
        imagePosition = new ImagePosition(0, 0,
                creatureImageView.getFitWidth(), creatureImageView.getFitHeight());


        healthProgressBar.setPrefWidth(Constant.CREATURE_IMAGE_WIDTH);
        healthProgressBar.setPrefHeight(Constant.BAR_HEIGHT);
        healthProgressBar.setMinHeight(Constant.BAR_HEIGHT);
        healthProgressBar.setStyle("-fx-accent: red;");
        healthProgressBar.setVisible(false);

        magicProgressBar.setPrefWidth(Constant.CREATURE_IMAGE_WIDTH);
        magicProgressBar.setPrefHeight(Constant.BAR_HEIGHT);
        magicProgressBar.setMinHeight(Constant.BAR_HEIGHT);
        magicProgressBar.setStyle("-fx-accent: blue;");
        magicProgressBar.setVisible(false);

        //debug
        this.currentAttackSpeed=3;
        System.out.println(creatureImageView.getFitHeight());
    }

    public void setCreatureImageView() {
        if (faceDirection == Constant.Direction.LEFT)
            this.creatureImageView.setImage(creatureLeftImage);
        else
            this.creatureImageView.setImage(creatureRightImage);
    }

    public void setSelectCreatureImageView() {
        if (faceDirection == Constant.Direction.LEFT)
            this.creatureImageView.setImage(selectCreatureLeftImage);
        else
            this.creatureImageView.setImage(selectCreatureRightImage);
    }

    public void setCreatureImagePos(double layoutX, double layoutY) {
        imagePosition.setLayoutX(layoutX);
        imagePosition.setLayoutY(layoutY);
        creatureImageView.setLayoutX(layoutX);
        creatureImageView.setLayoutY(layoutY);
    }

    public void setFaceDirection(int faceDirection) {
        this.faceDirection = faceDirection;
        setCreatureImageView();
    }

    public boolean canAttack() {
        return (System.currentTimeMillis() - lastAttackMillis >= 1000 / currentAttackSpeed);
    }

    private int observeEnemy() {
        if (isAlive()) {
            int selectEnemyId = 0;
            double minDistance = 1400.0;
            for (CreatureClass creatureMember : enemyFamily.values()) {
                double distance = imagePosition.getDistance(creatureMember.getImagePos());
                if (distance < minDistance) {
                    selectEnemyId = creatureMember.getCreatureId();
                    minDistance = distance;
                }
            }
            return selectEnemyId;
        }
        return -1;
    }

    public Bullet aiAttack() {
        if (canAttack() && isAlive()) {
            int targetEnemyId = observeEnemy();
            if (targetEnemyId == -1)
                return null;
            if (imagePosition.getDistance(enemyFamily.get(targetEnemyId).getImagePos()) > shootRange)
                return null;
            lastAttackMillis=System.currentTimeMillis();
            return new Bullet(this, enemyFamily.get(targetEnemyId),
                    new ImagePosition(imagePosition.getLayoutX() + getImageWidth() / 2,
                            imagePosition.getLayoutY() + getImageHeight() / 2), null);
        }
        return null;
    }

    public String getCampType() {
        return campType;
    }

    public ImageView getCreatureImageView() {
        return creatureImageView;
    }

    public int getCreatureId() {
        return creatureId;
    }

    public ProgressBar getHealthProgressBar() {
        return healthProgressBar;
    }

    public ProgressBar getMagicProgressBar() {
        return magicProgressBar;
    }

    public ImagePosition getImagePos() {
        return imagePosition;
    }

    public double getCurrentHealth() {
        return currentHealth;
    }

    public double getCurrentAttack() {
        return currentAttack;
    }

    public double getCurrentDefense() {
        return currentDefense;
    }

    public void randomMove() {
        direction = randomNum.nextInt(5);
    }

    public void move() {
        if (isAlive()) {
            switch (direction) {
                case Constant.Direction.UP: {
                    imagePosition.setLayoutY(imagePosition.getLayoutY() - currentMoveSpeed);
                    break;
                }
                case Constant.Direction.DOWN: {
                    imagePosition.setLayoutY(imagePosition.getLayoutY() + currentMoveSpeed);
                    break;
                }
                case Constant.Direction.LEFT: {
                    if (faceDirection == Constant.Direction.RIGHT) {
                        faceDirection = Constant.Direction.LEFT;
                        creatureImageView.setImage(creatureLeftImage);
                    }
                    imagePosition.setLayoutX(imagePosition.getLayoutX() - currentMoveSpeed);
                    break;
                }
                case Constant.Direction.RIGHT: {
                    if (faceDirection == Constant.Direction.LEFT) {
                        faceDirection = Constant.Direction.RIGHT;
                        creatureImageView.setImage(creatureRightImage);
                    }
                    imagePosition.setLayoutX(imagePosition.getLayoutX() + currentMoveSpeed);
                    break;
                }
                default:
                    break;
            }
            creatureImageView.setLayoutX(imagePosition.getLayoutX());
            creatureImageView.setLayoutY(imagePosition.getLayoutY());
        }
    }

    public boolean isAlive() {
        return currentHealth > 0;
    }

    public void drawBar() {
        healthProgressBar.setVisible(true);
        healthProgressBar.setLayoutX(imagePosition.getLayoutX());
        healthProgressBar.setLayoutY(imagePosition.getLayoutY() - 2 * Constant.BAR_HEIGHT);
        double progressValue = (double) currentHealth / baseHealth;
        double finalProgressValue = progressValue;
        Platform.runLater(()->healthProgressBar.setProgress(finalProgressValue));

        magicProgressBar.setVisible(true);
        magicProgressBar.setLayoutX(imagePosition.getLayoutX());
        magicProgressBar.setLayoutY(imagePosition.getLayoutY() - Constant.BAR_HEIGHT);
        progressValue = (double) currentHealth / baseHealth;
        double finalProgressValue1 = progressValue;
        Platform.runLater(()->magicProgressBar.setProgress(finalProgressValue1));
    }

    public void setCurrentHealth(double healthVal) {
        if (healthVal > baseHealth)
            currentHealth = baseHealth;
        else if (healthVal < 0)
            currentHealth = 0;
        else
            currentHealth = healthVal;
    }

    public ImagePosition getCenterPos() {
        return new ImagePosition(imagePosition.getLayoutX() + getImageWidth() / 2,
                imagePosition.getLayoutY() + getImageHeight() / 2,
                getImageWidth(), getImageHeight());
    }

    public double getImageWidth() {
        return creatureImageView.getFitWidth();
    }

    public double getImageHeight() {
        return creatureImageView.getFitHeight();
    }

    public void draw() {
        if (isAlive()) {
            drawBar();
            move();
        } else {
            creatureImageView.setVisible(false);
            creatureImageView.setDisable(true);
            healthProgressBar.setVisible(false);
            magicProgressBar.setVisible(false);
        }
    }


    //封装移动方式,画,攻击,返回子弹
    public Bullet update() {
        if (isAlive()) {
            randomMove();
            draw();
            Bullet bullet = aiAttack();
            return bullet;
        } else {
            draw();
            return null;
        }
    }

    //翻转isControlled状态
    public void flipControlled() {
        isControlled = !isControlled;
    }

    public boolean isControlled() {
        return isControlled;
    }

    public void setEnemyFamily(HashMap<Integer, CreatureClass> hashMap) {
        enemyFamily = hashMap;
    }
}
