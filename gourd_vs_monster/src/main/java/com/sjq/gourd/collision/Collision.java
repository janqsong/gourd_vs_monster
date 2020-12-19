package com.sjq.gourd.collision;

import com.sjq.gourd.bullet.Bullet;
import com.sjq.gourd.constant.Constant;
import com.sjq.gourd.constant.ImageUrl;
import com.sjq.gourd.creature.Creature;

public class Collision {
    private Bullet bullet;
    public Collision(Bullet bullet) {
        this.bullet = bullet;
    }

    public void collisionEvent() {
        Creature targetCreature = bullet.getTargetCreature();
        Creature sourceCreature = bullet.getSourceCreature();

        double targetHealth = targetCreature.getCurrentHealth();
        double sourceAttack = sourceCreature.getCurrentAttack();
        double targetDefense = targetCreature.getCurrentDefense();
        double damage = sourceAttack - targetDefense;
        if (damage < 1)
            damage = 1;
        damage = (double) Math.round(damage);
        targetCreature.setCurrentHealth(targetHealth - damage);
        sourceCreature.setCurrentMagic(sourceCreature.getCurrentMagic()+sourceCreature.getMagicIncrementOnce());
        bullet.setValid(false);
        bullet.setVisible(false);
        if(bullet.getBulletType()== Constant.CLOSE_BULLET_TYPE){
            if(sourceCreature.getClawType()!=Constant.ClawType.NONE_CLAW){
                targetCreature.getCloseAttackImageView().setImage(ImageUrl.closeAttackImageMap.get(sourceCreature.getClawType()));
            }
            bullet.getTargetCreature().setLastCloseAttack(System.currentTimeMillis());
        }
    }
}