package com.sjq.gourd.ai;

import com.sjq.gourd.bullet.Bullet;
import com.sjq.gourd.creature.CreatureClass;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class FoolAi implements AiInterface {
    Random random;

    public FoolAi(long seed) {
        random = new Random(seed);
    }

    @Override
    public CreatureClass observe(CreatureClass myCreature, HashMap<Integer, CreatureClass> enemies) {
        if (!myCreature.isAlive())
            return null;
        CreatureClass res = null;
        double minDistance = 1400.0;
        for (CreatureClass creature : enemies.values()) {
            double distance = myCreature.getImagePos().getDistance(creature.getImagePos());
            if (distance < minDistance) {
                res = creature;
                minDistance = distance;
            }
        }
        return res;
    }

    @Override
    public void moveMod(CreatureClass myCreature) {
        int x= random.nextInt();
        myCreature.setDirection(x);
    }

    @Override
    public Bullet aiAttack(CreatureClass myCreature, HashMap<Integer, CreatureClass> enemies) {
        //ai对整个敌人群体的攻击,先观测选取目标,再攻击产生子弹,如果没有攻击,就返回null
        if(!myCreature.isAlive())
            return null;
        if (myCreature.canAttack()) {
            CreatureClass target = observe(myCreature,enemies);
            if (target == null)
                return null;
            if(myCreature.getImagePos().getDistance(target.getImagePos())>myCreature.getShootRange())
                return null;
            Bullet bullet = new Bullet(myCreature, target, myCreature.getCenterPos(), null);
            myCreature.setLastAttackTimeMillis(System.currentTimeMillis());
            return bullet;
        }
        return null;
    }

}
