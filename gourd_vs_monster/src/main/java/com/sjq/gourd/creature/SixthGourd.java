package com.sjq.gourd.creature;

import com.sjq.gourd.bullet.Bullet;
import com.sjq.gourd.constant.Constant;
import com.sjq.gourd.constant.CreatureId;
import com.sjq.gourd.constant.ImageUrl;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;

public class SixthGourd extends Creature {

    private long lastTransfigurationMillis;
    private boolean inQAction = false;
    private double moveSpeedIncrement = 5;
    private double attackDecrement = 20;
    private double defenseIncrement = 100;
    private final long gap = 5000; //5000ms时间

    //隐身的照片
    private static Image imageLeft = new Image("/images/gourdImages/sixthGourdLeftLucency.png");
    private static Image imageLeftSelect = new Image("/images/gourdImages/sixthGourdLeftSelectLucency.png");
    private static Image imageRight = new Image("/images/gourdImages/sixthGourdRightLucency.png");
    private static Image imageRightSelect = new Image("/images/gourdImages/sixthGourdRightSelectLucency.png");

    public SixthGourd(int faceDirection, ImageView imageView, ImageView closeAttackImageView) {
        super(Constant.CampType.GOURD, CreatureId.SIXTH_GOURD_ID, CreatureId.SIXTH_GOURD_NAME,
                3500, 100, 150, 20, 1.0, 12, 90.0,
                faceDirection, 70.0, true, Constant.ClawType.FIRST_CLAW,
                imageView, closeAttackImageView, ImageUrl.gourdLeftImageMap.get(CreatureId.SIXTH_GOURD_ID),
                ImageUrl.gourdLeftSelectImageMap.get(CreatureId.SIXTH_GOURD_ID),
                ImageUrl.gourdRightImageMap.get(CreatureId.SIXTH_GOURD_ID),
                ImageUrl.gourdRightSelectImageMap.get(CreatureId.SIXTH_GOURD_ID));
    }

    @Override
    public void setCreatureImageView() {
        if (inQAction) {
            if (isControlled()) {
                if (direction == Constant.Direction.LEFT)
                    this.creatureImageView.setImage(imageLeftSelect);
                else
                    this.creatureImageView.setImage(imageRightSelect);
            } else {
                if (direction == Constant.Direction.LEFT)
                    this.creatureImageView.setImage(imageLeft);
                else
                    this.creatureImageView.setImage(imageRight);
            }
        } else {
            super.setCreatureImageView();
        }
    }

    @Override
    //封装移动方式,画,攻击,返回子弹
    public ArrayList<Bullet> update() {
        ArrayList<Bullet> bullets = new ArrayList<>();
        if (!isControlled()) {
            if (isAlive()) {
                aiInterface.moveMod(this, enemyFamily);
                Bullet bullet = aiInterface.aiAttack(this, enemyFamily);
                if (bullet != null)
                    bullets.add(bullet);
                if (inQAction && (double) System.currentTimeMillis() - lastTransfigurationMillis > gap)
                    disposeQAction();
            }
        } else {
            Bullet bullet = playerAttack();
            if (bullet != null)
                bullets.add(bullet);
            if (qFlag && !inQAction) {
                //保证两次技能不重叠
                qAction();
            }
            qFlag = false;
            if (inQAction && (double) System.currentTimeMillis() - lastTransfigurationMillis > gap)
                disposeQAction();
        }
        draw();
        return bullets;
    }


    @Override
    public ArrayList<Bullet> qAction() {
        //你看不到我,颜色变淡,防御力提升,移速提升,攻击变弱
        ArrayList<Bullet> arrayList = new ArrayList<>();
        if (currentMagic < baseMagic)
            return arrayList;
        currentMagic = 0;

        inQAction = true;
        setCreatureImageView();
        currentAttack -= attackDecrement;
        currentMoveSpeed += moveSpeedIncrement;
        currentDefense += defenseIncrement;
        lastTransfigurationMillis = System.currentTimeMillis();
        addState(new CreatureStateWithClock(CreatureState.Q_ACTION,gap));
        return arrayList;
    }

    private void disposeQAction() {
        inQAction = false;
        setCreatureImageView();
        currentMoveSpeed -= moveSpeedIncrement;
        currentAttack += attackDecrement;
        currentDefense -= defenseIncrement;
    }

}
