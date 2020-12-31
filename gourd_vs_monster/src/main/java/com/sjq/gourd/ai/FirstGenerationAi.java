package com.sjq.gourd.ai;

import com.sjq.gourd.bullet.Bullet;
import com.sjq.gourd.constant.Constant;
import com.sjq.gourd.constant.CreatureId;
import com.sjq.gourd.creature.Creature;

import java.util.HashMap;
import java.util.Random;

public class FirstGenerationAi implements AiInterface {
    Random random;
    //注意,每个生物都有一个ai,但random的种子必须是不同的,否则伪随机会导致所有ai移动方式相同
    private Creature firstPriority = null;
    //第一优先攻击目标
    private int timeCount = (int) (Constant.FIRST_GENERATION_AI_COUNT_TIME * 1000 / Constant.FRAME_TIME);
    //第一优先攻击目标的锁定时间

    //根据随机种子创建ai,要保证所有生物随机种子不同
    public FirstGenerationAi(long seed) {
        random = new Random(seed);
    }


    //通过观测选取第一优先级目标,如果没有目标,返回null
    @Override
    public Creature observe(Creature myCreature, HashMap<Integer, Creature> enemies) {
        int id = -1;
        double priority = -99999;
        if (myCreature.getCreatureId() == CreatureId.GRANDPA_ID) {
            for (Creature creature : enemies.values()) {
                if (myCreature != creature) {
                    double p = calculatePriority(myCreature, creature);
                    if (p > priority) {
                        id = creature.getCreatureId();
                        priority = p;
                    }
                }
            }
        } else {
            for (Creature creature : enemies.values()) {
                double p = calculatePriority(myCreature, creature);
                if (p > priority) {
                    id = creature.getCreatureId();
                    priority = p;
                }
            }
        }
        if (id == -1)
            return null;
        return enemies.get(id);
    }

    //通过当前位置和第一优先级目标的位置关系选择一个移动方向
    @Override
    public void moveMod(Creature myCreature, HashMap<Integer, Creature> enemies) {
        timeCount--;
        //不能更改方向时,直接返回
        if (!myCreature.canChangeDirection()) return;
        //如果第一优先级没了,更换第一优先级,重置count
        if (firstPriority == null || !firstPriority.isAlive() || timeCount <= 0) {
            firstPriority = observe(myCreature, enemies);
            timeCount = (int) (Constant.FIRST_GENERATION_AI_COUNT_TIME * 1000 / Constant.FRAME_TIME);
            setDirection(myCreature, random.nextInt(5));
            return;
        }

        //如果生物在角落里,就不要继续往角落里走
        int a = Constant.Direction.LEFT, b = Constant.Direction.RIGHT,
                c = Constant.Direction.UP, d = Constant.Direction.DOWN, e = Constant.Direction.STOP;
        boolean left = myCreature.isLeftMost(), right = myCreature.isRightMost(),
                up = myCreature.isHighest(), down = myCreature.isLowest();
        int direction = -1;
        if (left && up) direction = randDirection(b, d, e);
        else if (left && down) direction = randDirection(b, c, e);
        else if (right && up) direction = randDirection(a, d, e);
        else if (right && down) direction = randDirection(a, c, e);
        else if (left) direction = randDirection(b, c, d, e);
        else if (right) direction = randDirection(a, c, d, e);
        else if (up) direction = randDirection(a, b, d, e);
        else if (down) direction = randDirection(a, b, c, e);
        if (direction != -1) {
            setDirection(myCreature, direction);
            return;
        }

        //计算与攻击目标之间的位置关系
        double distance = myCreature.getImagePos().getDistance(firstPriority.getImagePos());
        double shootRange = myCreature.getShootRange();
        //选择x和y方向上较远的方向前进
        direction = myCreature.getImagePos().getRelativePosFar(firstPriority.getImagePos());
        //如果是近战生物,当目标生物很远时,我的生物追上去打,当在0.9倍攻击距离内,不改变移动方向,防止多个ai生物叠在一起越来越近
        if (myCreature.isCloseAttack()) {
            if (distance > 0.9 * myCreature.getShootRange())
                setDirection(myCreature, direction);
            return;
        }

        int rand = random.nextInt(10);
        //近战生物返回,这里都是远程攻击生物
        //当目标生物靠近到0.8倍射程以内,远离这个生物
        if (0.8 * shootRange > distance) {
            //50%反向,50%随机
            if (rand <= 5)
                setDirection(myCreature, oppositeDirection(direction));
            else
                setDirection(myCreature, random.nextInt(5));
        } else {
            //否则50%靠近,50%随机
            if (rand <= 5)
                setDirection(myCreature, direction);
            else
                setDirection(myCreature, random.nextInt(5));
        }
    }

