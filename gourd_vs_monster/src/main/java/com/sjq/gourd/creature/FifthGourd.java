package com.sjq.gourd.creature;

import com.sjq.gourd.bullet.Bullet;
import com.sjq.gourd.bullet.BulletState;
import com.sjq.gourd.constant.Constant;
import com.sjq.gourd.constant.CreatureId;
import com.sjq.gourd.constant.ImageUrl;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;

public class FifthGourd extends Creature {

    public FifthGourd(int faceDirection, ImageView imageView, ImageView closeAttackImageView) {
        super(Constant.CampType.GOURD, CreatureId.FIFTH_GOURD_ID, CreatureId.FIFTH_GOURD_NAME,
                3000, 150, 80, 40, 0.5, 12, 400.0,
                faceDirection, 70.0, false, Constant.ClawType.NONE_CLAW,
                imageView, closeAttackImageView, ImageUrl.gourdLeftImageMap.get(CreatureId.FIFTH_GOURD_ID),
                ImageUrl.gourdLeftSelectImageMap.get(CreatureId.FIFTH_GOURD_ID),
                ImageUrl.gourdRightImageMap.get(CreatureId.FIFTH_GOURD_ID),
                ImageUrl.gourdRightSelectImageMap.get(CreatureId.FIFTH_GOURD_ID));
    }

    @Override
    public ArrayList<Bullet> qAction() {
        //冰霜之神的降临,给1.5射程范围内的所有敌军发射一颗带有冰霜之心特效的子弹
        ArrayList<Bullet> bullets = new ArrayList<>();
        if (currentMagic < baseMagic)
            return bullets;
        currentMagic = 0;
        for (Creature creature : enemyFamily.values()) {
            if (this.imagePosition.getDistance(creature.imagePosition) < 1.5 * shootRange)
                bullets.add(new Bullet(this, creature, 10, Color.BLUE, BulletState.THE_HEART_OF_ICE));
        }
        return bullets;
    }
}
