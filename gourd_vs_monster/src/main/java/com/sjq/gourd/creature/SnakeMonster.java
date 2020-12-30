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

public class SnakeMonster extends Creature {

    private boolean inQAction = false, inEAction = false, inRAction = false;
    private long lastQActionMillis = 0, qGap = 5000;
    private long lastEActionMillis = 0, eGap = 5000;
    private long lastRActionMillis = 0, rGap = 5000;
    private final double healthIncrement = 1000;
    private final double defenseIncrement = 30;

    public SnakeMonster(int faceDirection, ImageView imageView, ImageView closeAttackImageView) {
        super(Constant.CampType.MONSTER, CreatureId.SNAKE_MONSTER_ID, CreatureId.SNAKE_MONSTER_NAME,
                5000, 200, 120, 30, 0.7, 15, 500.0,
                faceDirection, 100.0, false, Constant.ClawType.NONE_CLAW,
                imageView, closeAttackImageView, ImageUrl.monsterLeftImageMap.get(CreatureId.SNAKE_MONSTER_ID),
                ImageUrl.monsterLeftSelectImageMap.get(CreatureId.SNAKE_MONSTER_ID),
                ImageUrl.monsterRightImageMap.get(CreatureId.SNAKE_MONSTER_ID),
                ImageUrl.monsterRightSelectImageMap.get(CreatureId.SNAKE_MONSTER_ID));
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
                updateActionState();
            } else {
                draw();
            }
        } else {
            draw();
            Bullet bullet = playerAttack();
            if (bullet != null)
                bullets.add(bullet);
            if (qFlag && !inQAction) {
                ArrayList<Bullet> bullets1 = qAction();
                if (bullets1.size() > 0)
                    bullets.addAll(bullets1);
            }
            qFlag = false;
            if (eFlag && !inEAction) {
                eAction();
            }
            eFlag = false;
            if (rFlag && !inRAction) {
                rAction();
            }
            rFlag = false;
        }
        updateActionState();
        return bullets;
    }

    private void updateActionState() {
        long sys = System.currentTimeMillis();
        if (inQAction && sys - lastQActionMillis > qGap)
            disposeQAction();
        if (inEAction && sys - lastQActionMillis > eGap)
            disposeEAction();
        if (inRAction && sys - lastQActionMillis > rGap)
            disposeRAction();
    }

    @Override
    public ArrayList<Bullet> qAction() {
        //剧毒之牙,给2.0范围内所有的敌人发射一颗带有 剧毒之牙 效果的子弹
        ArrayList<Bullet> arrayList = new ArrayList<>();
        if (currentMagic < 0.5 * baseMagic)
            return arrayList;
        //消耗蓝量50%
        setCurrentMagic(currentMagic - 0.5 * baseMagic);
        inQAction = true;
        for (Creature creature : enemyFamily.values()) {
            if (this.imagePosition.getDistance(creature.imagePosition) < 2.0 * shootRange)
                arrayList.add(new Bullet(this, creature, 10, Color.BLACK, BulletState.THE_TEETH_OF_POISONOUS));
        }
        lastQActionMillis = System.currentTimeMillis();
        return arrayList;
    }

    @Override
    public void eAction() {
        //复活之术,如果己方有妖精已经死掉了,复活一只妖精,优先复活蝎子精,蝎子精复活只有1/2基础血量,其他复活满血
        //如果没怪死,这个技能仍旧扣蓝量,属于误操作,相当于空大
        if (currentMagic < baseMagic)
            return;
        currentMagic = 0;
        inEAction = true;
        for (Creature creature : myFamily.values()) {
            if (creature != this && !creature.isAlive()) {
                if (creature.getCreatureId() == CreatureId.SCORPION_MONSTER_ID) {
                    creature.setCurrentHealth(0.5 * creature.getBaseHealth());
                } else {
                    creature.setCurrentHealth(creature.getBaseHealth());
                }
                creature.setCurrentMagic(0);
                return;
            }
        }
        return;
    }

    @Override
    public void rAction() {
        //金身护罩,5s中瞬间加血1000,防御力加30
        if (currentMagic < 0.5 * baseMagic)
            return;
        setCurrentMagic(currentMagic - 0.5 * baseMagic);
        inRAction = true;
        setCurrentHealth(currentHealth + healthIncrement);
        setCurrentDefense(currentDefense + defenseIncrement);
    }

    private void disposeQAction() {
        inQAction = false;
    }

    private void disposeEAction() {
        inEAction = false;
    }

    private void disposeRAction() {
        setCurrentDefense(currentDefense - defenseIncrement);
        inRAction = false;
    }
}
