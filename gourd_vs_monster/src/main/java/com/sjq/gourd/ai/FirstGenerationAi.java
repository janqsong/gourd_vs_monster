package com.sjq.gourd.ai;

import com.sjq.gourd.bullet.Bullet;
import com.sjq.gourd.constant.Constant;
import com.sjq.gourd.creature.CreatureClass;

import java.util.HashMap;
import java.util.Random;

public class FirstGenerationAi implements AiInterface {
    Random random;
    private CreatureClass firstPriority = null;
    //对第一优先级追杀时间
    private int timeCount = (int) (Constant.FIRST_GENERATION_AI_COUNT_TIME * 1000 / Constant.FRAME_TIME);
    private double bloodPriority = 0.1, distancePriority = 0.1;

    public FirstGenerationAi(long seed) {
        random = new Random(seed);
    }

    @Override
    public CreatureClass observe(CreatureClass myCreature, HashMap<Integer, CreatureClass> enemies) {
        int id = -1;
        double priority = -99999;
        for (CreatureClass creature : enemies.values()) {
            double p = calculatePriority(myCreature, creature);
            if (p > priority) {
                id = creature.getCreatureId();
                priority = p;
            }
        }
        if (id == -1)
            return null;
        return enemies.get(id);
    }

    @Override
    public void moveMod(CreatureClass myCreature) {
        if (firstPriority == null || !firstPriority.isAlive()) {
            firstPriority = observe(myCreature, myCreature.getEnemyFamily());
            timeCount = (int) (Constant.FIRST_GENERATION_AI_COUNT_TIME * 1000 / Constant.FRAME_TIME);
            myCreature.setDirection(random.nextInt(5));
            return;
        }

        double maxX = Constant.FIGHT_PANE_WIDTH - myCreature.getImageWidth();
        double maxY = Constant.FIGHT_PANE_HEIGHT - myCreature.getImageHeight();
        double posX = myCreature.getImagePos().getLayoutX();
        double posY = myCreature.getImagePos().getLayoutY();
        int rand = random.nextInt(5);
        if (posX == 0 && posY == 0) {
            while (rand == Constant.Direction.LEFT || rand == Constant.Direction.UP)
                rand = random.nextInt(5);
            myCreature.setDirection(rand);
            return;
        } else if (posX == 0 && posY == maxY) {
            while (rand == Constant.Direction.LEFT || rand == Constant.Direction.DOWN)
                rand = random.nextInt(5);
            myCreature.setDirection(rand);
            return;
        } else if (posX == maxX && posY == 0) {
            while (rand == Constant.Direction.RIGHT || rand == Constant.Direction.UP)
                rand = random.nextInt(5);
            myCreature.setDirection(rand);
            return;
        } else if (posX == maxX && posY == maxY) {
            while (rand == Constant.Direction.RIGHT || rand == Constant.Direction.DOWN)
                rand = random.nextInt(5);
            myCreature.setDirection(rand);
            return;
        }

        double distance = myCreature.getImagePos().getDistance(firstPriority.getImagePos());
        double shootRange = myCreature.getShootRange();
        int direction = myCreature.getImagePos().getRelativePosFar(firstPriority.getImagePos());
        rand = random.nextInt(10);

        if (myCreature.isCloseAttack()) {
            if (distance > 0.9 * myCreature.getShootRange())
                myCreature.setDirection(direction);
            return;
        }

        if (0.8 * shootRange > distance) {
            //反向
            if (rand <= 5)
                if (direction % 2 == 0)
                    myCreature.setDirection((direction + 1) % 5);
                else
                    myCreature.setDirection((direction - 1) % 5);
            else
                myCreature.setDirection(random.nextInt(5));
        } else {
            if (rand <= 5)
                myCreature.setDirection(direction);
            else
                myCreature.setDirection(random.nextInt());
        }
        timeCount--;
    }

    @Override
    public Bullet aiAttack(CreatureClass myCreature, HashMap<Integer, CreatureClass> enemies) {
        if (myCreature == null || !myCreature.isAlive())
            return null;
        if (myCreature.canAttack()) {
            CreatureClass target = firstPriority;
            if (timeCount <= 0 || target == null || !target.isAlive()) {
                target = observe(myCreature, enemies);
                firstPriority = target;
                timeCount = (int) (Constant.FIRST_GENERATION_AI_COUNT_TIME * 1000 / Constant.FRAME_TIME);
            }
            if (target == null)
                return null;
            if (myCreature.getImagePos().getDistance(target.getImagePos()) > myCreature.getShootRange())
                return null;
            Bullet bullet = null;


            if (myCreature.isCloseAttack())
                bullet = new Bullet(myCreature, target, myCreature.getCenterPos());
            else
                bullet = new Bullet(myCreature, target, myCreature.getCenterPos(), null);
            myCreature.setLastAttackTimeMillis(System.currentTimeMillis());
            return bullet;
        }
        return null;
    }

    private double calculatePriority(CreatureClass myCreature, CreatureClass enemy) {
        if (enemy == null || !enemy.isAlive())
            return -9999999;
        double bloodPriorityCoefficient = 0.1, disPriorityCoefficient = 0.1;
        double answer = 0.0;
        double distance = myCreature.getImagePos().getDistance(enemy.getImagePos());
        double currentMoveSpeed = myCreature.getCurrentMoveSpeed();
        double shootRange = myCreature.getShootRange();
        int timeToAttack = (int) ((int) (distance - shootRange) / currentMoveSpeed);
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

        answer += disPriorityCoefficient * (1400 - distance);
        double curAttack = myCreature.getCurrentAttack();
        double curDefense = enemy.getCurrentDefense();
        double curHealth = enemy.getCurrentHealth();
        int attackNum = (int) (curHealth / (curAttack - curDefense));
        if (attackNum <= 0)
            bloodPriorityCoefficient = 0;
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
        answer += bloodPriorityCoefficient * (enemy.getBaseHealth() - enemy.getCurrentHealth());

        return answer;
    }
}
