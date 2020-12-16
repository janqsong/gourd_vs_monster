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

    private final double WIDTH;
    private final double HEIGHT;


    //add 加上width表示图片宽度
    public CreatureClass(DataInputStream in, DataOutputStream out,
                         String campType, int creatureId, String creatureName,
                         int baseHealth, int baseMagic, int baseAttack, int baseDefense, int baseAttackSpeed,
                         int baseMoveSpeed, double shootRange, int faceDirection, double width,
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

        this.creatureLeftImage = creatureLeftImage;
        this.selectCreatureLeftImage = selectCreatureLeftImage;
        this.creatureRightImage = creatureRightImage;
        this.selectCreatureRightImage = selectCreatureRightImage;

        creatureImageView = new ImageView();

        creatureImageView.setPreserveRatio(true);
        this.WIDTH = width;
        creatureImageView.setFitWidth(width);
        this.HEIGHT = creatureImageView.getFitHeight();

        if (direction == Constant.Direction.LEFT)
            creatureImageView.setImage(creatureLeftImage);
        else
            creatureImageView.setImage(creatureRightImage);

        creatureImageView.setVisible(false);
        creatureImageView.setDisable(true);
        imagePosition = new ImagePosition(0, 0);


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

    }

    //根据方向设置图片状态
    public void setCreatureImageView() {
        if (isControlled) {
            if (direction == Constant.Direction.LEFT)
                this.creatureImageView.setImage(selectCreatureLeftImage);
            else
                this.creatureImageView.setImage(selectCreatureRightImage);
        } else {
            if (direction == Constant.Direction.LEFT)
                this.creatureImageView.setImage(creatureLeftImage);
            else
                this.creatureImageView.setImage(creatureRightImage);
        }
    }

    //设置position并更新控件位置
    public void setCreatureImagePos(double layoutX, double layoutY) {
        imagePosition.setLayoutX(layoutX);
        imagePosition.setLayoutY(layoutY);
        if(layoutY>Constant.FIGHT_PANE_HEIGHT-HEIGHT){
            System.out.println(HEIGHT);
            System.out.println(layoutY);
            try {
                Thread.sleep(100000);

            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        creatureImageView.setLayoutX(layoutX);
        creatureImageView.setLayoutY(layoutY);
    }

    //设置移动方向
    public void setDirection(int direction) {
        this.direction = direction;
        setCreatureImageView();
    }

    //判断是否下载可以攻击,收到攻速的限制
    public boolean canAttack() {
        return (System.currentTimeMillis() - lastAttackMillis >= 1000 / currentAttackSpeed);
    }

    //观测敌人,选取一个敌人下标返回
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

    //ai攻击,返回一颗子弹
    public Bullet aiAttack() {
        if (canAttack() && isAlive()) {
            int targetEnemyId = observeEnemy();
            if (targetEnemyId == -1)
                return null;
            if (imagePosition.getDistance(enemyFamily.get(targetEnemyId).getImagePos()) > shootRange)
                return null;
            lastAttackMillis = System.currentTimeMillis();
            return new Bullet(this, enemyFamily.get(targetEnemyId),
                    new ImagePosition(imagePosition.getLayoutX() + getImageWidth() / 2,
                            imagePosition.getLayoutY() + getImageHeight() / 2), null);
        }
        return null;
    }

    //一种移动方式,这里是随机移动
    public void randomMove() {
        direction = randomNum.nextInt(5);
    }

    public void move() {
        //发生位移
        if (!isAlive())
            return;
        double x = imagePosition.getLayoutX();
        double y = imagePosition.getLayoutY();
        switch (direction) {
            case Constant.Direction.UP: {
                y -= currentMoveSpeed;
                break;
            }
            case Constant.Direction.DOWN: {
                y += currentMoveSpeed;
                break;
            }
            case Constant.Direction.LEFT: {
                x -= currentMoveSpeed;
                break;
            }
            case Constant.Direction.RIGHT: {
                x += currentMoveSpeed;
                break;
            }
            default: {
                break;
            }
        }
        if (x < 0)
            x = 0;
        if (x > Constant.FIGHT_PANE_WIDTH - WIDTH)
            x = Constant.FIGHT_PANE_WIDTH - WIDTH;
        if (y < 0)
            y = 0;
        if (y > Constant.FIGHT_PANE_HEIGHT - HEIGHT)
            y = Constant.FIGHT_PANE_HEIGHT - HEIGHT;
        if (isControlled) {
            if (creatureImageView.getImage() != selectCreatureLeftImage
                    && direction == Constant.Direction.LEFT)
                creatureImageView.setImage(selectCreatureLeftImage);
            else if (creatureImageView.getImage() != selectCreatureRightImage
                    && direction == Constant.Direction.RIGHT)
                creatureImageView.setImage(selectCreatureRightImage);
        } else {
            if (creatureImageView.getImage() != creatureLeftImage
                    && direction == Constant.Direction.LEFT)
                creatureImageView.setImage(creatureLeftImage);
            else if (creatureImageView.getImage() != creatureRightImage
                    && direction == Constant.Direction.RIGHT)
                creatureImageView.setImage(creatureRightImage);
        }
        setCreatureImagePos(x, y);
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
        Platform.runLater(() -> healthProgressBar.setProgress(finalProgressValue));

        magicProgressBar.setVisible(true);
        magicProgressBar.setLayoutX(imagePosition.getLayoutX());
        magicProgressBar.setLayoutY(imagePosition.getLayoutY() - Constant.BAR_HEIGHT);
        progressValue = (double) currentHealth / baseHealth;
        double finalProgressValue1 = progressValue;
        Platform.runLater(() -> magicProgressBar.setProgress(finalProgressValue1));
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
        double x = imagePosition.getLayoutX();
        double y = imagePosition.getLayoutY();
        return new ImagePosition(x + WIDTH / 2, y + HEIGHT / 2);
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
        if (!isControlled) {
            if (isAlive()) {
                randomMove();
                draw();
                Bullet bullet = aiAttack();
                return bullet;
            } else {
                draw();
                return null;
            }
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
}
