package com.sjq.gourd.ai;

import com.sjq.gourd.bullet.Bullet;
import com.sjq.gourd.creature.CreatureClass;

import java.util.HashMap;
import java.util.List;

public interface AiInterface {
    //观测
    public CreatureClass observe(CreatureClass myCreature, HashMap<Integer,CreatureClass> enemies);
    //移动方式
    public void moveMod(CreatureClass myCreature);
    //攻击模式
    public Bullet aiAttack(CreatureClass myCreature, HashMap<Integer,CreatureClass> enemies);
}
