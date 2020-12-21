package com.sjq.gourd.collision;

import com.sjq.gourd.bullet.Bullet;
import com.sjq.gourd.bullet.BulletState;
import com.sjq.gourd.constant.Constant;
import com.sjq.gourd.constant.ImageUrl;
import com.sjq.gourd.creature.Creature;
import com.sjq.gourd.creature.CreatureState;
import com.sjq.gourd.creature.CreatureStateWithClock;

public class Collision {
    private Bullet bullet;

    public Collision(Bullet bullet) {
        this.bullet = bullet;
    }

    public void collisionEvent() {
        Creature targetCreature = bullet.getTargetCreature();
        Creature sourceCreature = bullet.getSourceCreature();
        if (bullet.getBulletState() == BulletState.THE_GOD_OF_HEALING) {
            targetCreature.setCurrentHealth(targetCreature.getCurrentHealth() + sourceCreature.getCurrentAttack());
            bullet.setValid(false);
            bullet.setVisible(false);
            sourceCreature.setCurrentMagic(sourceCreature.getCurrentMagic() + sourceCreature.getMagicIncrementOnce());
            return;
        } else if (bullet.getBulletState() == BulletState.THE_GOD_OF_HEALING_MAX) {
            targetCreature.setCurrentHealth(targetCreature.getCurrentHealth() + 1000);
            sourceCreature.getStateSet().add(new CreatureStateWithClock(CreatureState.CURE, 5000));
            //5s的治愈效果
            return;
        }


        double targetHealth = targetCreature.getCurrentHealth();
        double sourceAttack = sourceCreature.getCurrentAttack();
        double targetDefense = targetCreature.getCurrentDefense();
        double damage = sourceAttack - targetDefense;
        if (damage < 1)
            damage = 1;
        damage = (double) Math.round(damage);
        targetCreature.setCurrentHealth(targetHealth - damage);
        sourceCreature.setCurrentMagic(sourceCreature.getCurrentMagic() + sourceCreature.getMagicIncrementOnce());
        bullet.setValid(false);
        bullet.setVisible(false);

        if (bullet.getBulletState() == BulletState.THE_HEART_OF_ICE) {
            targetCreature.addState(new CreatureStateWithClock(CreatureState.FROZEN, 2000));
            return;
        } else if (bullet.getBulletState() == BulletState.THE_SON_OF_FLAME) {
            targetCreature.addState(new CreatureStateWithClock(CreatureState.FIRING, 3000));
        } else if (bullet.getBulletState() == BulletState.THE_TEETH_OF_POISONOUS || bullet.getBulletState() == BulletState.GAZE_OF_MAGIC_MIRROR) {
            targetCreature.addState(new CreatureStateWithClock(CreatureState.SERIOUS_INJURY, 5000));
        } else if (bullet.getBulletState() == BulletState.SPEED_CUT_CLAW) {
            targetCreature.addState(new CreatureStateWithClock(CreatureState.SPEED_CUT, 5000));
        }


        if (bullet.getBulletType() == Constant.CLOSE_BULLET_TYPE) {
            if (sourceCreature.getClawType() != Constant.ClawType.NONE_CLAW && targetCreature.isAlive()) {
                targetCreature.getCloseAttackImageView().setImage(ImageUrl.closeAttackImageMap.get(sourceCreature.getClawType()));
                bullet.getTargetCreature().setLastCloseAttack(System.currentTimeMillis());
            }
        } else {

        }
    }
}