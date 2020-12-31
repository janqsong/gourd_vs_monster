package com.sjq.gourd.creature;

import com.sjq.gourd.bullet.Bullet;
import com.sjq.gourd.bullet.BulletState;
import com.sjq.gourd.constant.Constant;
import com.sjq.gourd.constant.CreatureId;
import com.sjq.gourd.constant.ImageUrl;
import javafx.scene.image.ImageView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;

public class ScorpionMonster extends Creature {

    private boolean inQAction = false, inEAction = false, inRAction = false;
    private long lastQActionMillis = 0, qGap = 5000;
    private long lastEActionMillis = 0, eGap = 5000;
    private long lastRActionMillis = 0, rGap = 10000;
    private final double moveSpeedIncrement = 10;
    private final double defenseIncrement = 20;
    private final double attackIncrement = 30;
    private final double attackSpeedIncrement = 0.5;
    private final double shootRangeIncrement = 80.0;
    private final double healthIncrement = 600;
    private Creature[] creatures = new Creature[3];

    public ScorpionMonster(DataInputStream in, DataOutputStream out, int faceDirection, ImageView imageView, ImageView closeAttackImageView) {
        super(in, out, Constant.CampType.MONSTER, CreatureId.SCORPION_MONSTER_ID, CreatureId.SCORPION_MONSTER_NAME,
                7500, 150, 150, 55, 0.5, 10, 100.0,
                faceDirection, 110.0, true, Constant.ClawType.THIRD_CLAW,
                imageView, closeAttackImageView, ImageUrl.monsterLeftImageMap.get(CreatureId.SCORPION_MONSTER_ID),
                ImageUrl.monsterLeftSelectImageMap.get(CreatureId.SCORPION_MONSTER_ID),
                ImageUrl.monsterRightImageMap.get(CreatureId.SCORPION_MONSTER_ID),
                ImageUrl.monsterRightSelectImageMap.get(CreatureId.SCORPION_MONSTER_ID));
        creatures[0] = null;
        creatures[1] = null;
        creatures[2] = null;
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

    @Override
    //test
    public ArrayList<Bullet> updateTest() {
        ArrayList<Bullet> bullets = new ArrayList<>();
        if (!isControlled()) {
            if (isAlive()) {
//                setCreatureState();这东西在move里更新就能保证
                aiInterface.moveMod(this, enemyFamily);
                draw();
                Bullet bullet = aiInterface.aiAttack(this, enemyFamily);
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
            updateActionState();
        }
        return bullets;
    }

    private void updateActionState() {
        long sys = System.currentTimeMillis();
        if (inQAction && sys - lastQActionMillis > qGap)
            disposeQAction();
        if (inEAction && sys - lastEActionMillis > eGap)
            disposeEAction();
        if (inRAction && sys - lastRActionMillis > rGap)
            disposeRAction();
    }

    @Override
    public ArrayList<Bullet> qAction() {
        //致残之爪,给5.0近战攻击范围内所有的敌人发射一颗具有致残效果的子弹
        ArrayList<Bullet> arrayList = new ArrayList<>();
        if (currentMagic < 0.5 * baseMagic)
            return arrayList;
        inQAction = true;
        for (Creature creature : enemyFamily.values()) {
            if (this.imagePosition.getDistance(creature.imagePosition) < 5.0 * shootRange) {
                arrayList.add(new Bullet(this, creature, BulletState.SPEED_CUT_CLAW, Constant.CLOSE_BULLET_TYPE));
            }
        }
        setCurrentMagic(0);
        lastQActionMillis = System.currentTimeMillis();
        return arrayList;
    }

    @Override
    public void eAction() {
        //狂暴之心,移速加快,攻击力加强,防御力提高,加一定攻速,加一定血量
        if (currentMagic < 0.5 * baseMagic)
            return;
        setCurrentMagic(currentMagic - 0.5 * baseMagic);
        inEAction = true;
        setCurrentHealth(currentHealth + healthIncrement);
        setCurrentAttack(currentAttack + attackIncrement);
        setCurrentDefense(currentDefense + defenseIncrement);
        setCurrentAttackSpeed(currentAttackSpeed + attackSpeedIncrement);
        setShootRange(shootRange + shootRangeIncrement);
        setCurrentMoveSpeed(currentMoveSpeed + moveSpeedIncrement);
        lastEActionMillis = System.currentTimeMillis();
    }

    @Override
    public void rAction() {
        //同命, 将血量对面三人绑定,己方扣血则绑定三人扣血,持续10s
        if (currentMagic < baseMagic)
            return;
        currentMagic = 0;
        inRAction = true;
        selectThree();
        lastRActionMillis = System.currentTimeMillis();
    }

    @Override
    public void setCurrentHealth(double healthVal) {
        double delta = healthVal - currentHealth;
        if (delta < 0 && inRAction) {
            for (int i = 0; i < 3; i++) {
                if (creatures[i] != null && creatures[i].isAlive())
                    creatures[i].setCurrentHealth(creatures[i].getCurrentHealth() - delta);
            }
        } else
            super.setCurrentHealth(healthVal);
    }

    private void disposeQAction() {
        inQAction = false;
    }

    private void disposeEAction() {
        inEAction = false;
        setCurrentAttack(currentAttack - attackIncrement);
        setCurrentDefense(currentDefense - defenseIncrement);
        setCurrentMoveSpeed(currentMoveSpeed - moveSpeedIncrement);
        setCurrentAttackSpeed(currentAttackSpeed - attackSpeedIncrement);
        setShootRange(shootRange - shootRangeIncrement);
    }

    private void disposeRAction() {
        creatures[0] = null;
        creatures[1] = null;
        creatures[2] = null;
        inRAction = false;
    }

    private void selectThree() {
        for (Creature creature : enemyFamily.values()) {
            if (creature.isAlive())
                addCreatures(creature);
        }
    }

    private void addCreatures(Creature creature) {
        double a = 99999999, b = 99999999, c = 99999999;
        if (creatures[0] != null) a = creatures[0].getCurrentHealth();
        if (creatures[1] != null) b = creatures[1].getCurrentHealth();
        if (creatures[2] != null) c = creatures[2].getCurrentHealth();
        double health = creature.getCurrentHealth();
        if (health < a) {
            creatures[2] = creatures[1];
            creatures[1] = creatures[0];
            creatures[0] = creature;
        } else if (health < b) {
            creatures[2] = creatures[1];
            creatures[1] = creature;
        } else if (health < c) {
            creatures[2] = creature;
        }
    }
}