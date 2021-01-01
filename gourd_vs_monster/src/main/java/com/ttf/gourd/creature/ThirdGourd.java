package com.ttf.gourd.creature;

import com.ttf.gourd.bullet.Bullet;
import com.ttf.gourd.constant.Constant;
import com.ttf.gourd.constant.CreatureId;
import com.ttf.gourd.constant.ImageUrl;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public class ThirdGourd extends Creature {

    private boolean inQAction = false;
    private long gap = 5000;//ms
    private final double attackIncrement = 50;
    private final double defenseIncrement = 100;
    private double moveSpeedDecrement;//基础移速的20%,如果减到0就是当前移速
    private double lastQActionMillis;//上次Q技能的时间

    ThirdGourd(int faceDirection, ImageView imageView, ImageView closeAttackImageView) {
        super(Constant.CampType.GOURD, CreatureId.THIRD_GOURD_ID, CreatureId.THIRD_GOURD_NAME,
                4500, 100, 150, 60, 0.4, 10, 80.0,
                faceDirection, 70.0, true, Constant.ClawType.FIRST_CLAW,
                imageView, closeAttackImageView, ImageUrl.gourdLeftImageMap.get(CreatureId.THIRD_GOURD_ID),
                ImageUrl.gourdLeftSelectImageMap.get(CreatureId.THIRD_GOURD_ID),
                ImageUrl.gourdRightImageMap.get(CreatureId.THIRD_GOURD_ID),
                ImageUrl.gourdRightSelectImageMap.get(CreatureId.THIRD_GOURD_ID));
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
                if (inQAction && (double) System.currentTimeMillis() - lastQActionMillis > gap)
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
            if (inQAction && (double) System.currentTimeMillis() - lastQActionMillis > gap)
                disposeQAction();
        }
        draw();
        return bullets;
    }


    @Override
    public ArrayList<Bullet> qAction() {
        //无敌金身 大幅提高防御力和攻击力,持续5s
        ArrayList<Bullet> arrayList = new ArrayList<>();
        if (currentMagic < baseMagic)
            return arrayList;
        currentMagic = 0;

        inQAction = true;
        currentAttack += attackIncrement;
        currentDefense += defenseIncrement;
        double speed = currentMoveSpeed;
        currentMoveSpeed -= 3;
        if (currentMoveSpeed < 0) {
            currentMoveSpeed = 0;
            moveSpeedDecrement = speed;
        } else
            moveSpeedDecrement = 3;
        lastQActionMillis = System.currentTimeMillis();
        addState(new CreatureStateWithClock(CreatureState.Q_ACTION,gap));
        return arrayList;
    }

    private void disposeQAction() {
        inQAction = false;
        currentAttack -= attackIncrement;
        currentDefense -= defenseIncrement;
        currentMoveSpeed += moveSpeedDecrement;
    }
}
