package com.sjq.gourd.creature;

import com.sjq.gourd.bullet.Bullet;
import com.sjq.gourd.constant.Constant;
import com.sjq.gourd.constant.CreatureId;
import com.sjq.gourd.constant.ImageUrl;
import javafx.scene.image.ImageView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;

public class FirstGourd extends Creature {

    private long lastTransfigurationMillis;
    private boolean inQAction = false;
    private double moveSpeedIncrement = 5;
    private double attackDecrement = 20;
    private final long gap = 5000; //5000ms时间

    public FirstGourd(DataInputStream in, DataOutputStream out, int faceDirection, ImageView imageView, ImageView closeAttackImageView) {
        super(in, out, Constant.CampType.GOURD, CreatureId.FIRST_GOURD_ID, CreatureId.FIRST_GOURD_NAME,
                3500, 100, 120, 40, 0.5, 10, 80.0,
                faceDirection, 100.0, true, Constant.ClawType.FIRST_CLAW,
                imageView, closeAttackImageView, ImageUrl.gourdLeftImageMap.get(CreatureId.FIRST_GOURD_ID),
                ImageUrl.gourdLeftSelectImageMap.get(CreatureId.FIRST_GOURD_ID),
                ImageUrl.gourdRightImageMap.get(CreatureId.FIRST_GOURD_ID),
                ImageUrl.gourdRightSelectImageMap.get(CreatureId.FIRST_GOURD_ID));
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
                if (inQAction && (double) System.currentTimeMillis() - lastTransfigurationMillis > gap)
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
                qFlag = false;
            }
            if (inQAction && (double) System.currentTimeMillis() - lastTransfigurationMillis > gap)
                disposeQAction();
        }
        return bullets;
    }


    @Override
    public ArrayList<Bullet> qAction() {
        //大丈夫能屈能伸,变小一段时间,增加移速,减少攻击力
        ArrayList<Bullet> arrayList = new ArrayList<>();
        if (currentMagic < baseMagic)
            return arrayList;
        currentMagic = 0;
        WIDTH = 0.5 * WIDTH;
        HEIGHT = 0.5 * HEIGHT;
        creatureImageView.setFitWidth(WIDTH);
        creatureImageView.setFitHeight(HEIGHT);
        inQAction = true;
        currentAttack -= attackDecrement;
        currentMoveSpeed += moveSpeedIncrement;
        lastTransfigurationMillis = System.currentTimeMillis();
        return arrayList;
    }

    private void disposeQAction() {
        WIDTH = 2 * WIDTH;
        HEIGHT = 2 * HEIGHT;
        creatureImageView.setFitWidth(WIDTH);
        creatureImageView.setFitHeight(HEIGHT);
        inQAction = false;
        currentMoveSpeed -= moveSpeedIncrement;
        currentAttack += attackDecrement;
    }
}
