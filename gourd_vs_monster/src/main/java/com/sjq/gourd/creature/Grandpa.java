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

public class Grandpa extends Creature {

    public Grandpa(DataInputStream in, DataOutputStream out, int faceDirection, ImageView imageView, ImageView closeAttackImageView) {
        super(in, out, Constant.CampType.GOURD, CreatureId.GRANDPA_ID, CreatureId.GRANDPA_NAME,
                2000, 200, 75, 10, 0.5, 12, 300.0,
                faceDirection, 70.0, false, Constant.ClawType.NONE_CLAW,
                imageView, closeAttackImageView, ImageUrl.gourdLeftImageMap.get(CreatureId.GRANDPA_ID),
                ImageUrl.gourdLeftSelectImageMap.get(CreatureId.GRANDPA_ID),
                ImageUrl.gourdRightImageMap.get(CreatureId.GRANDPA_ID),
                ImageUrl.gourdRightSelectImageMap.get(CreatureId.GRANDPA_ID));
    }


    @Override
    public ArrayList<Bullet> update() {
        ArrayList<Bullet> bullets = new ArrayList<>();
        if (!isControlled()) {
            if (isAlive()) {
                setCreatureState();
                aiInterface.moveMod(this, myFamily);
                draw();
                Bullet bullet = aiInterface.aiAttack(this, myFamily);
                if (bullet != null) {
                    bullets.add(bullet);
                    System.out.println("ai爷爷开炮了,目标是" + bullet.getTargetCreature().getCreatureName());
                }
            } else {
                draw();
            }
        } else {
            setCreatureState();
            draw();
            Bullet bullet = playerAttack();
            if (bullet != null)
                bullets.add(bullet);
        }
        return bullets;
    }

    @Override
    protected Bullet playerAttack() {
        if (playerAttackTarget == null)
            return null;
        if (campType.equals(playerAttackTarget.campType)) {
            if (!isAlive())
                return null;
            if (playerAttackTarget == null)
                return null;
            if (!playerAttackTarget.isAlive())
                return null;
            if (!canAttack())
                return null;
            if (getImagePos().getDistance(playerAttackTarget.getImagePos()) > shootRange)
                return null;
            setLastAttackTimeMillis(System.currentTimeMillis());
            return new Bullet(this, playerAttackTarget, 5, Color.GREEN, BulletState.THE_GOD_OF_HEALING);
        }
        return null;
    }
}
