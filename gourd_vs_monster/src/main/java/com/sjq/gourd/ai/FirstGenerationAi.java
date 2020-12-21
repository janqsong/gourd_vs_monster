package com.sjq.gourd.ai;

import com.sjq.gourd.bullet.Bullet;
import com.sjq.gourd.bullet.BulletState;
import com.sjq.gourd.constant.Constant;
import com.sjq.gourd.constant.CreatureId;
import com.sjq.gourd.creature.Creature;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Random;

public class FirstGenerationAi implements AiInterface {
    Random random;
    private Creature firstPriority = null;
    //对第一优先级追杀时间
    private int timeCount = (int) (Constant.FIRST_GENERATION_AI_COUNT_TIME * 1000 / Constant.FRAME_TIME);
    private double bloodPriority = 0.1, distancePriority = 0.1;

    public FirstGenerationAi(long seed) {
        random = new Random(seed);
    }

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

    @Override
    public void moveMod(Creature myCreature, HashMap<Integer, Creature> enemies) {
        if (firstPriority == null || !firstPriority.isAlive()) {
            firstPriority = observe(myCreature, enemies);
            timeCount = (int) (Constant.FIRST_GENERATION_AI_COUNT_TIME * 1000 / Constant.FRAME_TIME);
            myCreature.setDirection(random.nextInt(5));
            return;
        }
        if (!myCreature.canChangeDirection()) return;

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
            myCreature.setDirection(direction);
            return;
        }

        double distance = myCreature.getImagePos().getDistance(firstPriority.getImagePos());
        double shootRange = myCreature.getShootRange();
        direction = myCreature.getImagePos().getRelativePosFar(firstPriority.getImagePos());
        int rand = random.nextInt(10);

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
    public Bullet aiAttack(Creature myCreature, HashMap<Integer, Creature> enemies) {
        if (myCreature == null || !myCreature.isAlive())
            return null;
        if (myCreature.canAttack()) {
            Creature target = firstPriority;
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
            else {
                if (myCreature.getCreatureId() == CreatureId.GRANDPA_ID) {
                    bullet = new Bullet(myCreature, target, 5, Color.GREEN, BulletState.THE_GOD_OF_HEALING);
                } else if (myCreature.getEquipment() != null && myCreature.getEquipment().getName().equals("magicMirror")) {
                    bullet = new Bullet(myCreature, target, 5, Color.YELLOW, BulletState.GAZE_OF_MAGIC_MIRROR);
                } else {
                    bullet = new Bullet(myCreature, target, myCreature.getCenterPos(), null);
                }
            }
            myCreature.setLastAttackTimeMillis(System.currentTimeMillis());
            return bullet;
        }
        return null;
    }

    private double calculatePriority(Creature myCreature, Creature enemy) {
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

    private int randDirection(int... list) {
        int rand = random.nextInt(list.length);
        return list[rand];
    }
}