    //根据生物当前状态攻击
    @Override
    public Bullet aiAttack(Creature myCreature, HashMap<Integer, Creature> enemies) {
        //如果我的生物死了或者为null,不攻击
        if (myCreature == null || !myCreature.isAlive())
            return null;
        //如果没有达到攻击时间,不攻击
        if (myCreature.canAttack()) {
            Creature target = firstPriority;
            //目标为空,目标死亡,目标锁定时间结束,都会重新选取攻击目标
            if (timeCount <= 0 || target == null || !target.isAlive()) {
                target = observe(myCreature, enemies);
                firstPriority = target;
                timeCount = (int) (Constant.FIRST_GENERATION_AI_COUNT_TIME * 1000 / Constant.FRAME_TIME);
            }
            //重选目标后目标为空,则直接返回
            if (target == null)
                return null;
            //当射程不够,直接返回
            if (myCreature.getImagePos().getDistance(target.getImagePos()) > myCreature.getShootRange())
                return null;
            //否则直接可以攻击,产生一颗子弹
            Bullet bullet = myCreature.selectBullet(target);
            myCreature.setLastAttackTimeMillis(System.currentTimeMillis());
            return bullet;
        }
        return null;
    }

    //根据生物与目标生物的位置关系以及攻击防御生命值的关系返回优先级,数字越大,优先级越大
    private double calculatePriority(Creature myCreature, Creature enemy) {
        //敌人死了,优先级最低
        if (enemy == null || !enemy.isAlive())
            return -9999999;
        //血量优先级系数,和距离优先级系数
        double bloodPriorityCoefficient = 0.1, disPriorityCoefficient = 0.1;
        //最终的表示优先级的数字
        double answer = 0.0;
        double distance = myCreature.getImagePos().getDistance(enemy.getImagePos());
        double currentMoveSpeed = myCreature.getCurrentMoveSpeed();
        double shootRange = myCreature.getShootRange();
        int timeToAttack = 0;
        //避免除零错误
        if (currentMoveSpeed != 0)
            timeToAttack = (int) ((int) (distance - shootRange) / currentMoveSpeed);

        //timeToAttack表征的是需要移动多少帧才能够刚好打得到,越容易打到,距离系数越大
        if (timeToAttack <= 0)
            disPriorityCoefficient = 1024;//直接可以打
        else if (timeToAttack <= 5)
            disPriorityCoefficient = 512;
        else if (timeToAttack <= 10)
            disPriorityCoefficient = 256;
        else if (timeToAttack <= 15)
            disPriorityCoefficient = 128;
        else if (timeToAttack <= 20)
            disPriorityCoefficient = 64;
        else
            disPriorityCoefficient = 1;

        //用1400-distance来使得此项系数与距离负相关
        answer += disPriorityCoefficient * (1400 - distance);
        double curAttack = myCreature.getCurrentAttack();
        double curDefense = enemy.getCurrentDefense();
        double curHealth = enemy.getCurrentHealth();
        int attackNum = 0;
        //避免除0错误
        if (curAttack - curDefense != 0)
            attackNum = (int) (curHealth / (curAttack - curDefense));
        if (attackNum <= 0)
            bloodPriorityCoefficient = 0;//说明打人不掉血,或强制掉一滴血,系数最小
        else if (attackNum <= 3)
            bloodPriorityCoefficient = 1024;
        else if (attackNum <= 6)
            bloodPriorityCoefficient = 512;
        else if (attackNum <= 9)
            bloodPriorityCoefficient = 256;
        else if (attackNum <= 12)
            bloodPriorityCoefficient = 128;
        else if (attackNum <= 15)
            bloodPriorityCoefficient = 64;
        else
            bloodPriorityCoefficient = 1;
        //这一项与当前血量负相关
        answer += bloodPriorityCoefficient * (enemy.getBaseHealth() - enemy.getCurrentHealth());

        return answer;
    }

    //只在list这个列表中随机选一个
    private int randDirection(int... list) {
        int rand = random.nextInt(list.length);
        return list[rand];
    }


    //在AI中的改变方向必须调用这个方法,防止抖动
    private void setDirection(Creature myCreature, int direction) {
        if (myCreature.canChangeDirection())
            myCreature.setDirection(direction);
    }

    //根据方向返回反方向
    private int oppositeDirection(int direction) {
        switch (direction) {
            case Constant.Direction.LEFT:
                return Constant.Direction.RIGHT;
            case Constant.Direction.RIGHT:
                return Constant.Direction.LEFT;
            case Constant.Direction.UP:
                return Constant.Direction.DOWN;
            case Constant.Direction.DOWN:
                return Constant.Direction.UP;
            case Constant.Direction.STOP:
                return Constant.Direction.STOP;
            default:
                return random.nextInt(5);
        }
    }
}
