package com.sjq.gourd.ai;

import com.sjq.gourd.bullet.Bullet;
import com.sjq.gourd.creature.Creature;

import java.util.HashMap;
import java.util.Random;

public class FoolAi implements AiInterface {
    Random random;

    public FoolAi(long seed) {
        random = new Random(seed);
    }

    //直接找最近的攻击
    @Override
    public Creature observe(Creature myCreature, HashMap<Integer, Creature> enemies) {
        if (!myCreature.isAlive())
            return null;
        Creature res = null;
        double minDistance = 1400.0;
        for (Creature creature : enemies.values()) {
            double distance = myCreature.getImagePos().getDistance(creature.getImagePos());
            if (distance < minDistance) {
                res = creature;
                minDistance = distance;
            }
        }
        return res;
    }

    //完全随机移动
    @Override
    public void moveMod(Creature myCreature, HashMap<Integer, Creature> enemies) {
        int x = random.nextInt();
        myCreature.setDirection(x);
    }


    //直接找最近的攻击
    @Override
    public Bullet aiAttack(Creature myCreature, HashMap<Integer, Creature> enemies) {
        //ai对整个敌人群体的攻击,先观测选取目标,再攻击产生子弹,如果没有攻击,就返回null
        if (!myCreature.isAlive())
            return null;
        if (myCreature.canAttack()) {
            Creature target = observe(myCreature, enemies);
            if (target == null)
                return null;
            if (myCreature.getImagePos().getDistance(target.getImagePos()) > myCreature.getShootRange())
                return null;
            Bullet bullet = myCreature.selectBullet(target);
            myCreature.setLastAttackTimeMillis(System.currentTimeMillis());
            return bullet;
        }
        return null;
    }

}
