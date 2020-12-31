package com.sjq.gourd.creature;

import com.sjq.gourd.bullet.Bullet;
import com.sjq.gourd.constant.Constant;
import com.sjq.gourd.constant.CreatureId;
import com.sjq.gourd.constant.ImageUrl;
import javafx.scene.image.ImageView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;

public class SeventhGourd extends Creature {

    private long lastQAction = 0;
    private boolean inQAction = false;
    private long gap = 5000;
    private final double shootRangeIncrement = 400.0;

    public SeventhGourd(int faceDirection, ImageView imageView, ImageView closeAttackImageView) {
        super(Constant.CampType.GOURD, CreatureId.SEVENTH_GOURD_ID, CreatureId.SEVENTH_GOURD_NAME,
                2500, 100, 80, 20, 0.9, 15, 600.0,
                faceDirection, 70.0, false, Constant.ClawType.NONE_CLAW,
                imageView, closeAttackImageView, ImageUrl.gourdLeftImageMap.get(CreatureId.SEVENTH_GOURD_ID),
                ImageUrl.gourdLeftSelectImageMap.get(CreatureId.SEVENTH_GOURD_ID),
                ImageUrl.gourdRightImageMap.get(CreatureId.SEVENTH_GOURD_ID),
                ImageUrl.gourdRightSelectImageMap.get(CreatureId.SEVENTH_GOURD_ID));
    }


    @Override
    //封装移动方式,画,攻击,返回子弹
    public ArrayList<Bullet> update() {
        ArrayList<Bullet> bullets = new ArrayList<>();
        if (!isControlled()) {
            if (isAlive()) {
//                setCreatureState();这东西在move里更新就能保证
                aiInterface.moveMod(this, enemyFamily);
                draw();
                Bullet bullet = aiInterface.aiAttack(this, enemyFamily);
                if (bullet != null)
                    bullets.add(bullet);
                if (inQAction && (double) System.currentTimeMillis() - lastQAction > gap)
                    disposeQAction();
            } else {
                draw();
            }
        } else {
            draw();
            Bullet bullet = playerAttack();
            if (bullet != null)
                bullets.add(bullet);
            if (qFlag && !inQAction) {
                //保证两次技能不重叠
                qAction();
            }
            qFlag = false;
            if (inQAction && System.currentTimeMillis() - lastQAction > gap)
                disposeQAction();
        }
        return bullets;
    }


    @Override
    public ArrayList<Bullet> qAction() {
        //看我法宝,攻速5s内攻速提升基础攻速的300%,射程提升400.0
        ArrayList<Bullet> bullets = new ArrayList<>();
        if (currentMagic < baseMagic)
            return bullets;

        inQAction = true;
        currentMagic = 0;
        currentAttackSpeed += 3.0 * baseAttackSpeed;
        shootRange += shootRangeIncrement;
        lastQAction = System.currentTimeMillis();
        addState(new CreatureStateWithClock(CreatureState.Q_ACTION,gap));
        return bullets;
    }


    private void disposeQAction() {
        inQAction = false;
        currentAttackSpeed -= 3.0 * baseAttackSpeed;
        if (currentAttackSpeed <= 0)
            currentAttackSpeed = 0.01;
        shootRange -= shootRangeIncrement;
    }
}
