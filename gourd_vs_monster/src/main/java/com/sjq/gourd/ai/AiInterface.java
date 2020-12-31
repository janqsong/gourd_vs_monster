package com.sjq.gourd.ai;

import com.sjq.gourd.bullet.Bullet;
import com.sjq.gourd.creature.Creature;

import java.util.HashMap;
import java.util.List;

//ai接口,需要实现观测,移动,攻击
public interface AiInterface {
    //观测
    public Creature observe(Creature myCreature, HashMap<Integer,Creature> enemies);
    //移动方式
    public void moveMod(Creature myCreature, HashMap<Integer,Creature> enemies);
    //攻击模式
    public Bullet aiAttack(Creature myCreature, HashMap<Integer,Creature> enemies);
}
