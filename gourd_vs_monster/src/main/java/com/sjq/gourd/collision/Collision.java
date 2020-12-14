package com.sjq.gourd.collision;

import com.sjq.gourd.bullet.Bullet;
import com.sjq.gourd.creature.CreatureClass;

public class Collision {
    private Bullet bullet;

    public Collision(Bullet bullet) {
        this.bullet = bullet;
    }

    public void collisionEvent() {
        CreatureClass targetCreature = bullet.getTargetCreature();
        CreatureClass sourceCreature = bullet.getSourceCreature();

        double targetHealth = targetCreature.getCurrentHealth();
        double sourceAttack = sourceCreature.getCurrentAttack();
        double targetDefense = targetCreature.getCurrentDefense();
        double damage = sourceAttack - targetDefense;
        if (damage < 1)
            damage = 1;
        damage = (double) Math.round(damage);
        targetCreature.setCurrentHealth(targetHealth - damage);
        bullet.setValid(false);
        bullet.setVisible(false);
    }
}