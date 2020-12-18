package com.sjq.gourd.creature;

import com.sjq.gourd.ai.AiInterface;
import com.sjq.gourd.ai.FirstGenerationAi;
import com.sjq.gourd.constant.Constant;
//import com.sjq.gourd.protocol.PositionNotifyMsg;
import com.sjq.gourd.bullet.Bullet;
import com.sjq.gourd.constant.ImageUrl;
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

    protected long lastAttackMillis = 0;

    //上次被近战攻击的时间,注意是被近战攻击,检测到上次被攻击时间到现在的时间,超过一个较短的时间gap就让抓恒消失
    private long lastCloseAttack = 0;
    //显示被攻击是图片上的抓痕的控件
    private ImageView closeAttackImageView;
    //是否是近战类型
    private boolean isCloseAttack;
    //近战类型的抓痕
    private int clawType = Constant.ClawType.NONE_CLAW;

    protected int direction;
    protected boolean isControlled = false;
    protected ImagePosition imagePosition;
    protected AiInterface aiInterface;
    //每次攻击回复的蓝量
    private final double magicIncrementOnce = 10.0;

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
                         double baseHealth, double baseMagic, double baseAttack, double baseDefense, double baseAttackSpeed,
                         double baseMoveSpeed, double shootRange, int faceDirection, double width, boolean isCloseAttack, int clawType,
                         ImageView imageView, ImageView closeAttackImageView,
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

        this.isCloseAttack = isCloseAttack;
        this.clawType = clawType;

        this.creatureLeftImage = creatureLeftImage;
        this.selectCreatureLeftImage = selectCreatureLeftImage;
        this.creatureRightImage = creatureRightImage;
        this.selectCreatureRightImage = selectCreatureRightImage;

        //把抓痕的长宽暂时设置为50 50
        //初始化时抓痕不可见,只有在被近战攻击时,抓痕才可见
        this.closeAttackImageView = closeAttackImageView;
        this.closeAttackImageView.setPreserveRatio(true);
        this.closeAttackImageView.setFitWidth(50);
        this.closeAttackImageView.setFitHeight(50);
        closeAttackImageView.setVisible(false);

        creatureImageView = imageView;

        creatureImageView.setPreserveRatio(true);
        this.WIDTH = width;
        creatureImageView.setFitWidth(width);
        this.HEIGHT = creatureLeftImage.getHeight() / creatureLeftImage.getWidth() * width;
        creatureImageView.setFitHeight(HEIGHT);
        System.out.println(HEIGHT);

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

        //ai
        aiInterface = new FirstGenerationAi(creatureId + System.currentTimeMillis());
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
        if (layoutY > Constant.FIGHT_PANE_HEIGHT - HEIGHT) {
            System.out.println(HEIGHT);
            System.out.println(layoutY);
            try {
                Thread.sleep(100000);

            } catch (Exception e) {
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
        return (System.currentTimeMillis() - lastAttackMillis >= 1000.0 / currentAttackSpeed);
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
        Platform.runLater(() -> healthProgressBar.setVisible(true));
        healthProgressBar.setLayoutX(imagePosition.getLayoutX());
        healthProgressBar.setLayoutY(imagePosition.getLayoutY() - 2 * Constant.BAR_HEIGHT);
        double progressValue = (double) currentHealth / baseHealth;
        double finalProgressValue = progressValue;
        Platform.runLater(() -> healthProgressBar.setProgress(finalProgressValue));

        Platform.runLater(() -> magicProgressBar.setVisible(true));
        magicProgressBar.setLayoutX(imagePosition.getLayoutX());
        magicProgressBar.setLayoutY(imagePosition.getLayoutY() - Constant.BAR_HEIGHT);
        progressValue = (double) currentMagic / baseMagic;
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
        drawCloseAttack();
        if (isAlive()) {
            creatureImageView.setVisible(true);
            creatureImageView.setDisable(false);
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
                aiInterface.moveMod(this);
                draw();
                Bullet bullet = aiInterface.aiAttack(this, enemyFamily);
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


    public double getShootRange() {
        return shootRange;
    }

    public void setLastAttackTimeMillis(long currentTimeMillis) {
        lastAttackMillis = currentTimeMillis;
    }

    public double getCurrentMoveSpeed() {
        return currentMoveSpeed;
    }

    public double getBaseHealth() {
        return baseHealth;
    }


    public double getCurrentMagic() {
        return currentMagic;
    }

    public void setCurrentMagic(double currentMagic) {
        if (currentMagic < 0)
            currentMagic = 0;
        if (currentMagic > baseMagic)
            currentMagic = baseMagic;
        this.currentMagic = currentMagic;
    }

    public double getMagicIncrementOnce() {
        return magicIncrementOnce;
    }

    public void setLastCloseAttack(long lastCloseAttack) {
        this.lastCloseAttack = lastCloseAttack;
    }

    public void drawCloseAttack() {
        if (System.currentTimeMillis() - lastCloseAttack <= Constant.CLAW_IMAGE_EXIST_TIME) {
            //显示近战攻击图片
            closeAttackImageView.setLayoutX(imagePosition.getLayoutX());
            closeAttackImageView.setLayoutY(imagePosition.getLayoutY());
            closeAttackImageView.setVisible(true);
        } else {
            //否则不显示近战图片
            closeAttackImageView.setVisible(false);
        }
    }

    public String getCreatureName() {
        return creatureName;
    }

    public HashMap<Integer, CreatureClass> getEnemyFamily() {
        return enemyFamily;
    }

    public boolean isCloseAttack() {
        return isCloseAttack;
    }

    public int getClawType() {
        return clawType;
    }

    public ImageView getCloseAttackImageView() {
        return closeAttackImageView;
    }
}
